package org.chu.game.objects;

import org.chu.game.BeltMaster;
import org.chu.game.Constants;
import org.chu.game.render.RenderCall2d;
import org.chu.game.render.RenderCall3d;
import org.chu.game.render.RenderQueue;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Box extends Entity {
    private static final float SPEED = 60.0f;
    private static final float PUSH_OUT_SPEED = 30.0f;
    private static final float DEPTH = 0f;

    private static final float BOX_MODEL_WIDTH = 15f;

    public static final Color RED = new Color(1f, 0f, 0f, 1f);
    public static final Color GREEN = new Color(0f, 1f, 0f, 1f);
    public static final Color BLUE = new Color(0f, 0.423f, 1f, 1f);
    public static final Color YELLOW = new Color(1f, 0.753f, 0f, 1f);

    private static Animation<TextureRegion> fallingLeft;
    private static Animation<TextureRegion> fallingRight;

    private static Sound[] fallSounds;

    private Belt belt;
    private Spawner spawner;

    private Color color;
    private BoxState state;
    private float vy;
    private float vx;

    private float fallTime;
    private float fallDirSign;
    private float flyTime;  // when launched, set this to the fly time of the launcher
    private Animation<TextureRegion> currentAnim;

    private ModelInstance modelInstance;

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

        fallingLeft = new Animation<TextureRegion>(0.08f, fallLeft);
        fallingLeft.setPlayMode(Animation.PlayMode.LOOP);
        fallingRight = new Animation<TextureRegion>(0.08f, fallRight);
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

        // setup model
        ModelBuilder modelBuilder = new ModelBuilder();
        Model model = modelBuilder.createBox(BOX_MODEL_WIDTH, BOX_MODEL_WIDTH, BOX_MODEL_WIDTH,
                new Material(ColorAttribute.createDiffuse(color)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        modelInstance = new ModelInstance(model);
        modelInstance.transform = new Matrix4()
                .trn(x+8+BOX_MODEL_WIDTH/2, y+8+BOX_MODEL_WIDTH/2, 0)
                .rotate(Vector3.X, 30);
    }

    @Override
    public void update(double dt) {
        if(state == BoxState.STANDING) {
            fallTime = 0;
            // check to see if this box needs to fall
            Rectangle test = new Rectangle(hitbox);
            test.setPosition(x+4, y+3);
            if(!test.overlaps(belt.hitbox)) {
                if(this.x < belt.hitbox.getX() + belt.hitbox.getWidth() / 2) {
                    state = BoxState.FALLING_LEFT;
                    currentAnim = fallingLeft;
                    fallDirSign = 1;
                } else {
                    state = BoxState.FALLING_RIGHT;
                    currentAnim = fallingRight;
                    fallDirSign = -1;
                }
            } else {
                // translate box in velocity of conveyor belt
                x += vx * dt;
                vx = (belt.getState() - 2) * SPEED;
            }

            handleCollisions(dt);
        }

        else if(state == BoxState.FALLING_LEFT || state == BoxState.FALLING_RIGHT) {
            fallTime += dt;
            // switch state if box is sufficiently past the end of the belt
            if(fallTime > 0.12f) {
                state = BoxState.FREE_FALLING;
                vx = 0;
            }
            // translate box in velocity of conveyor belt
            x += vx * dt;
            // make box fall
            y += vy * dt;
            vy += Constants.GRAVITY * dt;

            handleCollisions(dt);
        }

        else if(state == BoxState.FREE_FALLING) {
            fallTime += dt;
            x += vx * dt;
            y += vy * dt;
            vy += Constants.GRAVITY * dt;

            handleCollisions(dt);
        }

        else if(state == BoxState.FLYING) {
            fallTime += dt;
            x += vx * dt;
            y += vy * dt;
            vy += Constants.GRAVITY * dt;
            if(fallTime > flyTime * 0.9) {
                state = BoxState.FREE_FALLING;
            }

            // notably, do NOT handle collisions while flying
        }

        // update hitbox position
        hitbox.setPosition(x+6, y+6);

        // update model position
        modelInstance.transform = new Matrix4()
                .trn(x+8+BOX_MODEL_WIDTH/2, y+8+BOX_MODEL_WIDTH/2, 0)
                .rotate(Vector3.X, 30)
                .rotate(Vector3.Z, fallTime * 340 * fallDirSign);

        // destroy box if out of bounds
        if(y < -32) {
            screen.removeEntity(this);
        }
    }

    private void handleCollisions(double dt) {
        for(Entity e : screen.getEntities()) {
            if(e == this) continue;
            if(hitbox.overlaps(e.hitbox)) {
                collide(e, dt);
            }
        }
    }

    private void collide(Entity e, double dt) {
        if(e instanceof Box) {
            // push out of the box if we're on a conveyor belt only
            if(this.state == BoxState.STANDING) {
                Box b = (Box)e;
                if(b.x > this.x) {
                    this.x -= PUSH_OUT_SPEED * dt;
                } else {
                    this.x += PUSH_OUT_SPEED * dt;
                }
            }
        } else if(e instanceof Belt) {
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
            fallTime = 0;
        }
    }

    @Override
    public void render(float time, RenderQueue queue) {
//        queue.draw(currentAnim.getKeyFrame(fallTime), x, y, DEPTH, color);
        queue.renderModel(modelInstance);
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
