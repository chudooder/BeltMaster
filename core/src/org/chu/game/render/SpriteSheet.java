package org.chu.game.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Shawn on 10/7/2017.
 */

public class SpriteSheet {
    private Texture texture;
    private int gridSizeW;
    private int gridSizeH;

    public SpriteSheet(Texture tex, int numCellsH, int numCellsV) {
        this.texture = tex;
        this.gridSizeW = tex.getWidth() / numCellsH;
        this.gridSizeH = tex.getHeight() / numCellsV;
    }

    public TextureRegion getRegion(int gridX, int gridY, int gridW, int gridH) {
        TextureRegion region = new TextureRegion(texture, gridSizeW * gridX, gridSizeH * gridY,
                gridSizeW * gridW, gridSizeH * gridH);
        return region;
    }
}
