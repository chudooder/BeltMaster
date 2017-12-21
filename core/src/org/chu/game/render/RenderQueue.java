package org.chu.game.render;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Receives and orders render calls so that sprites
 * are drawn in order of depth.
 * @author Shawn
 *
 */
public class RenderQueue {

    private PriorityQueue<RenderCall2d> queue2d;
    private Queue<RenderCall3d> queue3d;
    private int scale;

    public RenderQueue(int scale) {
        queue2d = new PriorityQueue<RenderCall2d>();
        queue3d = new LinkedList<RenderCall3d>();
        this.scale = scale;
    }

    public void draw(TextureRegion region, float x, float y, float z) {
        draw(region, x, y, z, Color.WHITE);
    }

    public void draw(TextureRegion region, float x, float y, float z, Color c) {
        queue2d.add(new TextureRenderCall(region, x*scale, y*scale, z, c));
    }

    public void draw(TextureRegion region, float x, float y, float z, Color c, float width, float height) {
        queue2d.add(new TextureRenderCall(region, x*scale, y*scale, z, c, width*scale, height*scale));
    }

    public void print(BitmapFont font, String text, float x, float y, float z, Color c) {
        queue2d.add(new FontRenderCall(font, text, x*scale, y*scale, z, c));
    }

    public void renderModel(final ModelInstance instance) {
        queue3d.add(new RenderCall3d() {
            @Override
            public void execute(ModelBatch batch, Environment env) {
                batch.render(instance, env);
            }
        });
    }

    public void add(RenderCall2d call) {
        queue2d.add(call);
    }

    public void add(RenderCall3d call) { queue3d.add(call); }

    public void execute(SpriteBatch spriteBatch, ModelBatch modelBatch, Camera camera, Environment env) {
        spriteBatch.setProjectionMatrix(camera.projection);
        spriteBatch.setTransformMatrix(camera.view);

        spriteBatch.begin();
        while(!queue2d.isEmpty()) {
            RenderCall2d call = queue2d.poll();
            call.execute(spriteBatch);
        }
        spriteBatch.end();
        RenderCall2d.callId = 0;

        modelBatch.begin(camera);
        while(!queue3d.isEmpty()) {
            RenderCall3d call = queue3d.poll();
            call.execute(modelBatch, env);
        }
        modelBatch.end();
    }

}

