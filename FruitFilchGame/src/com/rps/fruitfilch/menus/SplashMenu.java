package com.rps.fruitfilch.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.rps.fruitfilch.FruitRageGame;
import com.rps.fruitfilch.Assets.FILES;
import com.rps.fruitfilch.worlds.worldone.WorldOne;

public class SplashMenu implements Screen {
	
	private Texture splashScreenTexture;
	private TextureRegion splashBGRegion;
	private Image splashBG;
	
	private Stage localStage;
	private FruitRageGame localGameRef;
	
	public SplashMenu(FruitRageGame ref) {
		localGameRef = ref;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		localStage.draw();
		if(FruitRageGame.assets.getAssetManagerStatus()) {
			FruitRageGame.worldOne = new WorldOne(localGameRef);
			this.dispose();
			localGameRef.buttonClick = localGameRef.assets.getSound(FILES.BUTTON_CLICK_S);			
			localGameRef.menuMusic = localGameRef.assets.getMusic(FILES.MENUS_MUSIC_M);
			localGameRef.menuSlideSound = localGameRef.assets.getSound(FILES.MENU_SLIDE_S);
			localGameRef.menuMusic.setLooping(true);
			localGameRef.menuMusic.play();
			localGameRef.setScreen(localGameRef.mainMenu);
		}
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		localStage = new Stage(FruitRageGame.getSTAGE_W(), FruitRageGame.getSTAGE_H(), false);
		splashScreenTexture = new Texture("data/images/LoadingScreen.png");
		splashBGRegion = new TextureRegion(splashScreenTexture, 0, 0, 1600, 1600);
		TextureRegion stageBGTR = new TextureRegion(splashBGRegion, 0, 0, FruitRageGame.getSTAGE_W(), FruitRageGame.getSTAGE_H());
		TextureRegion loadingTR = new TextureRegion(splashScreenTexture, 0, 1601, 452, 238);
		splashBG = new Image(stageBGTR);
		Image loading = new Image(loadingTR);
		loading.x = localStage.width() / 2  - loading.width / 2;
		loading.y = localStage.height() / 2 - loading.height / 2;
		localStage.addActor(splashBG);
		localStage.addActor(loading);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		localStage.dispose();
	}

}
