package org.chu.game.input;

import org.chu.game.objects.Belt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BeltGestureProcessor implements GestureListener {

    private Belt belt;
    private Rectangle box;
    private boolean isSelected;
    private Viewport viewport;
    private int scale;

    public BeltGestureProcessor(Belt b, Viewport viewport, int scale) {
        this.belt = b;
        this.box = belt.getTouchRegion();
        this.viewport = viewport;
        this.scale = scale;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector3 xy = viewport.getCamera().unproject(new Vector3(x, y, 0));
        x = xy.x / scale;
        y = xy.y / scale;
        if(x < box.x || x > box.x + box.width
                || y < box.y || y > box.y + box.height)
            return false;
        isSelected = true;
        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if(isSelected) {
            belt.setState(2);   // stop belt
            isSelected = false;
            return true;
        }
        isSelected = false;
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if(isSelected) {
            if(velocityX > 1500) {
                belt.setState(4);   // moving right fast
            } else if(velocityX > 0) {
                belt.setState(3);   // moving right
            } else if(velocityX > -1500) {
                belt.setState(1);   // moving left
            } else {
                belt.setState(0);   // moving left fast
            }
            isSelected = false;
            return true;
        }
        isSelected = false;
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        isSelected = false;
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
            Vector2 pointer1, Vector2 pointer2) {
        isSelected = false;
        return false;
    }

    @Override
    public void pinchStop() {

    }

}
