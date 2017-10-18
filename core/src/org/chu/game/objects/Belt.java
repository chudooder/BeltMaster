package org.chu.game.objects;

import org.chu.game.BeltMaster;
import org.chu.game.render.RenderQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Belt extends Entity {

    // physics constants
    private static final float SLOW_SPEED = (1.0f / 30.0f);
    private static final int FALL_POINT = 5;

    // render depth
    private static final float DEPTH = 0.1f;

    // animation and rendering textures
    private static TextureRegion[] leftFrames;
    private static TextureRegion[] midFrames;
    private static TextureRegion[] rightFrames;
    private static TextureRegion cwCircle;
    private static TextureRegion cwArrow;
    private static TextureRegion ccwCircle;
    private static TextureRegion ccwArrow;
    private static TextureRegion cwLockedArrow;
    private static TextureRegion ccwLockedArrow;
    private static TextureRegion stopCircle;
    private static TextureRegion lock;

    private static Sound click;

    private int length;
    private Rectangle touchRegion;
    private int state;
    private boolean locked;

    private float timer;
    private int frame;

    public static void setupAnimations(BeltMaster game) {
        Texture sheet = game.getTexture("belt-sheet");
        TextureRegion[][] tmp = TextureRegion.split(sheet,
                sheet.getWidth() / 4, sheet.getHeight() / 4);
        leftFrames = new TextureRegion[4];
        midFrames = new TextureRegion[4];
        rightFrames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            leftFrames[i] = tmp[0][i];
            midFrames[i] = tmp[1][i];
            rightFrames[i] = tmp[2][i];
        }
        lock = tmp[3][2];

        TextureRegion[][] tmp2 = TextureRegion.split(sheet,
                sheet.getWidth() / 8, sheet.getHeight() / 8);
        cwCircle = tmp2[6][0];
        cwArrow = tmp2[6][1];
        ccwCircle = tmp2[7][0];
        ccwArrow = tmp2[7][1];
        stopCircle = tmp2[6][2];
        cwLockedArrow = tmp2[6][3];
        ccwLockedArrow = tmp2[7][3];

        click = game.getSound("conveyor-click");
    }

    public Belt(int x, int y, int length, int initialState, boolean locked) {
        super(x, y);
        this.length = length;
        if (length < 2) {
            System.err.println("ERR: Invalid belt length");
        }
        this.state = initialState;
        this.locked = locked;
        this.timer = 0;

        // create hitboxes
        hitbox = new Rectangle(x + FALL_POINT, y, length * 32 - FALL_POINT * 4, 24);
        touchRegion = new Rectangle(x - 16, y - 16, length * 32 + 32, 64);
//        touchRegion = new Rectangle(x, y, length*32, 32);

    }

    public void setState(int newState) {
        if (locked) return;
        if (newState < 0 || newState >= 5) {
            System.err.println("ERR: Try to set belt to invalid state: " + newState);
            return;
        }
        this.state = newState;
        click.play();
    }

    public int getState() {
        return state;
    }

    public Rectangle getTouchRegion() {
        return touchRegion;
    }

    @Override
    public void render(float time, RenderQueue queue) {
        timer += Gdx.graphics.getDeltaTime() * (state - 2);
        frame = (int) (timer / SLOW_SPEED % 8);
        if (frame < 0) frame += 8;
        queue.draw(leftFrames[frame % 4], x, y, DEPTH);
        for (int i = 1; i < length - 1; i++)
            queue.draw(midFrames[frame % 4], x + i * 32, y, DEPTH);
        queue.draw(rightFrames[frame % 4], x + (length - 1) * 32, y, DEPTH);
        TextureRegion circle = null;
        TextureRegion arrow = null;

        if (locked) {
            circle = stopCircle;
            if (state > 2) {
                arrow = cwLockedArrow;
            } else if (state < 2) {
                arrow = ccwLockedArrow;
            }
        } else {
            if (state > 2) {
                circle = cwCircle;
                arrow = cwArrow;
            } else if (state < 2) {
                circle = ccwCircle;
                arrow = ccwArrow;
            } else {
                circle = stopCircle;
            }
        }

        // endpoint circles
        queue.draw(circle, x + 4, y + 4, DEPTH);
        queue.draw(circle, x + (length - 1) * 32 + 12, y + 4, DEPTH);
        // scrolling arrows
        if (arrow != null) {
            for (int i = 12 + frame * 2; i < (length - 1) * 32 + frame * 2; i += 16) {
                queue.draw(arrow, x + i, y + 4, DEPTH);
            }
        }

        if (locked) {
            queue.draw(lock, x + (length - 1) * 16, y - 10, DEPTH);
        }
    }

    @Override
    public void update(double dt) {
    }
}
