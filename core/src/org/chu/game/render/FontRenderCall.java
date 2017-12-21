package org.chu.game.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Shawn on 11/9/2017.
 */

public class FontRenderCall extends RenderCall2d {
    BitmapFont font;
    String text;
    float x;
    float y;
    Color color;

    public FontRenderCall(BitmapFont font, String text, float x, float y, float z, Color color) {
        super(z);
        this.font = font;
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    public void execute(SpriteBatch batch) {
        Color prev = font.getColor();
        font.setColor(color);
        font.draw(batch, text, x, y);
        font.setColor(prev);
    }
}
