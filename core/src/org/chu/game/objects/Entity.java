package org.chu.game.objects;

import org.chu.game.render.RenderQueue;
import org.chu.game.screen.GameScreen;

import com.badlogic.gdx.math.Rectangle;

public abstract class Entity {

    public float x;
    public float y;

    public Rectangle hitbox;

    protected GameScreen screen;

    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
        hitbox = new Rectangle();
    }

    public void setScreen(GameScreen s) {
        screen = s;
    }

    public abstract void update();

    public abstract void render(float time, RenderQueue queue);
}
