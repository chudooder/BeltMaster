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
	private boolean[] validStates;
	private int state;
	
	// physics constants
	private static final float SLOW_SPEED = 0.05f;
	private static final int FALL_POINT = 7;
	
	// render depth
	private static final float DEPTH = 0.1f;
	
	// animation and rendering textures
	private static Animation leftCW;
	private static Animation midCW;
	private static Animation rightCW;
	private static Animation leftCCW;
	private static Animation midCCW;
	private static Animation rightCCW;
	private static TextureRegion cwCircle;
	private static TextureRegion cwArrow;
	private static TextureRegion ccwCircle;
	private static TextureRegion ccwArrow;

	public static void setupAnimations(AssetManager assets) {
		Texture sheet = assets.get("game-objects.png", Texture.class);
		TextureRegion[][] tmp = TextureRegion.split(sheet, 
				sheet.getWidth()/8, sheet.getHeight()/8);
		TextureRegion[] leftFrames = new TextureRegion[4];
		TextureRegion[] midFrames = new TextureRegion[4];
		TextureRegion[] rightFrames = new TextureRegion[4];
		for(int i=0; i<4; i++) {
			leftFrames[i] = tmp[4][4+i];
			midFrames[i] = tmp[5][4+i];
			rightFrames[i] = tmp[6][4+i];
		}
		
		cwCircle = new TextureRegion(sheet, 64, 112, 8, 8);
		cwArrow = new TextureRegion(sheet, 72, 112, 8, 8);
		ccwCircle = new TextureRegion(sheet, 64, 120, 8, 8);
		ccwArrow = new TextureRegion(sheet, 72, 120, 8, 8);
		
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
	public void render(float time, RenderQueue queue) {
		float realtime = time;
		if(state == 0 || state == 4) 
			realtime = time * 2;
		if(state > 2) {
			// main body
			queue.draw(leftCW.getKeyFrame(realtime, true), x, y, DEPTH);
			for(int i=1; i<length-1; i++)
				queue.draw(midCW.getKeyFrame(realtime, true), x+i*16, y, DEPTH);
			queue.draw(rightCW.getKeyFrame(realtime, true), x+(length-1)*16, y, DEPTH);
			// endpoint circles
			queue.draw(cwCircle, x+2, y+2, DEPTH);
			queue.draw(cwCircle, x+(length-1)*16+6, y+2, DEPTH);
			// scrolling arrows
			int offset = (int) ((realtime/SLOW_SPEED) % 8);
			for(int i=6+offset; i<(length-1)*16+offset; i+=8) {
				queue.draw(cwArrow, x + i, y + 2, DEPTH);
			}
		} else if(state < 2) {
			queue.draw(leftCCW.getKeyFrame(realtime, true), x, y, DEPTH);
			for(int i=1; i<length-1; i++)
				queue.draw(midCCW.getKeyFrame(realtime, true), x+i*16, y, DEPTH);
			queue.draw(rightCCW.getKeyFrame(realtime, true), x+(length-1)*16, y, DEPTH);
			// endpoint circles
			queue.draw(ccwCircle, x+2, y+2, DEPTH);
			queue.draw(ccwCircle, x+(length-1)*16+6, y+2, DEPTH);
			// scrolling arrows
			int offset = (int) (8 - (realtime/SLOW_SPEED % 8));
			for(int i=(length-1)*16+offset; i>=6+offset; i-=8) {
				queue.draw(ccwArrow, x + i, y + 2, DEPTH);
			}
		} else {
			queue.draw(leftCCW.getKeyFrame(0, true), x, y, DEPTH);
			for(int i=1; i<length-1; i++)
				queue.draw(midCCW.getKeyFrame(0, true), x+i*16, y, DEPTH);
			queue.draw(rightCCW.getKeyFrame(0, true), x+(length-1)*16, y, DEPTH);
		}
	}

	@Override
	public void update() {
		return;
	}



	

}
