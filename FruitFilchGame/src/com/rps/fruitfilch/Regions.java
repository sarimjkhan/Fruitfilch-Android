package com.rps.fruitfilch;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.rps.fruitfilch.Assets.FILES;

public class Regions {
	
		private Texture backgroundSheetW1 = FruitRageGame.assets.getTexture(FILES.BACKGROUND_SHEET);
		private Texture gameSheet = FruitRageGame.assets.getTexture(FILES.GAME_SHEET);
		private Texture magicPotBefSheet = FruitRageGame.assets.getTexture(FILES.MAGIC_POT_BEF_SHEET);
		private Texture magicPotAftSheet = FruitRageGame.assets.getTexture(FILES.MAGIC_POT_AFT_SHEET);
		private Texture characterSheet = FruitRageGame.assets.getTexture(FILES.CHARACTER_SHEET);
		private Texture obstacleSheet = FruitRageGame.assets.getTexture(FILES.OBSTACLE_SHEET);
		public Texture orangeDisAnimSheet = FruitRageGame.assets.getTexture(FILES.ORANGE_DIS_SHEET);
		public Texture appleDisAnimSheet = FruitRageGame.assets.getTexture(FILES.APPLE_DIS_SHEET);
		public Texture starDisAnimSheet = FruitRageGame.assets.getTexture(FILES.STAR_DIS_SHEET);
		public Texture starGlowSheet = FruitRageGame.assets.getTexture(FILES.STAR_GLOW_SHEET);
		public Texture trajectorySheet = FruitRageGame.assets.getTexture(FILES.TRAJECTORY_SHEET);
		public Texture topBarButtons = FruitRageGame.assets.getTexture(FILES.TOP_BAR_BUT_SHEET);
		public Texture pauseMenuSheet = FruitRageGame.assets.getTexture(FILES.PAUSE_MENU_SHEET);
		public Texture translucentSheet = FruitRageGame.assets.getTexture(FILES.TRANSLUCENT_SHEET);
		public Texture levelClearedSheet = FruitRageGame.assets.getTexture(FILES.LEVEL_CLEARED_SHEET);
		public Texture levelFailedSheet = FruitRageGame.assets.getTexture(FILES.LEVEL_FAILED_SHEET);
		
		public TextureRegion[] magicPotBefFrames = new TextureRegion[4];
		public TextureRegion[] magicPotAftFrames = new TextureRegion[14];
		{
			magicPotBefFrames[0] = new TextureRegion(magicPotBefSheet, 0, 0, 63, 48);
			magicPotBefFrames[1] = new TextureRegion(magicPotBefSheet, 64, 0, 63, 48);
			magicPotBefFrames[2] = new TextureRegion(magicPotBefSheet, 0, 49, 63, 48);
			magicPotBefFrames[3] = new TextureRegion(magicPotBefSheet, 64, 49, 63, 48);
			
			magicPotAftFrames[0] = new TextureRegion(magicPotAftSheet, 0, 0, 69, 48);
			magicPotAftFrames[1] = new TextureRegion(magicPotAftSheet, 0, 98, 69, 48);
			magicPotAftFrames[2] = new TextureRegion(magicPotAftSheet, 0, 147, 69, 48);
			magicPotAftFrames[3] = new TextureRegion(magicPotAftSheet, 70, 98, 69, 48);
			magicPotAftFrames[4] = new TextureRegion(magicPotAftSheet, 0, 196, 69, 48);
			magicPotAftFrames[5] = new TextureRegion(magicPotAftSheet, 70, 147, 69, 48);
			magicPotAftFrames[6] = new TextureRegion(magicPotAftSheet, 140, 98, 69, 48);
			magicPotAftFrames[7] = new TextureRegion(magicPotAftSheet, 70, 196, 69, 48);
			magicPotAftFrames[8] = new TextureRegion(magicPotAftSheet, 140, 147, 69, 48);
			magicPotAftFrames[9] = new TextureRegion(magicPotAftSheet, 0, 49, 69, 48);
			magicPotAftFrames[10] = new TextureRegion(magicPotAftSheet, 70, 0, 69, 48);
			magicPotAftFrames[11] = new TextureRegion(magicPotAftSheet, 70, 49, 69, 48);
			magicPotAftFrames[12] = new TextureRegion(magicPotAftSheet, 140, 0, 69, 48);
			magicPotAftFrames[13] = new TextureRegion(magicPotAftSheet, 140, 49, 69, 48);
		}		
		
		public TextureRegion backgroundW1 = new TextureRegion(backgroundSheetW1, 0, 0, 1600, 1600);
		public TextureRegion foregroundW1 = new TextureRegion(backgroundSheetW1, 0, 1601, 1600, 398);
		public TextureRegion bgSceneryW1 = new TextureRegion(backgroundSheetW1, 1601, 0, 1600, 398);
		public TextureRegion stageBackgroundW1, stageForegroundW1, stageSceneryW1;		
		
		public TextureRegion normalBar = new TextureRegion(gameSheet, 144, 481, 69, 10);
		public TextureRegion spikyBar = new TextureRegion(gameSheet, 0, 481, 72, 12);
		public TextureRegion stickyBar = new TextureRegion(gameSheet, 73, 481, 70, 14);
		public TextureRegion bouncyBar = new TextureRegion(gameSheet, 721, 0, 68, 15);
		public TextureRegion iBouncyBar = new TextureRegion(gameSheet, 214, 481, 68, 19);
		public TextureRegion orange = new TextureRegion(gameSheet, 721, 59, 31, 30);
		public TextureRegion strawberry = new TextureRegion(gameSheet, 753, 28, 31, 30);
		public TextureRegion apple = new TextureRegion(gameSheet, 721, 28, 31, 30);
		
		public TextureRegion thrower = new TextureRegion(characterSheet, 119, 53, 69, 197);
		public TextureRegion throwerNormHand = new TextureRegion(characterSheet, 119, 0, 70, 52);
		public TextureRegion throwerThrowHand = new TextureRegion(characterSheet, 0, 165, 70, 52);
		public TextureRegion catcher = new TextureRegion(characterSheet, 0, 0, 118, 164);
		public TextureRegion shotBlob = new TextureRegion(trajectorySheet, 0, 0, 24, 24);
		public TextureRegion trajectoryDot = new TextureRegion(trajectorySheet, 0, 25, 4, 4);
		public TextureRegion starRegion = new TextureRegion(starGlowSheet, 45, 0, 32, 30);
		public TextureRegion starGlowRegion = new TextureRegion(starGlowSheet,  0, 0, 44, 46);
		public TextureRegion boxObs = new TextureRegion(obstacleSheet, 0, 0, 125, 99);
		public TextureRegion stoneObs = new TextureRegion(obstacleSheet, 126, 0, 125, 55);
		public TextureRegion inGameFontSheet = new TextureRegion(FruitRageGame.assets.getTexture(FILES.IN_GAME_FONT_SHEET));
		
		public Regions() {
				stageBackgroundW1 = new TextureRegion(backgroundW1, backgroundW1.getRegionWidth() / 2 - FruitRageGame.getSTAGE_W() / 2, backgroundW1.getRegionHeight() - FruitRageGame.getSTAGE_H(), FruitRageGame.getSTAGE_W(), FruitRageGame.getSTAGE_H());
				stageForegroundW1 = new TextureRegion(foregroundW1, 0, 0, FruitRageGame.getSTAGE_W(), foregroundW1.getRegionHeight());
				stageSceneryW1 = new TextureRegion(bgSceneryW1, bgSceneryW1.getRegionHeight() / 2 - FruitRageGame.getSTAGE_W() / 2, bgSceneryW1.getRegionHeight() - FruitRageGame.getSTAGE_H(), FruitRageGame.getSTAGE_W(), FruitRageGame.getSTAGE_H());				
		}
}
