package com.rexyrex.gomoku.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rexyrex.gomoku.Gomoku;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = Gomoku.WIDTH/2;
		config.height = Gomoku.HEIGHT/2;
		config.title = Gomoku.TITLE;
		
		new LwjglApplication(new Gomoku(), config);
	}
}
