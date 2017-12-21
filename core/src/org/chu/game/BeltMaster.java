package org.chu.game;

import org.chu.game.objects.Belt;
import org.chu.game.objects.Box;
import org.chu.game.objects.Boxapult;
import org.chu.game.objects.Recycler;
import org.chu.game.objects.ScorePopup;
import org.chu.game.objects.Spawner;
import org.chu.game.objects.Truck;
import org.chu.game.render.SpriteSheet;
import org.chu.game.screen.GameScreen;
import org.chu.game.screen.MainMenuScreen;
import org.chu.game.ui.GoToLevelSelectButton;
import org.chu.game.ui.PauseButton;
import org.chu.game.ui.ResumeButton;
import org.chu.game.ui.ScoreHUD;
import org.chu.game.ui.SelectLevelButton;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.util.HashMap;
import java.util.Map;

public class BeltMaster extends Game {

    public static final int TARGET_WIDTH = 800;
    public static final int TARGET_HEIGHT = 450;
    private static int SCALE;
    public SpriteBatch spriteBatch;
    public ModelBatch modelBatch;
    public BitmapFont font;

    private AssetManager assets;
    private Map<String, SpriteSheet> spriteSheets;
    private Map<String, ShaderProgram> shaders;

    private float stateTime;

    @Override
    public void create() {
        // figure out what graphics scale we need
        if(Gdx.graphics.getWidth() > TARGET_WIDTH) {
            SCALE = 2;
        } else {
            SCALE = 1;
        }

        spriteBatch = new SpriteBatch();
        modelBatch = new ModelBatch();
        assets = new AssetManager();
        spriteSheets = new HashMap<String, SpriteSheet>();
        shaders = new HashMap<String, ShaderProgram>();

        loadAssets();
        loadShaders();

        while(!assets.update());

        makeSpriteSheets();

        // setup animations
        Belt.setupAnimations(this);
        Box.setupAnimations(this);
        Truck.setupAnimations(this);
        Spawner.setupAnimations(this);
        Recycler.setupAnimations(this);
        Boxapult.setupAnimations(this);
        ScorePopup.setupAnimations(this);
        SelectLevelButton.setupAnimations(this);
        ScoreHUD.setupAnimations(this);
        PauseButton.setupAnimations(this);
        ResumeButton.setupAnimations(this);
        GoToLevelSelectButton.setupAnimations(this);
        GameScreen.setupAnimations(this);

        this.setScreen(new MainMenuScreen(this));
    }

    private void loadAssets() {
        String suffix = SCALE == 2 ? "@2x" : "";
        assets.load("img/box-sheet"+suffix+".png", Texture.class);
        assets.load("img/belt-sheet"+suffix+".png", Texture.class);
        assets.load("img/game-objects"+suffix+".png", Texture.class);
        assets.load("img/select-box"+suffix+".png", Texture.class);
        assets.load("img/select-box-down"+suffix+".png", Texture.class);
        assets.load("img/scorebar-overlay"+suffix+".png", Texture.class);
        assets.load("img/scorebar-fill"+suffix+".png", Texture.class);
        assets.load("img/pause-button"+suffix+".png", Texture.class);
        assets.load("img/resume-button"+suffix+".png", Texture.class);
        assets.load("img/level-select-button"+suffix+".png", Texture.class);
        assets.load("img/boxapult"+suffix+".png", Texture.class);
        assets.load("img/bg"+suffix+".png", Texture.class);
        assets.load("fonts/vcr-osd-mono"+suffix+".fnt", BitmapFont.class);
        assets.load("audio/spawner.wav", Sound.class);
        assets.load("audio/box-fall-1.wav", Sound.class);
        assets.load("audio/box-fall-2.wav", Sound.class);
        assets.load("audio/box-fall-3.wav", Sound.class);
        assets.load("audio/conveyor-click.wav", Sound.class);
        assets.load("audio/miss.wav", Sound.class);
        assets.load("audio/score.wav", Sound.class);
    }

    private void loadShaders() {
        ShaderProgram defaultShader = new ShaderProgram(
                new FileHandle("shaders/default.vert"),
                new FileHandle("shaders/default.frag"));
        shaders.put("default", defaultShader);
    }

    private void makeSpriteSheets() {
        spriteSheets.put("game-objects", new SpriteSheet(getTexture("game-objects"), 16, 16));
    }

    @Override
    public void render() {
        super.render();
        stateTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        modelBatch.dispose();
        assets.clear();
    }

    public float getStateTime() {
        return stateTime;
    }

    public int getScale() {
        return SCALE;
    }

    /**
     * Returns the actual width of the screen in pixels
     * @return
     */
    public int getScreenWidth() {
        return getScale() * TARGET_WIDTH;
    }

    /**
     * Returns the actual height of the screen in pixels
     * @return
     */
    public int getScreenHeight() {
        return getScale() * TARGET_HEIGHT;
    }

    /**
     * Sets the current game screen after disposing of the previous one.
     * @param from
     * @param to
     */
    public void setScreenWithDispose(Screen from, Screen to) {
        from.dispose();
        setScreen(to);
    }

    public Texture getTexture(String string) {
        String suffix = SCALE == 2 ? "@2x" : "";
        return assets.get("img/"+string+suffix+".png", Texture.class);
    }

    public Sound getSound(String string) {
        return assets.get("audio/"+string+".wav", Sound.class);

    }

    public BitmapFont getFont(String string) {
        String suffix = SCALE == 2 ? "@2x" : "";
        return assets.get("fonts/"+string+suffix+".fnt", BitmapFont.class);
    }

    public SpriteSheet getSpriteSheet(String string) {
        return spriteSheets.get(string);
    }

    public ShaderProgram getShader(String string) {
        return shaders.get(string);
    }
}
