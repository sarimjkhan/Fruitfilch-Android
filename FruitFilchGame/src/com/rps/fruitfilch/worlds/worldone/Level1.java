package com.rps.fruitfilch.worlds.worldone;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.rps.fruitfilch.Assets.FILES;
import com.rps.fruitfilch.Fruit;
import com.rps.fruitfilch.FruitRageGame;
import com.rps.fruitfilch.Level;
import com.rps.fruitfilch.gameaddons.Annotation;
import com.rps.fruitfilch.gameutils.Coor;

public class Level1 extends Level {
	
	private ArrayList<Coor> coorSetOne, coorSetTwo, coorSetThree;
	private Annotation fingerAnno, barAnno, skipAnno;
	private boolean skipAnnoOnStage = false;
	private boolean fingerAnnoOnStage = false;
	private final String SKIP_ANNOTATION = "skipAnnotation";
	
	private class LocalRegions {
		private Texture levOneAnnoSheet = FruitRageGame.assets.getTexture(FILES.LEVEL_ONE_ANNOTATIONS);
	}
	
	public Level1(FruitRageGame passedRef, String levelConfigString) {
		super(passedRef, levelConfigString, 1);
		coorSetOne = new ArrayList<Coor>();
		coorSetOne.add(new Coor(312, 79, 135, 121));
		coorSetOne.add(new Coor(156, 158, 135, 121));
		coorSetOne.add(new Coor(136, 280, 135, 121));
		coorSetOne.add(new Coor(292, 201, 135, 121));
		coorSetOne.add(new Coor(468, 0, 135, 121));
		coorSetOne.add(new Coor(448, 122, 135, 121));
		coorSetOne.add(new Coor(604, 0, 135, 121));
		coorSetOne.add(new Coor(428, 244, 135, 121));
		coorSetOne.add(new Coor(584, 122, 135, 121));
		coorSetOne.add(new Coor(0, 237, 135, 121));
		
		coorSetTwo = new ArrayList<Coor>();
		coorSetTwo.add(new Coor(0, 0, 155, 78));
		coorSetTwo.add(new Coor(0, 79, 155, 78));
		coorSetTwo.add(new Coor(0, 158, 155, 78));
		
		coorSetThree = new ArrayList<Coor>();
		coorSetThree.add(new Coor(156, 0, 155, 78));
		coorSetThree.add(new Coor(156, 79, 155, 78));
		coorSetThree.add(new Coor(312, 0, 155, 78));
		
		
	}
	
	@Override
	protected void populateFruitArray() {
		float posX = blobX + shotBlob.width / 2;
		float posY = blobY + shotBlob.height / 2;
		fruitArr.add(new Fruit(tRegions.orange, tRegions.orangeDisAnimSheet, 15, FRUIT_NAME.ORANGE, posX, posY, RATIO, world));
		fruitArr.add(new Fruit(tRegions.apple, tRegions.appleDisAnimSheet, 15, FRUIT_NAME.APPLE, posX, posY, RATIO, world));
		fruitArr.add(new Fruit(tRegions.orange, tRegions.orangeDisAnimSheet, 15, FRUIT_NAME.ORANGE, posX, posY, RATIO, world));
		
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		if(currentFruit != null) {
			if((currentFruit.getBodyStatus().getLinearVelocity().x == 0 ||
					currentFruit.getBodyStatus().getLinearVelocity().y == 0) && !skipAnnoOnStage) {
					skipAnno.addToStage(localStage);
					skipAnnoOnStage = true;
			}
		} else if(this.skipAnnoTouched && skipAnnoOnStage) {
				skipAnno.removeWithAnimation(true);
				this.skipAnnoTouched = false;
				skipAnnoOnStage = false;
		} else if(currentFruit == null && skipAnnoOnStage) {
			skipAnno.removeWithAnimation(true);
			this.skipAnnoTouched = false;
			skipAnnoOnStage = false;
		}
	}
	
	@Override
	public void show() {
		LocalRegions localRegions = new LocalRegions();
		fingerAnno = new Annotation(localRegions.levOneAnnoSheet, coorSetOne);
		barAnno = new Annotation(localRegions.levOneAnnoSheet, coorSetTwo, 0.3f);
		skipAnno = new Annotation(localRegions.levOneAnnoSheet, coorSetThree, 0.3f, SKIP_ANNOTATION);
		annotations.add(fingerAnno);
		annotations.add(barAnno);
		fingerAnnoOnStage = true;
		super.show();		
		fingerAnno.setPosition(this.blobStageOriginX - (fingerAnno.width - 12), this.blobStageOriginY - (fingerAnno.height - 5));
		barAnno.setPosition(390, 140);		
		skipAnno.setPosition(topBar.skipButton.x + 20, topBar.skipButton.y - 70);
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		super.touchDown(x, y, pointer, button);
		if(this.shotBlobTouched) {
			if(fingerAnnoOnStage) {
				fingerAnno.removeWithAnimation(true);
				fingerAnnoOnStage = false;
			}
			barAnno.removeWithAnimation(true);
		}
		return false;
	}
	
}
