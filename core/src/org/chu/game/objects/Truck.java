package org.chu.game.objects;

import java.util.HashMap;
import java.util.Map;

import org.chu.game.BeltMaster;
import org.chu.game.render.RenderQueue;
import org.chu.game.render.SpriteSheet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Truck extends Entity {

    private static final float FRONT_DEPTH = -0.5f;
    private static final float BACK_DEPTH = 0.5f;

    private static Map<Color, TextureRegion> fronts;
    private static TextureRegion wheels;
    private static TextureRegion back;

    private Color color;
    private float bounceTimer = -1;
    private float bounceOffset = 0;

    public static void setupAnimations(BeltMaster beltMaster) {
        SpriteSheet sheet = beltMaster.getSpriteSheet("game-objects");
        fronts = new HashMap<Color, TextureRegion>();

        fronts.put(Box.RED, sheet.getRegion(0, 0, 4, 4));
        fronts.put(Box.GREEN, sheet.getRegion(4, 0, 4, 4));
        fronts.put(Box.BLUE, sheet.getRegion(8, 0, 4, 4));
        fronts.put(Box.YELLOW, sheet.getRegion(12, 0, 4, 4));

        wheels = sheet.getRegion(4, 7, 4, 1);
        back = sheet.getRegion(0, 4, 4, 2);
    }

    public Truck(int x, int y, Color color) {
        super(x, y);
        this.color = color;
        hitbox = new Rectangle(x, y, 64, 16);
    }

    @Override
    public void update(double dt) {
        if(bounceTimer >= 0) {
            bounceTimer += dt;
            bounceOffset = (float) (3 * Math.sin(bounceTimer * 2 * Math.PI));
            if(bounceTimer > 0.5f) {
                bounceTimer = -1;
                bounceOffset = 0;
            }
        }
    }

    @Override
    public void render(float time, RenderQueue queue) {
        queue.draw(wheels, x, y, FRONT_DEPTH);
        queue.draw(fronts.get(color), x, y - bounceOffset, FRONT_DEPTH);
        queue.draw(back, x, y+64 - bounceOffset, BACK_DEPTH);
    }

    public Color getColor() {
        return color;
    }

    public void startBounce() {
        bounceTimer = 0;
    }

}
