package org.chu.game.objects;

import org.chu.game.RenderQueue;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Recycler extends Entity {
	
	private static final float BACK_DEPTH = 0.5f;
	private static final float FRONT_DEPTH = -0.5f;
	private static TextureRegion backSprite;
	private static TextureRegion frontSprite;
	
	public static void setupAnimations(AssetManager assets) {
		Texture sheet = assets.get("game-objects.png", Texture.class);
		frontSprite = new TextureRegion(sheet, 0, 64, 32, 32);
		backSprite = new TextureRegion(sheet, 32, 64, 32, 32);
	}

	public Recycler(float x, float y) {
		super(x, y);
		hitbox = new Rectangle(x, y, 32, 4);
	}

	@Override
	public void update() {
		
	}

	@Override
	public void render(float time, RenderQueue queue) {
		queue.draw(backSprite, x, y, BACK_DEPTH);
		queue.draw(frontSprite, x, y, FRONT_DEPTH);
	}

}
