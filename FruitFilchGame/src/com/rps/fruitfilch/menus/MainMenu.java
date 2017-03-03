package com.rps.fruitfilch.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.rps.fruitfilch.Assets.FILES;
import com.rps.fruitfilch.FruitRageGame;

public class MainMenu implements Screen, InputProcessor {
	
	private FruitRageGame localGameRef;
	
	private Stage localStage;
	private Texture mainMenuSheet, exitBoxSheet;
	private TextureRegion backgroundRegion;
	private TextureRegion stageBackgroundRegion, logoRegion, bigCloudRegion, smallCloudRegion, playButtonRegion,
	playButtonClickedRegion, aboutButtonRegion, aboutButtonClickedRegion, soundButtonRegion, soundButtonClickedRegion;
	
	private Image background, logo, bigCloud, smallCloud;		
	private ImageButton playButton, soundButton, aboutButton;
	private InputMultiplexer multiplexer;
	private ExitBox exitBox;
	
	private class ExitBox {
		private Image exitBox;
		private ImageButton exitConfirm, exitCancel;
		private boolean onStage = false;
		
		public ExitBox() {
			exitBox = new Image(new TextureRegion(exitBoxSheet, 0, 0, 583, 203));
			exitBox.x = FruitRageGame.getSTAGE_W() / 2 - exitBox.width / 2;
			exitBox.y = FruitRageGame.getSTAGE_H() / 2 - exitBox.height / 2;
			exitConfirm = new ImageButton(new TextureRegion(exitBoxSheet, 0, 282, 82, 77),
					new TextureRegion(exitBoxSheet, 83, 272, 70, 67)) {
				@Override
				public boolean  touchDown(float x, float y, int pointer) {
					super.touchDown(x, y, pointer);
					return true;
				}
				@Override
				public void touchUp(float x, float y, int pointer) {
					//super.touchUp(x, y, pointer);
					localGameRef.buttonClick.play();
					Gdx.app.exit();
				}
			};
			exitConfirm.x = (exitBox.x + exitBox.width / 2) - (exitConfirm.width + 40);
			exitConfirm.y = exitBox.y + 20;
			exitCancel = new ImageButton(new TextureRegion(exitBoxSheet, 0, 204, 82, 77),
					new TextureRegion(exitBoxSheet, 83, 204, 70, 67)) {
				@Override
				public boolean touchDown(float x, float y, int pointer) {
					super.touchDown(x, y, pointer);
					return true;
				}
				@Override
				public void touchUp(float x, float y, int pointer) {
					super.touchUp(x, y, pointer);
					localGameRef.buttonClick.play();
					removeFromStage();
				}
			};
			exitCancel.x = (exitBox.x + exitBox.width / 2) + 40;
			exitCancel.y = exitBox.y + 20;
		}
		
		public void addToStage() {
			localStage.addActor(exitBox);
			localStage.addActor(exitConfirm);
			localStage.addActor(exitCancel);
			onStage = true;
		}
		
		public void removeFromStage() {
			localStage.removeActor(exitCancel);
			localStage.removeActor(exitConfirm);
			localStage.removeActor(exitBox);
			onStage = false;
		}
	}
	
	public MainMenu(FruitRageGame ref) {
		localGameRef = ref;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		localStage.draw();		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		localStage = new Stage(FruitRageGame.getSTAGE_W(), FruitRageGame.getSTAGE_H(), false);
		mainMenuSheet = FruitRageGame.assets.getTexture(FILES.MAIN_MENU_SHEET);
		exitBoxSheet = FruitRageGame.assets.getTexture(FILES.EXIT_BOX);
		exitBox = new ExitBox();
		Texture bg = FruitRageGame.assets.getTexture(FILES.MENU_BACKGROUND_1);		
		backgroundRegion = new TextureRegion(bg, 0, 0, 1600, 1600);
		
		stageBackgroundRegion = new TextureRegion(backgroundRegion, backgroundRegion.getRegionWidth() / 2 - FruitRageGame.getSTAGE_W() / 2, 
				backgroundRegion.getRegionHeight() - FruitRageGame.getSTAGE_H(), FruitRageGame.getSTAGE_W(),  FruitRageGame.getSTAGE_H());
		logoRegion = new TextureRegion(mainMenuSheet, 0, 481, 438, 121);
		bigCloudRegion = new TextureRegion(mainMenuSheet, 0, 603, 234, 99);
		smallCloudRegion = new TextureRegion(mainMenuSheet, 855, 0, 146, 101);
		playButtonRegion = new TextureRegion(mainMenuSheet, 235, 603, 167, 216);
		playButtonClickedRegion = new TextureRegion(mainMenuSheet, 0, 703, 167, 216);
		aboutButtonRegion = new TextureRegion(mainMenuSheet, 439, 481, 56, 53);
		aboutButtonClickedRegion = new TextureRegion(mainMenuSheet, 168, 703, 56, 53);
		soundButtonRegion = new TextureRegion(mainMenuSheet, 168, 757, 55, 51);
		soundButtonClickedRegion = new TextureRegion(mainMenuSheet, 0, 920, 55, 51);
		
		background = new Image(stageBackgroundRegion);
		logo = new Image(logoRegion);
		bigCloud = new Image(bigCloudRegion);
		smallCloud = new Image(smallCloudRegion);
		
		playButton = new ImageButton(playButtonRegion, playButtonClickedRegion) {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				if(!exitBox.onStage) {
						super.touchDown(x, y, pointer);
						return true;
				}
				return false;
			}
			@Override
			public void touchUp(float x, float y, int pointer) {
				super.touchUp(x, y, pointer);
				localGameRef.buttonClick.play();
				setWorldMenuScreen();
			}
		};
		soundButton = new ImageButton(soundButtonRegion, soundButtonClickedRegion) {};
		aboutButton = new ImageButton(aboutButtonRegion, aboutButtonClickedRegion) {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				if(!exitBox.onStage) {
					super.touchDown(x, y, pointer);
					return true;
				}
				return false;
			}
			
			@Override
			public void touchUp(float x, float y, int pointer) {
				super.touchUp(x, y, pointer);
				localGameRef.buttonClick.play();
				dispose();
				localGameRef.setScreen(localGameRef.aboutMenu);
			}
		};
		
		logo.x = FruitRageGame.getSTAGE_W() / 2 - logo.width / 2;
		logo.y = FruitRageGame.getSTAGE_H() - (logo.height + 30);
		smallCloud.x = logo.x - smallCloud.width - smallCloud.width / 6;
		smallCloud.y = logo.y - smallCloud.height / 2;
		bigCloud.x = logo.x + logo.width - logo.width / 16;
		bigCloud.y = logo.y - logo.height / 2 - logo.height / 4;
		playButton.x = FruitRageGame.getSTAGE_W()/2 - playButton.width / 2;
		playButton.y = (FruitRageGame.getSTAGE_H() / 2 - playButton.height / 2) - 50;
		aboutButton.x = 50;
		aboutButton.y = playButton.y + 120;
		soundButton.x = 50;
		soundButton.y = aboutButton.y - (soundButton.height + 10);		
		
		localStage.addActor(background);
		localStage.addActor(logo);
		localStage.addActor(smallCloud);
		localStage.addActor(bigCloud);
		localStage.addActor(playButton);
		localStage.addActor(aboutButton);
		//localStage.addActor(soundButton);
		
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(localStage);
		multiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(multiplexer);
		/*localGameRef.menuMusic.setLooping(true);
		localGameRef.menuMusic.play();*/
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
	
	private void setWorldMenuScreen() {
		this.dispose();
		localGameRef.setScreen(localGameRef.worldMenu);
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.BACK && !exitBox.onStage) {
			localGameRef.buttonClick.play();
			exitBox.addToStage();
		} else if(keycode == Keys.BACK && exitBox.onStage) {
			localGameRef.buttonClick.play();
			exitBox.removeFromStage();
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
