package com.rps.fruitfilch.worlds.worldone;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.rps.fruitfilch.Assets.FILES;
import com.rps.fruitfilch.Fruit;
import com.rps.fruitfilch.FruitRageGame;
import com.rps.fruitfilch.Level;
import com.rps.fruitfilch.gameaddons.Annotation;
import com.rps.fruitfilch.gameutils.Coor;

public final class Level5 extends Level {

	private ArrayList<Coor> coorSetOne;
	private Annotation spikyBarAnno;
	
	private class LocalRegions {
		private Texture levelFiveAnnoSheet = FruitRageGame.assets.getTexture(FILES.LEVEL_FIVE_ANNOTATIONS);
	}
	
	public Level5(FruitRageGame passedRef, String levelConfigString) {
		super(passedRef, levelConfigString, 5);
		coorSetOne = new ArrayList<Coor>();
		coorSetOne.add(new Coor(0, 0, 151, 47));
		coorSetOne.add(new Coor(0, 48, 151, 47));
		coorSetOne.add(new Coor(0, 96, 151, 47));
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
		spikyBarAnno = new Annotation(localRegions.levelFiveAnnoSheet, coorSetOne, 0.3f);
		annotations.add(spikyBarAnno);
		super.show();
		spikyBarAnno.setPosition(600, 255);
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		super.touchDown(x, y, pointer, button);
		if(this.shotBlobTouched) {
			spikyBarAnno.removeWithAnimation(true);
		}
		return false;
	}
	

}
