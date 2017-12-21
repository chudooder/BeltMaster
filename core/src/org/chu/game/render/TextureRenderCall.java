package org.chu.game.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureRenderCall extends RenderCall2d {

    TextureRegion region;
    float x;
    float y;
    float width;
    float height;
    Color color;

    public TextureRenderCall(TextureRegion region, float x, float y, float z, Color color) {
        this(region, x, y, z, color, region.getRegionWidth(), region.getRegionHeight());
    }

    public TextureRenderCall(TextureRegion region, float x, float y, float z,
            Color color, float width, float height) {
        super(z);
        this.region = region;
        this.x = x;
        this.y = y;
        this.color = color;
        this.width = width;
        this.height = height;
    }

    @Override
    public void execute(SpriteBatch batch) {
        batch.setColor(color);
        batch.draw(region, x, y, width, height);
    }

}
