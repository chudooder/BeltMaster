package org.chu.game.objects;

import org.chu.game.RenderQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ScorePopup extends Entity {
	
	private static float SPEED = 64f;
	private static float DEPTH = -0.5f;
	private static TextureRegion goodTex;
	private static TextureRegion missTex;
	
	private TextureRegion type;
	private float timer;
	private float vy;
	
	public static void setupAnimations(AssetManager assets) {
		Texture sheet = assets.get("game-objects.png", Texture.class);
		goodTex = new TextureRegion(sheet, 0, 96, 32, 16);
		missTex = new TextureRegion(sheet, 0, 112, 32, 16);
	}

	public ScorePopup(float x, float y, boolean good) {
		super(x, y);
		type = good ? goodTex : missTex;
		vy = SPEED;
		timer = 0;
	}

	@Override
	public void update() {
		vy -= 64f * Gdx.graphics.getDeltaTime();
		y += vy * Gdx.graphics.getDeltaTime();
		timer += Gdx.graphics.getDeltaTime();
		
		if(timer > 1.0f) screen.removeEntity(this);
	}

	@Override
	public void render(float time, RenderQueue queue) {
		queue.draw(type, x, y, DEPTH);
	}

}
