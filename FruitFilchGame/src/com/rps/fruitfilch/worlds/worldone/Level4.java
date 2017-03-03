package com.rps.fruitfilch.worlds.worldone;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.rps.fruitfilch.Assets.FILES;
import com.rps.fruitfilch.Fruit;
import com.rps.fruitfilch.FruitRageGame;
import com.rps.fruitfilch.Level;
import com.rps.fruitfilch.gameaddons.Annotation;
import com.rps.fruitfilch.gameutils.Coor;

public final class Level4 extends Level {
	
	private ArrayList<Coor> coorSetOne, coorSetTwo;
	private Annotation iBouncyBarAnno, stickyBarAnno;
	
	private class LocalRegions {
		private Texture levFourAnnoSheet = FruitRageGame.assets.getTexture(FILES.LEVEL_FOUR_ANNOTATIONS);
	}

	public Level4(FruitRageGame passedRef, String levelConfigString) {
		super(passedRef, levelConfigString, 4);
		coorSetOne = new ArrayList<Coor>();
		coorSetOne.add(new Coor(0, 0, 188, 85));
		coorSetOne.add(new Coor(0, 86, 188, 85));
		coorSetOne.add(new Coor(189, 0, 188, 85));
		
		coorSetTwo = new ArrayList<Coor>();
		coorSetTwo.add(new Coor(189, 86, 156, 84));
		coorSetTwo.add(new Coor(189, 171, 156, 84));
		coorSetTwo.add(new Coor(346, 86, 156, 84));
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
		iBouncyBarAnno = new Annotation(localRegions.levFourAnnoSheet, coorSetOne, 0.3f);
		stickyBarAnno = new Annotation(localRegions.levFourAnnoSheet, coorSetTwo, 0.3f);
		annotations.add(iBouncyBarAnno);
		annotations.add(stickyBarAnno);
		super.show();
		iBouncyBarAnno.setPosition(425, 200);
		stickyBarAnno.setPosition(690, 305);
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		super.touchDown(x, y, pointer, button);
		if(this.shotBlobTouched) {
			iBouncyBarAnno.removeWithAnimation(true);
			stickyBarAnno.removeWithAnimation(true);
		}
		return false;
	}
	

}
