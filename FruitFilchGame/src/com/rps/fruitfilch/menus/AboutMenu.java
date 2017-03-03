package com.rps.fruitfilch.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.FlickScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.rps.fruitfilch.Assets.FILES;
import com.rps.fruitfilch.FruitRageGame;

public class AboutMenu implements Screen, InputProcessor {
	
	private FruitRageGame localGameRef;
	private Stage localStage;
	private ImageButton backButton;
	private FlickScrollPane scroll;
	private Table container;
	private Table table;
	
	private Texture aboutT;
	private TextureRegion aboutTR;
	private Image about, background;
	
	InputMultiplexer multiplexer;
	
	public AboutMenu(FruitRageGame ref) {
		localGameRef = ref;
	}
	
	float yScroll;
	
	
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		localStage.act(delta);
		localStage.draw();
		/*if(yScroll < (scroll.getMaxY()) && !scroll.isFlinging() && !scroll.isPanning())
			scroll.setScrollY(++yScroll);
		else if(!scroll.isFlinging() && !scroll.isPanning() && (yScroll > (scroll.getMaxY()) || yScroll == scroll.getMaxY())) yScroll = - localStage.height();
		//if(!scroll.isFlinging()) scroll.setScrollY(yScroll);
		else if(scroll.isFlinging() || scroll.isPanning()) {
			yScroll = scroll.getScrollY();
		}*/
		
		//if(yScroll < ())
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
		Texture tmpT = FruitRageGame.assets.getTexture(FILES.WORLD_MENU_SHEET);
		TextureRegion bgTmpTR = new TextureRegion(FruitRageGame.assets.getTexture(FILES.MENU_BACKGROUND_1), 0, 0, 1600, 1600);
		TextureRegion stageBackgroundRegion = new TextureRegion(bgTmpTR, bgTmpTR.getRegionWidth() / 2 - FruitRageGame.getSTAGE_W() / 2,
				bgTmpTR.getRegionHeight() - FruitRageGame.getSTAGE_H(), FruitRageGame.getSTAGE_W(), FruitRageGame.getSTAGE_H());
		
		background = new Image(stageBackgroundRegion);
		localStage.addActor(background);
		
		container = new Table();
		container.width = FruitRageGame.getSTAGE_W();
		container.height = FruitRageGame.getSTAGE_H();
		localStage.addActor(container);
		
		aboutT = FruitRageGame.assets.getTexture(FILES.ABOUT_MENU_SHEET);
		aboutTR = new TextureRegion(aboutT, 0, 0, 400, 1300);
		about = new Image(aboutTR);
		
		table = new Table();
		scroll = new FlickScrollPane(table);
		//scroll.setOverscroll(true);
		container.add(scroll);
		
		table.parse("pad:1 * expand:x space:10");
		table.bottom();
		table.add(about);	
		
		TextureRegion buttonNorm = new TextureRegion(tmpT, 503, 816, 72, 68);
		TextureRegion buttonPressed = new TextureRegion(tmpT, 671, 664, 72, 68);
		backButton = new ImageButton(buttonNorm, buttonPressed) {
			@Override
			public void touchUp(float x, float y, int pointer) {
				super.touchUp(x, y, pointer);
				localGameRef.buttonClick.play();
				dispose();
				localGameRef.setScreen(localGameRef.mainMenu);
			}
		};
		backButton.x = 30; backButton.y = localStage.height() - backButton.height - 30;
		localStage.addActor(backButton);
		//yScroll = - localStage.height();
		yScroll = 0;
		//scroll.setupOverscroll(localStage.height() + 3, 0, 0);
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
			this.dispose();
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
