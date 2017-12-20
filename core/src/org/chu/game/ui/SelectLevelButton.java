package org.chu.game.ui;

import org.chu.game.BeltMaster;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class SelectLevelButton extends Button {

    public static float BUTTON_SIZE;

    private static Drawable imageUp;
    private static Drawable imageDown;
    private static BitmapFont font;

    private String levelName;
    private int levelIndex;

    public static void setupAnimations(BeltMaster game) {
        imageUp = new Image(game.getTexture("select-box")).getDrawable();
        imageDown = new Image(game.getTexture("select-box-down")).getDrawable();
        font = game.getFont("vcr-osd-mono");

        BUTTON_SIZE = game.getTexture("select-box").getWidth();
        System.out.println("Button size: " + BUTTON_SIZE);
    }

    public SelectLevelButton(String name, int index) {
        super(imageUp, imageDown);
        this.levelName = name;
        this.levelIndex = index;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        font.setColor(Color.WHITE);
        font.draw(batch, levelName, getX(), getY());
    }

    public int getLevelIndex() {
        return levelIndex;
    }



}
