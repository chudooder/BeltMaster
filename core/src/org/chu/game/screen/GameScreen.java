package org.chu.game.screen;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.chu.game.BeltMaster;
import org.chu.game.input.BeltInputProcessor;
import org.chu.game.objects.Belt;
import org.chu.game.objects.Box;
import org.chu.game.objects.Boxapult;
import org.chu.game.objects.Entity;
import org.chu.game.objects.Recycler;
import org.chu.game.objects.Spawner;
import org.chu.game.objects.Truck;
import org.chu.game.render.RenderQueue;
import org.chu.game.ui.PauseButton;
import org.chu.game.ui.ScoreHUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {

    private static final int NUM_TILES_V = 13;
    private static final int NUM_TILES_H = 25;

    public enum GameOutcome {
        WON,
        LOST
    }

    private static TextureRegion background;

    private BeltMaster game;
    private boolean isPaused;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Environment environment;
    private List<Entity> entities;
    private List<Spawner> spawners;

    private float topY;

    private ScoreHUD scoreHUD;

    private Queue<Entity> addQueue;
    private Queue<Entity> removeQueue;

    private InputMultiplexer input;
    private RenderQueue renderQueue;

    public static void setupAnimations(BeltMaster game) {
        background = new TextureRegion(game.getTexture("bg"));
        System.out.println(background.getRegionHeight());
    }

    public GameScreen(BeltMaster game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.getScreenWidth(), game.getScreenHeight());
        camera.near = -1000f;
        camera.far = 1000f;
        camera.update(true);
        viewport = new ExtendViewport(game.getScreenWidth(), game.getScreenHeight(), camera);
        viewport.update(game.getScreenWidth(), game.getScreenHeight());

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(Color.WHITE, new Vector3(0f, -1f, 0f)));

        entities = new ArrayList<Entity>();
        spawners = new ArrayList<Spawner>();
        scoreHUD = new ScoreHUD();

        addQueue = new LinkedList<Entity>();
        removeQueue = new LinkedList<Entity>();

        input = new InputMultiplexer();

        renderQueue = new RenderQueue(game.getScale());

//        debugSetup();
        addEntity(scoreHUD);
        addEntity(new PauseButton());
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

    public void setScore(int maxScore, int maxMiss) {
        scoreHUD.set(maxScore, maxMiss);
    }

    public ScoreHUD getScoreHUD() {
        return scoreHUD;
    }

    public void createBelt(int x, int y, int length, int initState, boolean locked) {
        Belt b = new Belt(x, y, length, initState, locked);
        addEntity(b);
        input.addProcessor(new BeltInputProcessor(b, viewport, game.getScale()));
//        input.addProcessor(new GestureDetector(new BeltGestureProcessor(b, viewport, game.getScale())));
    }

    public void createBox(int x, int y, Color c) {
        Box b = new Box(x, y, c);
        addEntity(b);
    }

    public void createTruck(int x, Color c) {
        Truck t = new Truck(x, 0, c);
        addEntity(t);
    }

    public void createSpawner(int x, Queue<Color> colors, float spawnTime, float offset) {
        Spawner s = new Spawner(x, 32*NUM_TILES_V, colors, spawnTime, offset);
        spawners.add(s);
        addEntity(s);
    }

    public void createRecycler(int x) {
        Recycler r = new Recycler(x, 0);
        addEntity(r);
    }

    public void createBoxapult(int x, int y, int destX, int destY) {
        Boxapult b = new Boxapult(x, y, destX, destY);
        addEntity(b);
    }

    public void addEntity(Entity e) {
        e.setScreen(this);
        addQueue.add(e);
    }


    @Override
    public void render(float delta) {
        // update things
        update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();

        // draw background
        renderQueue.draw(background, 0, 0, 999);

        // get render calls
        for(Entity e : entities) {
            e.render(game.getStateTime(), renderQueue);
        }

        // draw things
        renderQueue.execute(game.spriteBatch, game.modelBatch, camera, environment);
    }

    private void update(double dt) {
        if(isPaused) return;

        // update box positions
        for(Entity e : entities) {
            e.update(dt);
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
        System.out.println(width);
        viewport.update(width, height);
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
        Gdx.input.setInputProcessor(input);
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

    public void recycle(Box box) {
        Spawner spawner = spawners.get((int)(Math.random()*spawners.size()));
        if(box.getSpawner() == spawner) {
            spawner = spawners.get((int)(Math.random()*spawners.size()));
        }
        spawner.recycleBlock(box.getColor());
    }

    public void endGame(GameOutcome i) {
        if(i == GameOutcome.LOST) {
            System.out.println("NA WORKERS LUL");
            game.setScreenWithDispose(this, new LevelSelectScreen(this.game));
        } else {
            System.out.println("EU ARTISANS PogChamp");
            game.setScreenWithDispose(this, new LevelSelectScreen(this.game));
        }
    }

    public Viewport getViewport() {
        return viewport;
    }

    public float getTopY() {
        return viewport.unproject(new Vector2(0, 0)).y;
    }
}
