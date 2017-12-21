package org.chu.game.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class RenderCall2d implements Comparable<RenderCall2d> {

    static int callId = 0;
    public float z;
    public int id;

    public RenderCall2d(float z) {
        this.z = z;
        id = callId++;
    }

    public abstract void execute(SpriteBatch batch);


    @Override
    public int compareTo(RenderCall2d other) {
        // sort by z-depth first
        if(this.z > other.z) {
            return -1;
        } else if(this.z < other.z) {
            return 1;
        }

        return this.id - other.id;
    }
}
