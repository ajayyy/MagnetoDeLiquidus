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
		
//		System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");
		
		new LwjglApplication(new Main(), config);
	}
}
