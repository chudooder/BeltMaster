package org.chu.game.render;

import java.util.PriorityQueue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Receives and orders render calls so that sprites
 * are drawn in order of depth.
 * @author Shawn
 *
 */
public class RenderQueue {

    private PriorityQueue<RenderCall> queue;
    private int scale;

    public RenderQueue(int scale) {
        queue = new PriorityQueue<RenderCall>();
        this.scale = scale;
    }

    public void draw(TextureRegion region, float x, float y, float z) {
        draw(region, x, y, z, Color.WHITE);
    }

    public void draw(TextureRegion region, float x, float y, float z, Color c) {
        queue.add(new TextureRenderCall(region, x*scale, y*scale, z, c));
    }

    public void draw(TextureRegion region, float x, float y, float z, Color c, float width, float height) {
        queue.add(new TextureRenderCall(region, x*scale, y*scale, z, c, width*scale, height*scale));
    }

    public void add(RenderCall call) {
        queue.add(call);
    }

    public void execute(SpriteBatch batch) {
        while(!queue.isEmpty()) {
            RenderCall call = queue.poll();
            call.execute(batch);
        }
        RenderCall.callId = 0;
    }

}

