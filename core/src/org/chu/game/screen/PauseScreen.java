package org.chu.game.screen;

import org.chu.game.BeltMaster;
import org.chu.game.ui.LevelSelectButton;
import org.chu.game.ui.ResumeButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class PauseScreen implements Screen {

    private Stage stage;
    private Table table;
    private BeltMaster game;
    private GameScreen gameScreen;

    public PauseScreen(BeltMaster game, GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.game = game;
        stage = new Stage();
        table = new Table();

        LabelStyle style = new LabelStyle(game.getFont("vcr-osd-mono"), Color.WHITE);
        table.setFillParent(true);
        table.add(new Label("PAUSED", style));
        table.row();

        setupButtons();

        stage.addActor(table);
    }

    private void setupButtons() {
        // resume button
        ResumeButton resume = new ResumeButton();
        resume.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreenWithDispose(PauseScreen.this, gameScreen);
            }
        });
        table.add(resume);

        // level select button
        LevelSelectButton levelSelect = new LevelSelectButton();
        levelSelect.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Return to the level select screen, disposing both the game screen and this screen
                gameScreen.dispose();
                game.setScreenWithDispose(PauseScreen.this, new LevelSelectScreen(game));
            }
        });
        table.add(levelSelect);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }

}
