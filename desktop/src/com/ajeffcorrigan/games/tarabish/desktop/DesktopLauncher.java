package com.ajeffcorrigan.games.tarabish.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ajeffcorrigan.games.tarabish.tarabish;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Tarabish";
        config.width = 480;
        config.height = 320;
		new LwjglApplication(new tarabish(), config);
	}
}
