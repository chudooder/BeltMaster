package org.chu.game.objects;

import java.util.LinkedList;
import java.util.Queue;

import org.chu.game.BeltMaster;
import org.chu.game.render.RenderQueue;
import org.chu.game.render.SpriteSheet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Spawner extends Entity {

    private static final float DEPTH = -0.5f;

    private float spawnTimer;            // internal clock
    private float spawnTime;            // time between block spawns
    private float offset;
    private Queue<Color> originalQueue;    // original queue to copy over in case reset
    private Queue<Color> spawnColors;    // queue of blocks to spawn

    private static TextureRegion sprite;
    private static Sound boxPop;

    private boolean soundPlayed;

    public static void setupAnimations(BeltMaster beltMaster) {
        SpriteSheet sheet = beltMaster.getSpriteSheet("game-objects");
        sprite = sheet.getRegion(12, 4, 4, 4);
        boxPop = beltMaster.getSound("spawner");
    }

    public Spawner(float x, float y, Queue<Color> spawnColors, float spawnTime, float offset) {
        super(x, y);
        this.spawnColors = spawnColors;
        this.spawnTime = spawnTime;
        this.offset = offset;
        this.spawnTimer = offset;
        this.soundPlayed = false;

        originalQueue = new LinkedList<Color>(spawnColors);
    }

    @Override
    public void update(double dt) {
        if(!spawnColors.isEmpty()) {
            spawnTimer += dt;
        }
        if(spawnTimer > spawnTime) {
            // adjust timer
            spawnTimer -= spawnTime;
            // spawn a block
            if(!spawnColors.isEmpty()) {
                Color color = spawnColors.poll();
                if(color != null) {
                    Box box = new Box(x + 16, y + 0, color);
                    box.setSpawner(this);
                    screen.addEntity(box);
                }
            }
        }
    }

    public void reset() {
        spawnColors = new LinkedList<Color>(originalQueue);
        spawnTimer = offset;
    }

    public void recycleBlock(Color color) {
        spawnColors.add(color);
    }

    @Override
    public void render(float time, RenderQueue queue) {
        float offsetY = 0;
        if(spawnTimer > spawnTime - 0.5f && spawnTimer < spawnTime - 0.1f) {
            offsetY = (spawnTimer + 0.5f - spawnTime) * 10;
            soundPlayed = false;
        } else if (spawnTimer >= spawnTime - 0.1f) {
            if(!soundPlayed) {
                boxPop.play();
                soundPlayed = true;
            }
            offsetY = (spawnTime - spawnTimer) * 10;
        }
        queue.draw(sprite, x, y + offsetY, DEPTH);
    }

}
