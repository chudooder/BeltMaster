package org.chu.game.objects;

import org.chu.game.BeltMaster;
import org.chu.game.render.RenderQueue;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Recycler extends Entity {

    private static final float BACK_DEPTH = 0.5f;
    private static final float FRONT_DEPTH = -0.5f;
    private static TextureRegion backSprite;
    private static TextureRegion frontSprite;

    public static void setupAnimations(BeltMaster beltMaster) {
        Texture sheet = beltMaster.getTexture("game-objects");
        frontSprite = new TextureRegion(sheet, 0, sheet.getHeight()/4*3,
                sheet.getWidth()/4, sheet.getHeight()/4);
        backSprite = new TextureRegion(sheet, sheet.getWidth()/4,
                sheet.getHeight()/2, sheet.getWidth()/4, sheet.getHeight()/8*3);
    }

    public Recycler(float x, float y) {
        super(x, y);
        hitbox = new Rectangle(x, y, 32, 4);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(float time, RenderQueue queue) {
        queue.draw(backSprite, x, y+16, BACK_DEPTH);
        queue.draw(frontSprite, x, y, FRONT_DEPTH);
    }

}
