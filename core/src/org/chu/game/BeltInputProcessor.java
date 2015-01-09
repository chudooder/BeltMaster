package org.chu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;

public class BeltInputProcessor implements InputProcessor {
	
	private Belt belt;
	private Rectangle box;
	
	public BeltInputProcessor(Belt b) {
		belt = b;
		box = b.hitbox;
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
		// check button
		if(button != Input.Buttons.LEFT)
			return false;
		
		// correct touch location to match view
//		System.out.println("screenX: "+screenX +" screenY: "+screenY+ " sizeX: "+Gdx.graphics.getWidth());
		screenX = screenX * 400 / Gdx.graphics.getWidth();
		screenY = (Gdx.graphics.getHeight() - screenY) * 240 / Gdx.graphics.getHeight();
//		System.out.println("ADJUSTED: screenX: "+screenX +" screenY: "+screenY);
		
		
		// check bounds
		if(screenX < box.x || screenX > box.x + box.width 
				|| screenY < box.y || screenY > box.y + box.height)
			return false;
		// process input
		if(screenX < box.x + box.width / 2) {
			belt.setState(belt.getState() - 1);
		} else {
			belt.setState(belt.getState() + 1);
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
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
