package com.rps.fruitfilch.worlds.worldone;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.rps.fruitfilch.Assets.FILES;
import com.rps.fruitfilch.Fruit;
import com.rps.fruitfilch.FruitRageGame;
import com.rps.fruitfilch.Level;
import com.rps.fruitfilch.gameaddons.Annotation;
import com.rps.fruitfilch.gameutils.Coor;

public final class Level7 extends Level {
	
	private ArrayList<Coor> coorSetOne;
	private Annotation obstacleAnno;

	private class LocalRegions {
		private Texture levSevenAnnoSheet = FruitRageGame.assets.getTexture(FILES.LEVEL_SEVEN_ANNOTATIONS);
	}
	
	public Level7(FruitRageGame passedRef, String levelConfigString) {
		super(passedRef, levelConfigString, 7);
		coorSetOne = new ArrayList<Coor>();
		coorSetOne.add(new Coor(0, 154, 159, 76));
		coorSetOne.add(new Coor(0, 0, 159, 76));
		coorSetOne.add(new Coor(0, 77, 159, 76));
	}

	@Override
	protected void populateFruitArray() {
		float posX = blobX + shotBlob.width / 2;
		float posY = blobY + shotBlob.height / 2;		
		fruitArr.add(new Fruit(tRegions.strawberry, tRegions.appleDisAnimSheet,  15, FRUIT_NAME.STRAWBERRY, posX, posY, RATIO, world));
		fruitArr.add(new Fruit(tRegions.apple, tRegions.appleDisAnimSheet, 15, FRUIT_NAME.APPLE, posX, posY, RATIO, world));
		fruitArr.add(new Fruit(tRegions.orange, tRegions.orangeDisAnimSheet, 15, FRUIT_NAME.ORANGE, posX, posY, RATIO, world));
	}
	
	@Override
	public void show() {
		LocalRegions localRegions = new LocalRegions();
		obstacleAnno = new Annotation(localRegions.levSevenAnnoSheet,coorSetOne, 0.3f);
		annotations.add(obstacleAnno);
		super.show();
		obstacleAnno.setPosition(350, 250);
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		super.touchDown(x, y, pointer, button);
		if(this.shotBlobTouched) {
			obstacleAnno.removeWithAnimation(true);
		}
		return false;
	}
	

}
