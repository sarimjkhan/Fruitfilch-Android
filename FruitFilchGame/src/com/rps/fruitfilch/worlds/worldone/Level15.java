package com.rps.fruitfilch.worlds.worldone;

import com.rps.fruitfilch.Fruit;
import com.rps.fruitfilch.FruitRageGame;
import com.rps.fruitfilch.Level;

public final class Level15 extends Level {

	public Level15(FruitRageGame passedRef, String levelConfigString) {
		super(passedRef, levelConfigString, 15);
	}

	@Override
	protected void populateFruitArray() {
		float posX = blobX + shotBlob.width / 2;
		float posY = blobY + shotBlob.height / 2;		
		fruitArr.add(new Fruit(tRegions.strawberry, tRegions.appleDisAnimSheet,  15, FRUIT_NAME.STRAWBERRY, posX, posY, RATIO, world));
		fruitArr.add(new Fruit(tRegions.apple, tRegions.appleDisAnimSheet, 15, FRUIT_NAME.APPLE, posX, posY, RATIO, world));
		fruitArr.add(new Fruit(tRegions.orange, tRegions.orangeDisAnimSheet, 15, FRUIT_NAME.ORANGE, posX, posY, RATIO, world));
	}
	

}
