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

public class WorldMenu implements Screen, InputProcessor {
	
	private FruitRageGame localGameRef;
	private Stage localStage;
	private InputMultiplexer multiplexer;
	
	private Texture backgroundTexture, worldMenuTexture;
	private TextureRegion stageBackgroundRegion, farmersHouseRegion, farmersHouseClickedRegion,
	leftRegion, rightRegion, moreComingSoonRegion, backButtonRG, backButtonClickedRG;
	
	private Image background;
	private ImageButton leftButton, rightButton, backButton;
	private PaneButton farmersHouseButton, moreComingSoonButton;
	
	private float clamp1, clamp2;
	private boolean inLeftTransition = false, inRightTransition = false;
	
	private class PaneButton extends ImageButton {
		private boolean isMoving = false;
		
		public PaneButton(TextureRegion region) {
			super(region);
		}
		
		public PaneButton(TextureRegion region, TextureRegion regionClicked) {
			super(region, regionClicked);
		}
		
	}
	
	public WorldMenu(FruitRageGame ref) {
		localGameRef = ref;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		localStage.act(Gdx.graphics.getDeltaTime());
		localStage.draw();
		
		if(inRightTransition) {
			if(moreComingSoonButton.x > clamp1) {
				if(moreComingSoonButton.x - clamp1 > 50) {
					moreComingSoonButton.x -= 50;
					farmersHouseButton.x -=50;
				} else { --moreComingSoonButton.x; --farmersHouseButton.x; }
			} else if(moreComingSoonButton.x == clamp1) { 
				inRightTransition = false; 
				Gdx.app.log("Right", ""); 
				localStage.removeActor(rightButton); 
				localStage.addActor(leftButton);
			}
		} 
		else if(inLeftTransition) {
			if(farmersHouseButton.x < clamp1) {
				if( (clamp1 - farmersHouseButton.x) > 50 || farmersHouseButton.x < 0) {
					farmersHouseButton.x += 50;
					moreComingSoonButton.x += 50;
				} else { ++farmersHouseButton.x; ++moreComingSoonButton.x; }
			} 
			else if(farmersHouseButton.x == clamp1) { 
				inLeftTransition = false; Gdx.app.log("Left", ""); 
				localStage.removeActor(leftButton);
				localStage.addActor(rightButton);
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		localStage = new Stage(FruitRageGame.getSTAGE_W(), FruitRageGame.getSTAGE_H(), false);		
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(localStage);
		multiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(multiplexer);
		backgroundTexture = FruitRageGame.assets.getTexture(FILES.MENU_BACKGROUND_1);
		worldMenuTexture = FruitRageGame.assets.getTexture(FILES.WORLD_MENU_SHEET);
		
		TextureRegion bgTmpTR = new TextureRegion(backgroundTexture, 0, 0, 1600, 1600);		
		stageBackgroundRegion = new TextureRegion(bgTmpTR, bgTmpTR.getRegionWidth() / 2 - FruitRageGame.getSTAGE_W() / 2,
				bgTmpTR.getRegionHeight() - FruitRageGame.getSTAGE_H(), FruitRageGame.getSTAGE_W(), FruitRageGame.getSTAGE_H());
		farmersHouseRegion = new TextureRegion(worldMenuTexture, 0, 0, 502, 331);
		farmersHouseClickedRegion = new TextureRegion(worldMenuTexture, 503, 0, 502, 331);
		leftRegion = new TextureRegion(worldMenuTexture, 503, 664, 83, 151);
		rightRegion = new TextureRegion(worldMenuTexture, 587, 664, 83, 151);
		moreComingSoonRegion = new TextureRegion(worldMenuTexture, 0, 332, 502, 331);
		backButtonRG = new TextureRegion(worldMenuTexture, 503, 816, 72, 68);
		backButtonClickedRG = new TextureRegion(worldMenuTexture, 671, 664, 72, 68);
		
		backButton = new ImageButton(backButtonRG, backButtonClickedRG) {
			@Override
			public void touchUp(float x, float y, int pointer) {
				super.touchUp(x, y, pointer);
				localGameRef.buttonClick.play();
				localGameRef.setScreen(localGameRef.mainMenu);
			}
		};
		backButton.x = 30; backButton.y = localStage.height() - backButton.height - 30;
		
		background = new Image(stageBackgroundRegion);
		final WorldMenu internalRef = this;
		farmersHouseButton = new PaneButton(farmersHouseRegion, farmersHouseClickedRegion) {						
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				super.touchDown(x, y, pointer);
				return true;
			}
			@Override
			public void touchUp(float x, float y, int pointer) {
				super.touchUp(x, y, pointer);
				localGameRef.buttonClick.play();
				internalRef.dispose();
				localGameRef.setScreen(localGameRef.levelMenuW1);
			}
		};
		leftButton = new ImageButton(leftRegion) {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				if(!inLeftTransition && !inRightTransition) {
					super.touchDown(x, y, pointer);
					return true;
				}
				return false;
			}
			
			@Override
			public void touchUp(float x, float y, int pointer) {
				super.touchUp(x, y, pointer);
				localGameRef.menuSlideSound.play();
				inLeftTransition = true;
			}
		};
		rightButton = new ImageButton(rightRegion) {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				if(!inLeftTransition && !inRightTransition) {
					super.touchDown(x, y, pointer);
					return true;
				}
				return false;
			}
			
			@Override
			public void touchUp(float x, float y, int pointer) {
				super.touchUp(x, y, pointer);
				localGameRef.menuSlideSound.play();
				inRightTransition = true;
			}
		};
		moreComingSoonButton = new PaneButton(moreComingSoonRegion);
		
		leftButton.x = 50;
		leftButton.y = FruitRageGame.getSTAGE_H() / 2 - leftButton.height / 2;
		rightButton.x = FruitRageGame.getSTAGE_W() - rightButton.width - leftButton.x;
		rightButton.y = leftButton.y;
		clamp1 = farmersHouseButton.x = FruitRageGame.getSTAGE_W() / 2 - farmersHouseButton.width / 2;
		farmersHouseButton.y = FruitRageGame.getSTAGE_H() / 2 - farmersHouseButton.height / 2;
		clamp2 = moreComingSoonButton.x = FruitRageGame.getSTAGE_W() + farmersHouseButton.x;
		moreComingSoonButton.y = farmersHouseButton.y;
		
		
		localStage.addActor(background);
		localStage.addActor(farmersHouseButton);
		localStage.addActor(moreComingSoonButton);
		localStage.addActor(rightButton);		
		localStage.addActor(backButton);
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
		
	}

	@Override
	public void dispose() {
		localStage.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.BACK) {
			localGameRef.buttonClick.play();
			dispose();
			localGameRef.setScreen(localGameRef.mainMenu);
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
