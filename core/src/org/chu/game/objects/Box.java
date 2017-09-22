package org.chu.game.objects;

import org.chu.game.BeltMaster;
import org.chu.game.Constants;
import org.chu.game.render.RenderQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Box extends Entity {
    private static final float SPEED = 60.0f;
    private static final float DEPTH = 0f;

    public static final Color RED = new Color(1f, 0f, 0f, 1f);
    public static final Color GREEN = new Color(0f, 1f, 0f, 1f);
    public static final Color BLUE = new Color(0f, 0.423f, 1f, 1f);
    public static final Color YELLOW = new Color(1f, 0.753f, 0f, 1f);

    private static Animation fallingLeft;
    private static Animation fallingRight;

    private static Sound[] fallSounds;

    private Color color;
    private Belt belt;
    private Spawner spawner;

    private BoxState state;
    private float vy;
    private float vx;

    private float timer;
    private float flyTime;  // when launched, set this to the fly time of the launcher
    private Animation currentAnim;

    public static void setupAnimations(BeltMaster beltMaster) {
        Texture sheet = beltMaster.getTexture("box-sheet");
        TextureRegion[][] tmp = TextureRegion.split(sheet,
                sheet.getWidth()/4, sheet.getHeight()/2);
        TextureRegion[] fallLeft = new TextureRegion[4];
        TextureRegion[] fallRight = new TextureRegion[4];
        for(int i=0; i<4; i++) {
            fallLeft[i] = tmp[0][i];
            fallRight[i] = tmp[1][i];
        }

        fallingLeft = new Animation(0.08f, fallLeft);
        fallingLeft.setPlayMode(Animation.PlayMode.LOOP);
        fallingRight = new Animation(0.08f, fallRight);
        fallingRight.setPlayMode(Animation.PlayMode.LOOP);

        fallSounds = new Sound[3];
        fallSounds[0] = beltMaster.getSound("box-fall-1");
        fallSounds[1] = beltMaster.getSound("box-fall-2");
        fallSounds[2] = beltMaster.getSound("box-fall-3");
    }

    public Box(float x, float y, Color color) {
        super(x, y);
        this.color = color;
        this.hitbox = new Rectangle(x + 6, y + 6, 20, 20);

        state = BoxState.FREE_FALLING;
        currentAnim = fallingLeft;
    }

    @Override
    public void update(double dt) {
        if(state == BoxState.STANDING) {
            timer = 0;
            // check to see if this box needs to fall
            Rectangle test = new Rectangle(hitbox);
            test.setPosition(x+4, y+3);
            if(!test.overlaps(belt.hitbox)) {
                if(belt.getState() < 2) {
                    state = BoxState.FALLING_LEFT;
                    currentAnim = fallingLeft;
                } else if(belt.getState() > 2) {
                    state = BoxState.FALLING_RIGHT;
                    currentAnim = fallingRight;
                }
            } else {
                // translate box in velocity of conveyor belt
                x += vx * dt;
                vx = (belt.getState() - 2) * SPEED;
            }
        }

        else if(state == BoxState.FALLING_LEFT || state == BoxState.FALLING_RIGHT) {
            timer += dt;
            // switch state if box is sufficiently past the end of the belt
            if(timer > 0.12f) {
                state = BoxState.FREE_FALLING;
                vx = 0;
            }
            // translate box in velocity of conveyor belt
            x += vx * dt;
            // make box fall
            y += vy * dt;
            vy += Constants.GRAVITY * dt;
        }

        else if(state == BoxState.FREE_FALLING) {
            timer += dt;
            x += vx * dt;
            y += vy * dt;
            vy += Constants.GRAVITY * dt;

            for(Entity e : screen.getEntities()) {
                if(hitbox.overlaps(e.hitbox)) {
                    collide(e);
                }
            }
        }

        else if(state == BoxState.FLYING) {
            timer += dt;
            x += vx * dt;
            y += vy * dt;
            vy += Constants.GRAVITY * dt;
            if(timer > flyTime * 0.9) {
                state = BoxState.FREE_FALLING;
            }
        }

        // update hitbox position
        hitbox.setPosition(x+6, y+6);

        // destroy box if out of bounds
        if(y < -32) {
            screen.removeEntity(this);
        }
    }

    private void collide(Entity e) {
        if(e instanceof Belt) {
            Belt b = (Belt)e;
            this.belt = b;
            // push up out of belt
            this.y = belt.y + belt.hitbox.height - 4;
            vy = 0;
            // set to the appropriate state
            state = BoxState.STANDING;
            // play sound
//            Sound sound = fallSounds[(int)(Math.random()*fallSounds.length)];
//            sound.play();

        } else if(e instanceof Truck) {
            Truck t = (Truck) e;
            if(t.getColor().equals(color)) {
                screen.addEntity(new ScorePopup(t.x, t.y+64, true));
                screen.getScoreHUD().score();
            } else {
                screen.addEntity(new ScorePopup(t.x, t.y+64, false));
                screen.getScoreHUD().miss();
            }
            screen.removeEntity(this);
            t.startBounce();

        } else if(e instanceof Recycler) {
            screen.recycle(this);
            screen.removeEntity(this);

        } else if(e instanceof Boxapult) {
            Boxapult b = (Boxapult) e;
            x = b.getX0();
            y = b.getY0();
            vx = b.getVx();
            vy = b.getVy();
            flyTime = b.getFlyTime();
            state = BoxState.FLYING;
            timer = 0;
        }
    }

    @Override
    public void render(float time, RenderQueue queue) {
        queue.draw(currentAnim.getKeyFrame(timer), x, y, DEPTH, color);
    }

    public Color getColor() {
        return color;
    }

    public void setSpawner(Spawner s) {
        this.spawner = s;
    }

    public Spawner getSpawner() {
        return spawner;
    }


    private enum BoxState {
        STANDING,
        FALLING_LEFT,
        FALLING_RIGHT,
        FREE_FALLING,
        FLYING
    }
}
