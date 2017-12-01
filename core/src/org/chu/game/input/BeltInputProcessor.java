package org.chu.game.input;

import org.chu.game.objects.Belt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BeltInputProcessor implements InputProcessor {

    private Belt belt;
    private Rectangle box;

    private boolean isSelected;
    private int touchPointerIndex;
    private long touchTime;
    private float touchX;

    private Viewport viewport;
    private int scale;

    public BeltInputProcessor(Belt b, Viewport viewport, int scale) {
        belt = b;
        box = b.getTouchRegion();
        isSelected = false;
        this.viewport = viewport;
        this.scale = scale;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 xy = viewport.getCamera().unproject(new Vector3(screenX, screenY, 0));
        float x = xy.x / scale;
        float y = xy.y / scale;
        if(x < box.x || x > box.x + box.width
                || y < box.y || y > box.y + box.height)
            return false;

        isSelected = true;
        touchPointerIndex = pointer;
        touchX = x;
        touchTime = TimeUtils.millis();
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(!isSelected || this.touchPointerIndex != pointer) {
            return false;
        }

        // reset the variables
        isSelected = false;
        touchPointerIndex = -1;

        Vector3 xy = viewport.getCamera().unproject(new Vector3(screenX, screenY, 0));
        float x = xy.x / scale;
        float y = xy.y / scale;

        long curTime = TimeUtils.millis();
        float secondsDiff = (curTime - touchTime) / 1000f;

        if(secondsDiff > 0.75f) {
            return false;
        }

        float vx = (x - touchX) / secondsDiff;

        if(Math.abs(vx) < 20) {
            belt.setState(2);   // stop belt
        }
        else if(vx > 1500) {
            belt.setState(4);   // moving right fast
        } else if(vx > 0) {
            belt.setState(3);   // moving right
        } else if(vx > -1500) {
            belt.setState(1);   // moving left
        } else {
            belt.setState(0);   // moving left fast
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
