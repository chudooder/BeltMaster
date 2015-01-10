package org.chu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Box extends Entity {
	
	private static final float GRAVITY = 200.0f;
	private static final float SPEED = 30.0f;
	private static final float DEPTH = 0f;
	
	public static final Color RED = new Color(1f, 0f, 0f, 1f);
	public static final Color GREEN = new Color(0f, 1f, 0f, 1f);
	public static final Color BLUE = new Color(0f, 0.423f, 1f, 1f);
	public static final Color YELLOW = new Color(1f, 0.753f, 0f, 1f);
	
	private static Animation fallingLeft;
	private static Animation fallingRight;

	private Color color;
	private Belt belt;
	
	private BoxState state;
	private float vy;
	private float vx;
	
	private float timer;
	private Animation currentAnim;
	
	public static void setupAnimations(AssetManager assets) {
		Texture sheet = assets.get("game-objects.png", Texture.class);
		TextureRegion[][] tmp = TextureRegion.split(sheet, 
				sheet.getWidth()/8, sheet.getHeight()/8);
		TextureRegion[] fallLeft = new TextureRegion[4];
		TextureRegion[] fallRight = new TextureRegion[4];
		for(int i=0; i<4; i++) {
			fallLeft[i] = tmp[0][4+i];
			fallRight[i] = tmp[1][4+i];
		}
		TextureRegion stand = tmp[0][4];
		
		fallingLeft = new Animation(0.065f, fallLeft);
		fallingLeft.setPlayMode(Animation.PlayMode.LOOP);
		fallingRight = new Animation(0.065f, fallRight);
		fallingRight.setPlayMode(Animation.PlayMode.LOOP);
	}
	
	public Box(float x, float y, Color color) {
		super(x, y);
		this.color = color;
		this.hitbox = new Rectangle(x + 4, y + 4, 10, 10);
		
		state = BoxState.FREE_FALLING;
		currentAnim = fallingLeft;
	}
	
	@Override
	public void update() {
		if(state == BoxState.STANDING) {
			timer = 0;
			// check to see if this box needs to fall
			Rectangle test = new Rectangle(hitbox);
			test.setPosition(x+4, y+3);
			if(!test.overlaps(belt.hitbox)) {
				if(belt.getState() < 2) {
					state = BoxState.FALLING_LEFT;
					currentAnim = fallingLeft;
				} else if(belt.getState() > 2) {
					state = BoxState.FALLING_RIGHT;
					currentAnim = fallingRight;
				}
			} else {
				// translate box in velocity of conveyor belt
				vx = (belt.getState() - 2) * SPEED;
				x += vx * Gdx.graphics.getDeltaTime();
			}
		}
		
		else if(state == BoxState.FALLING_LEFT || state == BoxState.FALLING_RIGHT) {
			timer += Gdx.graphics.getDeltaTime();
			// switch state if box is sufficiently past the end of the belt
			if(timer > 0.12f) {
				state = BoxState.FREE_FALLING;
			}
			// translate box in velocity of conveyor belt
			x += vx * Gdx.graphics.getDeltaTime();
			// make box fall
			vy -= GRAVITY * Gdx.graphics.getDeltaTime();
			y += vy * Gdx.graphics.getDeltaTime();
		}
		
		else if(state == BoxState.FREE_FALLING) {
			timer += Gdx.graphics.getDeltaTime();
			vy -= GRAVITY * Gdx.graphics.getDeltaTime();
			y += vy * Gdx.graphics.getDeltaTime();
			
			for(Entity e : screen.getEntities()) {
				if(hitbox.overlaps(e.hitbox)) { 
					if(e instanceof Belt) {
						Belt b = (Belt)e;
						this.belt = b;
						// push up out of belt
						this.y = belt.y + belt.hitbox.height - 4;
						vy = 0;
						// set to the appropriate state
						state = BoxState.STANDING;
					} else if(e instanceof Truck) {
						Truck t = (Truck) e;
						screen.addEntity(new ScorePopup(t.x, t.y+32, t.getColor().equals(color)));
						screen.removeEntity(this);
					}
				}
			}
		}
		
		// update hitbox position
		hitbox.setPosition(x+4, y+4);
		
		// destroy box if out of bounds
		if(y < -32) {
			screen.removeEntity(this);
		}
	}

	@Override
	public void render(float time, RenderQueue queue) {
		queue.draw(currentAnim.getKeyFrame(timer), x, y, DEPTH, color);
	}
	
	private enum BoxState {
		STANDING,
		FALLING_LEFT,
		FALLING_RIGHT,
		FREE_FALLING
	}

}
