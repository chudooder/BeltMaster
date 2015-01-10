package org.chu.game;

import org.chu.game.objects.Belt;
import org.chu.game.objects.Box;
import org.chu.game.objects.ScorePopup;
import org.chu.game.objects.Spawner;
import org.chu.game.objects.Truck;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BeltMaster extends Game {
	
	public SpriteBatch batch;
	public BitmapFont font;
	
	private OrthographicCamera camera;
	private AssetManager assets;
	
	private float stateTime;
	
	@Override
	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 400, 240);
		
		batch = new SpriteBatch();
		font = new BitmapFont();
		assets = new AssetManager();
		
		loadAssets();
		
		while(!assets.update());

		// setup animations
		Belt.setupAnimations(assets);
		Box.setupAnimations(assets);
		Truck.setupAnimations(assets);
		Spawner.setupAnimations(assets);
		ScorePopup.setupAnimations(assets);
		
		this.setScreen(new MainMenuScreen(this));
	}
	
	private void loadAssets() {
		assets.load("game-objects.png", Texture.class);
	}

	@Override
	public void render() {
		super.render();
		stateTime += Gdx.graphics.getDeltaTime();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
		assets.clear();
	}
	
	public float getStateTime() {
		return stateTime;
	}
}
