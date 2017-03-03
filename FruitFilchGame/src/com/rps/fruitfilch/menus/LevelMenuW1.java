package com.rps.fruitfilch.menus;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.rps.fruitfilch.FruitRageGame;
import com.rps.fruitfilch.Assets.FILES;
import com.rps.fruitfilch.worlds.worldone.WorldOne;

public class LevelMenuW1 implements Screen, InputProcessor {
	
	private static FruitRageGame localGameRef;
	private Stage localStage;
	private InputMultiplexer multiplexer;
	
	private Texture levelMenuTexture, backgroundTexture;
	private TextureRegion stageBgRG, backButtonRG, backButtonClickedRG, levelButtonRG, levelButtonLockedRG, starRG;
	private Image background;
	private ImageButton backButton;
	private ArrayList<LevelButton> levelButtons;
	Iterator<LevelButton> itr;
	
	public class LevelButton extends ImageButton {
		private int levelNum;
		private int score;
		private boolean isLevelClearedForTouchDown = false;
		private String levelString = "LEVEL";
		BitmapFont scoreFnt, levelNumFnt;
		
		public LevelButton(TextureRegion region, float x, float y, int levelNum, int score, int stars) {
			super(region);
			//Gdx.app.log(String.valueOf(score), String.valueOf(stars));
			this.levelNum = levelNum;
			this.score = score;
			this.x = x; this.y = y;
			this.levelString = this.levelString + levelNum;
			scoreFnt = new BitmapFont(FruitRageGame.assets.getBitmapFontData(FILES.LEVEL_MENU_SCORE_FONT), new TextureRegion(FruitRageGame.assets.getTexture(FILES.LEVEL_MENU_SCORE_SHEET)), false);
			levelNumFnt = new BitmapFont(FruitRageGame.assets.getBitmapFontData(FILES.LEVEL_NUMBER_FONT), new TextureRegion(FruitRageGame.assets.getTexture(FILES.LEVEL_NUMBER_FONT_SHEET)), false);
			localStage.addActor(this);
			if(stars > 0) {
				float posX = this.x + 28;
				float posY = this.y + 105;
				for(int i = 0; i < stars; ++i) {
					Image star = new Image(starRG);
					star.x = posX;
					star.y = posY;
					localStage.addActor(star);
					posX = posX + star.width + 1;
				}
			}
		}		
		/*public LevelButton createButton(TextureRegion region, float x, float y, int levelNum) {
			return new LevelButton(region, x, y, levelNum);
		}*/
		public void drawFonts() {
			//if(this.isLevelClearedForTouchDown) {
				localStage.getSpriteBatch().begin();
				if(score > 0)
					scoreFnt.draw(localStage.getSpriteBatch(), String.valueOf(score), this.x + 38, this.y + 20);
				/*if(score > 0)
					scoreFnt.draw(localStage.getSpriteBatch(), String.valueOf(score), this.x + 50, this.y + 20);*/
				
				if(levelNum == 1)
					levelNumFnt.draw(localStage.getSpriteBatch(), String.valueOf(levelNum), this.x + 51, this.y + 48);
				else if(levelNum < 10)
					levelNumFnt.draw(localStage.getSpriteBatch(), String.valueOf(levelNum), this.x + 48, this.y + 48);
				else
					levelNumFnt.draw(localStage.getSpriteBatch(), String.valueOf(levelNum), this.x + 45, this.y + 48);
				localStage.getSpriteBatch().end();
		//	}
		}		
		
		/*private LevelButton(TextureRegion region, TextureRegion clickedRegion) {
			super(region, clickedRegion);
		}*/
		
		/*public static LevelButton createButton(TextureRegion region, TextureRegion clickedRegion) {
			return new LevelButton(region, clickedRegion);
		}*/
		/*public void setPos(float x, float y) {
			this.x = x; this.y = y;
		}*/
		
		@Override
		public boolean touchDown(float x, float y, int pointer) {
			super.touchDown(x, y, pointer);			
			return this.isLevelClearedForTouchDown;
		}
		
		@Override
		public void touchUp(float x, float y, int pointer) {
			super.touchUp(x, y, pointer);
			localGameRef.buttonClick.play();
			localGameRef.menuMusic.stop();
			localGameRef.setScreen(FruitRageGame.worldOne.createLevel(WorldOne.WORLD_ONE.valueOf(levelString)));
		}
	}
	
	public LevelMenuW1(FruitRageGame ref) {
		localGameRef = ref;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		localStage.act(delta);
		localStage.draw();
		for(LevelButton button : levelButtons) {
			button.drawFonts();
		}		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		localStage = new Stage(FruitRageGame.getSTAGE_W(), FruitRageGame.getSTAGE_H(), false);
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(localStage);
		multiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(multiplexer);
		backgroundTexture = FruitRageGame.assets.getTexture(FILES.MENU_BACKGROUND_1);
		levelMenuTexture = FruitRageGame.assets.getTexture(FILES.LEVEL_MENU_W1_SHEET);
		
		TextureRegion bgTmpTR = new TextureRegion(backgroundTexture, 0, 0, 1600, 1600);
		stageBgRG = new TextureRegion(bgTmpTR, bgTmpTR.getRegionWidth() / 2 - FruitRageGame.getSTAGE_W() / 2,
				bgTmpTR.getRegionHeight() - FruitRageGame.getSTAGE_H(), FruitRageGame.getSTAGE_W(), FruitRageGame.getSTAGE_H());
		backButtonRG = new TextureRegion(levelMenuTexture, 0, 127, 72, 68);
		backButtonClickedRG = new TextureRegion(levelMenuTexture, 73, 127, 72, 68);
		levelButtonRG = new TextureRegion(levelMenuTexture, 0, 0, 107, 126);
		levelButtonLockedRG = new TextureRegion(levelMenuTexture, 108, 0, 107, 126);
		starRG = new TextureRegion(levelMenuTexture, 0, 196, 16, 15);
		
		background = new Image(stageBgRG);
		localStage.addActor(background);		
		
		backButton = new ImageButton(backButtonRG, backButtonClickedRG) {
			@Override
			public void touchUp(float x, float y, int pointer) {
				super.touchUp(x, y, pointer);
				localGameRef.buttonClick.play();
				dispose();
				localGameRef.setScreen(localGameRef.worldMenu);
			}
		};
		
		backButton.x = 30; backButton.y = localStage.height() - backButton.height - 30;
		
		float posX = FruitRageGame.getSTAGE_W() / 2 - levelButtonRG.getRegionWidth() / 2;
		float incX = levelButtonRG.getRegionWidth() + 20;
		float bckUp = posX -= 2 * incX;
		float posY = FruitRageGame.getSTAGE_H() / 2 - levelButtonRG.getRegionHeight() / 2;
		float incY = levelButtonRG.getRegionHeight() + 1;
		posY += incY;
		levelButtons = new ArrayList<LevelButton>();
		for(int i = 1; i < 16; ++i) {
			//Gdx.app.log("Level Menu", String.valueOf(FruitRageGame.sqlHelperLibgdx.isLevelCleared(i)));
			if(FruitRageGame.sqlHelperLibgdx != null) {
				if(FruitRageGame.sqlHelperLibgdx.isLevelCleared(i) || i == 1 || (i > 1 && FruitRageGame.sqlHelperLibgdx.isLevelCleared(i - 1))) {
					//Gdx.app.log("Level Menu", "Level is cleared");
					levelButtons.add(new LevelButton(levelButtonRG, posX, posY, i, FruitRageGame.sqlHelperLibgdx.getScore(i), FruitRageGame.sqlHelperLibgdx.getStars(i)));
					levelButtons.get(i - 1).isLevelClearedForTouchDown = true;
				} else levelButtons.add(new LevelButton(levelButtonLockedRG, posX, posY, i, 0, 0));
			}
			else {
				levelButtons.add(new LevelButton(levelButtonRG, posX, posY, i, 0, 0));
				levelButtons.get(i - 1).isLevelClearedForTouchDown = true;
			}
			posX = (i % 5 == 0) ? bckUp : posX + incX;
			posY = (i % 5 == 0) ? posY - incY : posY;
		}
		localStage.addActor(backButton);
		
		if(!localGameRef.menuMusic.isPlaying()) localGameRef.menuMusic.play();
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

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.BACK) {
			localGameRef.buttonClick.play();
			dispose();
			localGameRef.setScreen(localGameRef.worldMenu);
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
