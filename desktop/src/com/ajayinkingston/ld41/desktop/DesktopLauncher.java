package com.ajayinkingston.ld41.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ajayinkingston.ld41.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = 1000;
		config.height = 600;
		
		config.foregroundFPS = 60;
		
		config.samples = 4;

		
		new LwjglApplication(new Main(), config);
	}
}
