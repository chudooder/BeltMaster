package org.chu.game.objects;

import org.chu.game.BeltMaster;
import org.chu.game.render.RenderQueue;
import org.chu.game.render.SpriteSheet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Recycler extends Entity {

    private static final float BACK_DEPTH = 0.5f;
    private static final float FRONT_DEPTH = -0.5f;
    private static TextureRegion backSprite;
    private static TextureRegion frontSprite;

    public static void setupAnimations(BeltMaster beltMaster) {
        SpriteSheet sheet = beltMaster.getSpriteSheet("game-objects");
        frontSprite = sheet.getRegion(0, 6, 4, 2);
        backSprite = sheet.getRegion(4, 4, 4, 3);
    }

    public Recycler(float x, float y) {
        super(x, y);
        hitbox = new Rectangle(x, y, 32, 4);
    }

    @Override
    public void update(double dt) {

    }

    @Override
    public void render(float time, RenderQueue queue) {
        queue.draw(backSprite, x, y+16, BACK_DEPTH);
        queue.draw(frontSprite, x, y, FRONT_DEPTH);
    }

}
