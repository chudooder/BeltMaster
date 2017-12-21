package org.chu.game.render;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

/**
 * Created by Chudooder on 12/20/2017.
 */

public abstract class RenderCall3d {
    public abstract void execute(ModelBatch batch, Environment env);
}