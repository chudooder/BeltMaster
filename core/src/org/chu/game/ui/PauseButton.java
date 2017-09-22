package org.chu.game.ui;

import org.chu.game.BeltMaster;
import org.chu.game.objects.Entity;
import org.chu.game.render.RenderQueue;
import org.chu.game.screen.PauseScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class PauseButton extends Entity {

    private static final float DEPTH = -1.0f;

    private static TextureRegion texture;

    private float width;
    private float height;

    public static void setupAnimations(BeltMaster game) {
        texture = new TextureRegion(game.getTexture("pause-button"));
    }

    public PauseButton() {
        super(0, 0);
    }

    @Override
    public void update(double dt) {
        int scale = screen.getGame().getScale();
        y = screen.getTopY() / scale - 32;
        width = 32 * scale;
        height = 32 * scale;
        if(Gdx.input.isTouched()) {
            Vector3 posScaled = screen.getViewport().getCamera().unproject(
                    new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            Vector3 pos = new Vector3(posScaled.x / scale, posScaled.y / scale, 0);
            if(pos.x < x || pos.y < y || pos.x > x+width || pos.y > y+height) {
                return;
            }
            // do stuff
            screen.getGame().setScreen(new PauseScreen(screen.getGame(), screen));
        }
    }

    @Override
    public void render(float time, RenderQueue queue) {
        queue.draw(texture, x, y, DEPTH);
    }

}
