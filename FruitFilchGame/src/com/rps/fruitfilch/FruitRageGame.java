package com.rps.fruitfilch;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.rps.fruitfilch.menus.AboutMenu;
import com.rps.fruitfilch.menus.LevelMenuW1;
import com.rps.fruitfilch.menus.MainMenu;
import com.rps.fruitfilch.menus.SplashMenu;
import com.rps.fruitfilch.menus.WorldMenu;
import com.rps.fruitfilch.worlds.worldone.WorldOne;

public class FruitRageGame extends Game {
	
	private static int STAGE_W;
	private static int STAGE_H;
	
	private static int SCREEN_W;
	private static int SCREEN_H;
	
	public static float SCREEN_TO_STAGE_RATIO;
	public static Assets assets;
	public static SQLHelperLibgdx sqlHelperLibgdx = null;
	public static WorldOne worldOne;
	
	public static SplashMenu splashMenu;
	public static MainMenu mainMenu;
	public static WorldMenu worldMenu;
	public static LevelMenuW1 levelMenuW1;
	public static AboutMenu aboutMenu;
	
	public Sound buttonClick;
	public Sound menuSlideSound;
	public Music menuMusic;

	public FruitRageGame() {
		
	}
	
	public FruitRageGame(SQLHelperLibgdx helper) {
		sqlHelperLibgdx = helper;
	}
	
	@Override
	public void create() {	
		if(sqlHelperLibgdx != null)
			sqlHelperLibgdx.openDatabase();
		STAGE_W = SCREEN_W = Gdx.graphics.getWidth();
		STAGE_H = SCREEN_H = Gdx.graphics.getHeight();
		assets = new Assets();
		assets.loadAssets();
		splashMenu = new SplashMenu(this);
		mainMenu = new MainMenu(this);
		worldMenu = new WorldMenu(this);
		levelMenuW1 = new LevelMenuW1(this);
		aboutMenu = new AboutMenu(this);
		Gdx.input.setCatchBackKey(true);
		Gdx.app.setLogLevel(Application.LOG_NONE);
		this.setScreen(splashMenu);
		//SCREEN_TO_STAGE_RATIO = (SCREEN_W/SCREEN_H) / (STAGE_W/STAGE_H);
		
		/*if(assets.getAssetManagerStatus()) {
			worldOne = new WorldOne();
			Level2 level2 = worldOne.createLevel(WorldOne.WORLD_ONE.LEVEL2, this);
			this.setScreen(level2);			
		}*/
		
	}
	
	@Override
	public void dispose() {
		if(sqlHelperLibgdx != null)
			sqlHelperLibgdx.closeDatabase();
	}

	public static int getSTAGE_W() {
		return STAGE_W;
	}

	/*public static void setSTAGE_W(int sTAGE_W) {
		STAGE_W = sTAGE_W;
	}*/

	public static int getSTAGE_H() {
		return STAGE_H;
	}

	/*public static void setSTAGE_H(int sTAGE_H) {
		STAGE_H = sTAGE_H;
	}*/

	/*public static int getSCREEN_W() {
		return SCREEN_W;
	}*/

	/*public static int setSCREEN_W(int sCREEN_W) {
		SCREEN_W = sCREEN_W;
		return sCREEN_W;
	}*/

	/*public static int getSCREEN_H() {
		return SCREEN_H;
	}*/

	/*public static int setSCREEN_H(int sCREEN_H) {
		SCREEN_H = sCREEN_H;
		return sCREEN_H;
	}*/
	
}
