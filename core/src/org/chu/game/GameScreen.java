package org.chu.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.chu.game.input.BeltGestureProcessor;
import org.chu.game.objects.Belt;
import org.chu.game.objects.Box;
import org.chu.game.objects.Entity;
import org.chu.game.objects.Spawner;
import org.chu.game.objects.Truck;

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
		
//		debugSetup();
		
		Gdx.input.setInputProcessor(input);
	}
	
	private void debugSetup() {
		createBelt(68, 100, 7, 0, false);
		createBelt(196, 100, 5, 0, false);
		createBelt(84, 132, 3, 2, false);
		createBelt(32, 68, 8, 4, true);
		
		createBox(108, 200, Box.GREEN);
		
		Queue<Color> queue = new LinkedList<Color>();
		queue.add(Box.RED);
		queue.add(Box.GREEN);
		queue.add(Box.BLUE);
		createSpawner(100, queue, 3f, 0f);
		
		createTruck(8, Box.RED);
	}

	public void createBelt(int x, int y, int length, int initState, boolean locked) {
		Belt b = new Belt(x, y, length, initState, locked);
		addEntity(b);
//		input.addProcessor(new BeltInputProcessor(b));
		input.addProcessor(new GestureDetector(new BeltGestureProcessor(b)));
	}
	
	public void createBox(int x, int y, Color c) {
		Box b = new Box(x, y, c);
		b.setScreen(this);
		addEntity(b);
	}
	
	public void createTruck(int x, Color c) {
		Truck t = new Truck(x, 0, c);
		addEntity(t);
	}
	
	public void createSpawner(int x, Queue<Color> colors, float spawnTime, float offset) {
		Spawner s = new Spawner(x, 240-16, colors, spawnTime, offset);
		s.setScreen(this);
		addEntity(s);
	}
	
	public void addEntity(Entity e) {
		e.setScreen(this);
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
