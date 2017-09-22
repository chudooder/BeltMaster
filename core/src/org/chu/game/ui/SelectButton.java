package org.chu.game.ui;

import org.chu.game.BeltMaster;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class SelectButton extends Button {

    private static Drawable imageUp;
    private static Drawable imageDown;
    private static BitmapFont font;

    private String levelName;
    private int levelIndex;

    public static void setupAnimations(BeltMaster game) {
        imageUp = new Image(game.getTexture("select-box")).getDrawable();
        imageDown = new Image(game.getTexture("select-box-down")).getDrawable();
        font = game.getFont("vcr-osd-mono");
    }

    public SelectButton(String name, int index) {
        super(imageUp, imageDown);
        this.levelName = name;
        this.levelIndex = index;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        font.draw(batch, levelName, getX(), getY());
    }

    public int getLevelIndex() {
        return levelIndex;
    }



}
