package org.chu.game.ui;

import org.chu.game.BeltMaster;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class LevelSelectButton extends Button {
	private static Drawable play;
	
	public LevelSelectButton() {
		super(play, play);
	}
	
	public static void setupAnimations(BeltMaster game) {
		play = new Image(game.getTexture("level-select-button")).getDrawable();
	}

}
