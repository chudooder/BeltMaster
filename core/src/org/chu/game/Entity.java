package org.chu.game;

import com.badlogic.gdx.math.Rectangle;

public abstract class Entity {
	
	public float x;
	public float y;
	
	public Rectangle hitbox;
	
	protected GameScreen screen;
	
	public Entity(int x, int y) {
		this.x = x;
		this.y = y;
		hitbox = new Rectangle();
	}

	public abstract void update();
	
	public abstract void render(float time, RenderQueue queue);
}
