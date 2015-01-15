package org.chu.game;

import org.chu.game.objects.Belt;
import org.chu.game.objects.Box;
import org.chu.game.objects.Recycler;
import org.chu.game.objects.ScorePopup;
import org.chu.game.objects.Spawner;
import org.chu.game.objects.Truck;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BeltMaster extends Game {
	
	private static int SCALE = 2;
	public SpriteBatch batch;
	public BitmapFont font;
	
	private OrthographicCamera camera;
	private AssetManager assets;
	
	private float stateTime;
	
	@Override
	public void create() {
		System.out.println(Gdx.graphics.getWidth() +" "+ Gdx.graphics.getHeight());
		// figure out what graphics scale we need
		if(Gdx.graphics.getWidth() > 800) {
			SCALE = 2;
		} else {
			SCALE = 1;
		}
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800*SCALE, 450*SCALE);
		
		batch = new SpriteBatch();
		assets = new AssetManager();
		
		loadAssets();
		
		while(!assets.update());

		// setup animations
		Belt.setupAnimations(this);
		Box.setupAnimations(this);
		Truck.setupAnimations(this);
		Spawner.setupAnimations(this);
		Recycler.setupAnimations(this);
		ScorePopup.setupAnimations(this);
		SelectButton.setupAnimations(this);
		
		this.setScreen(new MainMenuScreen(this));
	}
	
	private void loadAssets() {
		String suffix = SCALE == 2 ? "@2x" : "";
		assets.load("box-sheet"+suffix+".png", Texture.class);
		assets.load("belt-sheet"+suffix+".png", Texture.class);
		assets.load("game-objects"+suffix+".png", Texture.class);
		assets.load("select-box"+suffix+".png", Texture.class);
		assets.load("select-box-down"+suffix+".png", Texture.class);
		assets.load("audio/spawner.wav", Sound.class);
		assets.load("audio/box-fall-1.wav", Sound.class);
		assets.load("audio/box-fall-2.wav", Sound.class);
		assets.load("audio/box-fall-3.wav", Sound.class);
		assets.load("audio/conveyor-click.wav", Sound.class);
		assets.load("audio/miss.wav", Sound.class);
		assets.load("audio/score.wav", Sound.class);
		assets.load("fonts/vcr-osd-mono.fnt", BitmapFont.class);
	}

	@Override
	public void render() {
		super.render();
		stateTime += Gdx.graphics.getDeltaTime();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		assets.clear();
	}
	
	public float getStateTime() {
		return stateTime;
	}
	
	public int getScale() {
		return SCALE;
	}

	public Texture getTexture(String string) {
		String suffix = SCALE == 2 ? "@2x" : "";
		return assets.get(string+suffix+".png", Texture.class);
	}

	public Sound getSound(String string) {
		return assets.get("audio/"+string+".wav", Sound.class);
		
	}

	public BitmapFont getFont(String string) {
		String suffix = SCALE == 2 ? "@2x" : "";
		return assets.get("fonts/"+string+suffix+".fnt", BitmapFont.class);
	}
}
