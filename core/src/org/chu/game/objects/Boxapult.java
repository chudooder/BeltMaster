package org.chu.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import org.chu.game.BeltMaster;
import org.chu.game.Constants;
import org.chu.game.render.RenderQueue;

/**
 * Created by Chudooder on 9/22/2017.
 */

public class Boxapult extends Entity {

    private static final int DELTA_H = 32;
    private static final float DEPTH = 0.1f;

    private static TextureRegion idle;

    private final float vx;
    private final float vy;
    private final float x0;
    private final float y0;
    private final float flyTime;

    public static void setupAnimations(BeltMaster game) {
        Texture tex = game.getTexture("boxapult");
        idle = new TextureRegion(tex, 0, 0, tex.getWidth(), tex.getHeight());
    }

    public Boxapult(int x, int y, int destX, int destY) {
        super(x, y);

        // position of the launched block
        x0 = x;
        y0 = y + 10;

        float h;    // vertical distance between launch position and peak, should be negative
        if(destY > y0) {    // destination is above launch position
            h = destY - y0 + DELTA_H;
        } else {
            h = DELTA_H;
        }

        // calculate initial y velocity
        vy = (float) Math.sqrt(-2f * Constants.GRAVITY * h);

        // calculate time t to complete arc
        flyTime = (float) (-vy - Math.sqrt(Math.pow(vy, 2) - 2 * Constants.GRAVITY * (y0 - destY)))
                / Constants.GRAVITY;

        // calculate initial x velocity
        vx = (destX - x0) / flyTime;

        System.out.printf("(%f, %f) to (%d, %d): vx = %f, vy = %f, t = %f", x0, y0, destX, destY, vx, vy, flyTime);

        this.hitbox = new Rectangle(x, y, 32, 32);
    }

    public float getVx() { return vx; }
    public float getVy() { return vy; }
    public float getX0() { return x0; }
    public float getY0() { return y0; }
    public float getFlyTime() { return flyTime; }

    @Override
    public void update(double dt) {

    }

    @Override
    public void render(float time, RenderQueue queue) {
        queue.draw(idle, x, y, DEPTH);
    }
}
