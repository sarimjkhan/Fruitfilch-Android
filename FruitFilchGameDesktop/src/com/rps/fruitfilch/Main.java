package com.rps.fruitfilch;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rps.fruitfilch.FruitRageGame;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "FruitRage";
		cfg.useGL20 = false;
		cfg.width = 854; //1024 854 1280
		cfg.height = 480; //600 480 720
		
		new LwjglApplication(new FruitRageGame(), cfg);
	}
}
