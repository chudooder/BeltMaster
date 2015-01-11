package org.chu.game.objects;

import org.chu.game.RenderQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Belt extends Entity {
	
	// physics constants
	private static final float SLOW_SPEED = (1.0f/30.0f);
	private static final int FALL_POINT = 5;
	
	// render depth
	private static final float DEPTH = 0.1f;
	
	// animation and rendering textures
	private static TextureRegion[] leftFrames; 
	private static TextureRegion[] midFrames;
	private static TextureRegion[] rightFrames;
	private static TextureRegion cwCircle;
	private static TextureRegion cwArrow;
	private static TextureRegion ccwCircle;
	private static TextureRegion ccwArrow;
	private static TextureRegion cwLockedArrow;
	private static TextureRegion ccwLockedArrow;
	private static TextureRegion stopCircle;
	private static TextureRegion lock;
	
	private int length;
	private Rectangle touchRegion;
	private int state;
	private boolean locked;
	
	private float timer;
	private int frame;

	public static void setupAnimations(AssetManager assets) {
		Texture sheet = assets.get("game-objects.png", Texture.class);
		TextureRegion[][] tmp = TextureRegion.split(sheet, 
				sheet.getWidth()/8, sheet.getHeight()/8);
		leftFrames = new TextureRegion[4];
		midFrames = new TextureRegion[4];
		rightFrames = new TextureRegion[4];
		for(int i=0; i<4; i++) {
			leftFrames[i] = tmp[4][4+i];
			midFrames[i] = tmp[5][4+i];
			rightFrames[i] = tmp[6][4+i];
		}
		
		cwCircle = new TextureRegion(sheet, 64, 112, 8, 8);
		cwArrow = new TextureRegion(sheet, 72, 112, 8, 8);
		ccwCircle = new TextureRegion(sheet, 64, 120, 8, 8);
		ccwArrow = new TextureRegion(sheet, 72, 120, 8, 8);
		stopCircle = new TextureRegion(sheet, 80, 112, 8, 8);
		cwLockedArrow = new TextureRegion(sheet, 88, 112, 8, 8);
		ccwLockedArrow = new TextureRegion(sheet, 88, 120, 8, 8);
		lock = new TextureRegion(sheet, 96, 112, 16, 16);
	}
	
	public Belt(int x, int y, int length, int initialState, boolean locked) {
		super(x, y);
		this.length = length;
		if(length < 2) {
			System.err.println("ERR: Invalid belt length");
		}
		this.state = initialState;
		this.locked = locked;
		this.timer = 0;
		
		// create hitboxes
		hitbox = new Rectangle(x+FALL_POINT, y, length*16-FALL_POINT*2, 12);
		touchRegion = new Rectangle(x-8, y-8, length*16 + 16, 16 + 16);
	}
	
	public void setState(int newState) {
		if(locked) return;
		if(newState < 0 || newState >= 5) {
			System.err.println("ERR: Try to set belt to invalid state: "+newState);
			return;
		}
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
		timer += Gdx.graphics.getDeltaTime() * (state - 2);
		// clamp timer to [0,1)
		if(timer < 0) {
			timer += SLOW_SPEED;
			frame = (frame - 1 + 8) % 8;
		}
		if(timer > SLOW_SPEED) {
			timer -= SLOW_SPEED;
			frame = (frame + 1) % 8;
		}
		// calculate frame
		queue.draw(leftFrames[frame%4], x, y, DEPTH);
		for(int i=1; i<length-1; i++)
			queue.draw(midFrames[frame%4], x+i*16, y, DEPTH);
		queue.draw(rightFrames[frame%4], x+(length-1)*16, y, DEPTH);
		TextureRegion circle = null;
		TextureRegion arrow = null;
		
		if(locked) {
			circle = stopCircle;
			if(state > 2) {
				arrow = cwLockedArrow;
			} else if(state < 2) {
				arrow = ccwLockedArrow;
			}
		} else {
			if(state > 2) {
				circle = cwCircle;
				arrow = cwArrow;
			} else if(state < 2) {
				circle = ccwCircle;
				arrow = ccwArrow;
			} else {
				circle = stopCircle;
			}
		}

		// endpoint circles
		queue.draw(circle, x+2, y+2, DEPTH);
		queue.draw(circle, x+(length-1)*16+6, y+2, DEPTH);
		// scrolling arrows
		if(arrow != null) {
			for(int i=6+frame; i<(length-1)*16+frame; i+=8) {
				queue.draw(arrow, x + i, y + 2, DEPTH);
			}
		}

		if(locked) {
			queue.draw(lock, x+(length-1)*8, y-5, DEPTH);
		}
	}

	@Override
	public void update() {
		return;
	}



	

}
