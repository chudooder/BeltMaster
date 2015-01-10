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
	private List<Entity> entities;
	
	private Queue<Entity> addQueue;
	private Queue<Entity> removeQueue;
	
	private InputMultiplexer input;
	
	private RenderQueue renderQueue;
	
	public GameScreen(BeltMaster game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 400, 240);
		entities = new ArrayList<Entity>();
		
		addQueue = new LinkedList<Entity>();
		removeQueue = new LinkedList<Entity>();
		
		input = new InputMultiplexer();
		
		renderQueue = new RenderQueue();
		
		createBelt(68, 100, 7, new boolean[]{true, true, true, true, true}, 0);
		createBelt(196, 100, 5, new boolean[]{true, true, true, true, true}, 0);
		createBelt(84, 132, 3, new boolean[]{true, true, true, true, true}, 2);
		createBelt(32, 68, 8, new boolean[]{true, true, true, true, true}, 4);
		
		createBox(108, 200, Box.GREEN);
		
		createTruck(8, 16, Box.GREEN);
		
		Gdx.input.setInputProcessor(input);
	}
	
	private void createBelt(int x, int y, int length, boolean[] states, int initState) {
		Belt b = new Belt(x, y, length, states, initState);
		addEntity(b);
//		input.addProcessor(new BeltInputProcessor(b));
		input.addProcessor(new GestureDetector(new BeltGestureProcessor(b)));
	}
	
	private void createBox(int x, int y, Color c) {
		Box b = new Box(x, y, c);
		b.screen = this;
		addEntity(b);
	}
	
	private void createTruck(int x, int y, Color c) {
		Truck t = new Truck(x, y, c);
		addEntity(t);
	}
	
	public void addEntity(Entity e) {
		e.screen = this;
		addQueue.add(e);
	}


	@Override
	public void render(float delta) {
		// update things
		update();
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		// get render calls
		for(Entity e : entities) {
			e.render(game.getStateTime(), renderQueue);
		}
		
		// draw things
		game.batch.begin();
		renderQueue.execute(game.batch);
		game.batch.end();
	}

	private void update() {
		if(isPaused) return;
		
		// update box positions
		for(Entity e : entities) {
			e.update();
		}
		
		// process remove queue
		while(!removeQueue.isEmpty()) {
			entities.remove(removeQueue.poll());
		}
		
		// process add queue
		while(!addQueue.isEmpty()) {
			entities.add(addQueue.poll());
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

	public void removeEntity(Entity entity) {
		removeQueue.add(entity);
	}

	public List<Entity> getEntities() {
		return entities;
	}

}
