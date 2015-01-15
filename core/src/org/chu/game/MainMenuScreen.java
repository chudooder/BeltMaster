package org.chu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class MainMenuScreen implements Screen {
	
	private BeltMaster game;
	private OrthographicCamera camera;
	private BitmapFont font;
	
	public MainMenuScreen(BeltMaster game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 400, 240);
		font = game.getFont("vcr-osd-mono");
	}

	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		// draw things
		font.draw(game.batch, "BELTMASTER", 100, 150);
		game.batch.end();
		
		// update things
		update();
	}
	
	private void update() {
		if(Gdx.input.isTouched()) {
			game.setScreen(new LevelSelectScreen(game));
			dispose();
		}
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		
	}

}
