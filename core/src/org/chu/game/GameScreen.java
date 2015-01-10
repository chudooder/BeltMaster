package org.chu.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;

public class GameScreen implements Screen {
	
	private BeltMaster game;
	private boolean isPaused;
	private OrthographicCamera camera;
	private List<Belt> belts;
	private List<Box> boxes;
	
	private Queue<Box> removeQueue;
	
	private InputMultiplexer input;
	
	private RenderQueue renderQueue;
	
	public GameScreen(BeltMaster game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 400, 240);
		
		belts = new ArrayList<Belt>();
		boxes = new ArrayList<Box>();
		
		input = new InputMultiplexer();
		
		renderQueue = new RenderQueue();
		removeQueue = new LinkedList<Box>();
		
		createBelt(68, 100, 7, new boolean[]{true, true, true, true, true}, 0);
//		createBelt(196, 100, 5, new boolean[]{true, true, true, true, true}, 0);
//		createBelt(84, 132, 3, new boolean[]{true, true, true, true, true}, 2);
//		createBelt(32, 68, 8, new boolean[]{true, true, true, true, true}, 4);
		
		createBox(108, 200, Color.RED);
		
		
		Gdx.input.setInputProcessor(input);
	}
	
	private void createBelt(int x, int y, int length, boolean[] states, int initState) {
		Belt b = new Belt(x, y, length, states, initState);
		b.screen = this;
		getBelts().add(b);
//		input.addProcessor(new BeltInputProcessor(b));
		input.addProcessor(new GestureDetector(new BeltGestureProcessor(b)));
	}
	
	private void createBox(int x, int y, Color c) {
		Box b = new Box(x, y, c);
		b.screen = this;
		boxes.add(b);
	}


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		
		game.batch.setProjectionMatrix(camera.combined);
		

		// get render calls
		for(Belt b : getBelts()) {
			b.render(game.getStateTime(), renderQueue);
		}
		for(Box b : boxes) {
			b.render(game.getStateTime(), renderQueue);
		}
		
		// draw things
		game.batch.begin();
		renderQueue.execute(game.batch);
		game.batch.end();
		
		// update things
		update();
	}

	private void update() {
		if(isPaused) return;
		
		// update box positions
		for(Box b : boxes) {
			b.update();
		}
		
		// process remove queue
		while(!removeQueue.isEmpty()) {
			boxes.remove(removeQueue.poll());
		}
	}

	@Override
	public void resize(int width, int height) {
		return;
	}

	@Override
	public void pause() {
		isPaused = true;
	}

	@Override
	public void resume() {
		isPaused = false;
	}

	@Override
	public void hide() {

	}

	@Override
	public void show() {

	}

	@Override
	public void dispose() {
		
	}
	
	public BeltMaster getGame() {
		return game;
	}

	public List<Belt> getBelts() {
		return belts;
	}

	public List<Box> getBoxes() {
		return boxes;
	}

	public void removeBox(Box box) {
		removeQueue.add(box);
	}

}
