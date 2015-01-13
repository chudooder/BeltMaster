package org.chu.game.objects;

import org.chu.game.BeltMaster;
import org.chu.game.RenderQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ScorePopup extends Entity {
	
	private static float SPEED = 64f;
	private static float DEPTH = -0.5f;
	private static TextureRegion goodTex;
	private static TextureRegion missTex;
	
	private static Sound score;
	private static Sound miss;
	
	private TextureRegion type;
	private float timer;
	private float vy;
	
	public static void setupAnimations(BeltMaster beltMaster) {
		Texture sheet = beltMaster.getTexture("game-objects");
		TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth()/4, sheet.getHeight()/4);
		goodTex = tmp[2][2];
		missTex = tmp[3][2];
		
		score = beltMaster.getSound("score");
		miss = beltMaster.getSound("miss");
	}

	public ScorePopup(float x, float y, boolean good) {
		super(x, y);
		type = good ? goodTex : missTex;
		vy = SPEED;
		timer = 0;
		Sound sound = good ? score : miss;
		sound.play();
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
