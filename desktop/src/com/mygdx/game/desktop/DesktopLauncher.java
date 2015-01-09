package com.mygdx.game.desktop;

import org.chu.game.BeltMaster;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Beltmaster";
		config.width = 400;
		config.height = 240;
		new LwjglApplication(new BeltMaster(), config);
	}
}
