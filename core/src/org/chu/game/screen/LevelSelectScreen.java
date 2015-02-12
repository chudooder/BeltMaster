package org.chu.game.screen;

import java.util.List;

import org.chu.game.BeltMaster;
import org.chu.game.LevelLoader;
import org.chu.game.ui.SelectButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LevelSelectScreen implements Screen {
	
	private BeltMaster game;
	private LevelLoader levelLoader;
	
	private Stage stage;
	private Table table;
	private ShapeRenderer shapeRenderer;
	
	public LevelSelectScreen(BeltMaster game) {
		this.game = game;
		levelLoader = new LevelLoader(game);
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		table = new Table();
		table.setFillParent(true);
		setupButtons();
		stage.addActor(table);
		
		shapeRenderer = new ShapeRenderer();
	}
	
	private void setupButtons() {
		List<String> levels = levelLoader.getLevelNames();
		for(int i=0; i<levels.size(); i++) {
			final SelectButton box = new SelectButton(levels.get(i), i);
			box.addListener(new ClickListener(){
				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					int index = box.getLevelIndex();
					GameScreen screen = levelLoader.loadLevel(index);
					game.setScreen(screen);
				}
			});
			table.add(box).pad(32);
		}

	}
	

	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
		table.drawDebug(shapeRenderer);
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
