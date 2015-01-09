package org.chu.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Belt extends Entity {
	
	private int length;
	private Rectangle touchRegion;
	
	// represents the valid states this belt can be in (left, stopped, right; slow, fast)
	private boolean[] validStates;
	private int state;
	
	private static final float SLOW_SPEED = 0.05f;
	private static final int FALL_POINT = 7;
	
	private static Animation leftCW;
	private static Animation midCW;
	private static Animation rightCW;
	private static Animation leftCCW;
	private static Animation midCCW;
	private static Animation rightCCW;

	public static void setupAnimations(AssetManager assets) {
		Texture sheet = assets.get("belt-sheet.png", Texture.class);
		TextureRegion[][] tmp = TextureRegion.split(sheet, 
				sheet.getWidth()/4, sheet.getHeight()/4);
		TextureRegion[] leftFrames = tmp[0];
		TextureRegion[] midFrames = tmp[1];
		TextureRegion[] rightFrames = tmp[2];
		
		leftCW = new Animation(SLOW_SPEED, leftFrames);
		midCW = new Animation(SLOW_SPEED, midFrames);
		rightCW = new Animation(SLOW_SPEED, rightFrames);
		
		leftCCW = new Animation(SLOW_SPEED, leftFrames);
		leftCCW.setPlayMode(Animation.PlayMode.REVERSED);
		
		midCCW = new Animation(SLOW_SPEED, midFrames);
		midCCW.setPlayMode(Animation.PlayMode.REVERSED);
		
		rightCCW = new Animation(SLOW_SPEED, rightFrames);
		rightCCW.setPlayMode(Animation.PlayMode.REVERSED);
	}
	
	public Belt(int x, int y, int length, boolean[] validStates, int initialState) {
		super(x, y);
		this.length = length;
		if(length < 2) {
			System.err.println("ERR: Invalid belt length");
		}
		this.validStates = validStates;
		this.state = initialState;
		
		// create hitboxes
		hitbox = new Rectangle(x+FALL_POINT, y, length*16-FALL_POINT*2, 12);
		touchRegion = new Rectangle(x-8, y-8, length*16 + 16, 16 + 16);
	}
	
	public void setState(int newState) {
		if(newState < 0 || newState >= 5) {
			System.err.println("ERR: Try to set belt to invalid state: "+newState);
			return;
		}
		if(!validStates[newState]) return;
		this.state = newState;
	}
	
	public int getState() {
		return state;
	}
	
	public Rectangle getTouchRegion() {
		return touchRegion;
	}

	@Override
	public void render(float time, SpriteBatch batch) {
		float realtime = time;
		if(state == 2) 
			realtime = 0;
		if(state == 0 || state == 4) 
			realtime = time * 2;
		if(state > 2) {
			batch.setColor(0.7f, 0.9f, 1.0f, 1.0f);
			batch.draw(leftCW.getKeyFrame(realtime, true), x, y);
			for(int i=1; i<length-1; i++)
				batch.draw(midCW.getKeyFrame(realtime, true), x+i*16, y);
			batch.draw(rightCW.getKeyFrame(realtime, true), x+(length-1)*16, y);
		} else {
			batch.setColor(0.8f, 1.0f, 0.8f, 1.0f);
			batch.draw(leftCCW.getKeyFrame(realtime, true), x, y);
			for(int i=1; i<length-1; i++)
				batch.draw(midCCW.getKeyFrame(realtime, true), x+i*16, y);
			batch.draw(rightCCW.getKeyFrame(realtime, true), x+(length-1)*16, y);
		}
		batch.setColor(Color.WHITE);
	}

	@Override
	public void update() {
		return;
	}



	

}
