package org.chu.game.input;

import org.chu.game.objects.Belt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BeltGestureProcessor implements GestureListener {
	
	private Belt belt;
	private Rectangle box;
	private boolean isSelected;
	
	public BeltGestureProcessor(Belt b) {
		this.belt = b;
		this.box = belt.getTouchRegion();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		x = x * 800 / Gdx.graphics.getWidth();
		y = (Gdx.graphics.getHeight() - y) * 450 / Gdx.graphics.getHeight();
		if(x < box.x || x > box.x + box.width 
				|| y < box.y || y > box.y + box.height)
			return false;
		isSelected = true;
		return true;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		if(isSelected) {
			belt.setState(2);
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
				belt.setState(4);
			} else if(velocityX > 0) {
				belt.setState(3);
			} else if(velocityX > -1500) {
				belt.setState(1);
			} else {
				belt.setState(0);
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
		System.out.println("Zoom");
		isSelected = false;
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		System.out.println("Pinch");
		isSelected = false;
		return false;
	}

}
