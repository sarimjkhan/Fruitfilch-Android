package com.rps.fruitfilch;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Assets {
	
	public static enum FILES {
		BACKGROUND_SHEET("data/images/Backgrounds.png"),
		GAME_SHEET("data/images/GameSheet.png"),
		MAGIC_POT_BEF_SHEET("data/images/MagicPotBefore.png"),
		MAGIC_POT_AFT_SHEET("data/images/MagicPotAfter.png"),
		CHARACTER_SHEET("data/images/Characters.png"),
		OBSTACLE_SHEET("data/images/ObstacleSheet.png"),
		ORANGE_DIS_SHEET("data/images/OrangeDisappearAnimation.png"),
		APPLE_DIS_SHEET("data/images/AppleDisappearAnimation.png"),
		STAR_DIS_SHEET("data/images/StarDisappearAnimation.png"),
		STAR_GLOW_SHEET("data/images/StarGlow.png"),
		TRAJECTORY_SHEET("data/images/TrajectorySheet.png"),
		TOP_BAR_BUT_SHEET("data/images/TopBarButtons.png"),
		IN_GAME_FONT_SHEET("data/Fonts/InGameFont.png"),
		IN_GAME_FONT("data/Fonts/InGameFont.fnt"),
		MAIN_MENU_SHEET("data/images/MainMenu.png"),
		PAUSE_MENU_SHEET("data/images/PauseMenu.png"),
		TRANSLUCENT_SHEET("data/images/Translucent.png"),
		LEVEL_CLEARED_SHEET("data/images/LevelCleared.png"),
		MENU_BACKGROUND_1("data/images/BackgroundSpriteSheet.png"),
		WORLD_MENU_SHEET("data/images/WorldMenu.png"),
		ABOUT_MENU_SHEET("data/images/AboutMenu.png"),
		LEVEL_MENU_W1_SHEET("data/images/LevelMenuW1.png"),
		LEVEL_MENU_SCORE_FONT("data/Fonts/levelMenuScoreFont.fnt"),
		LEVEL_MENU_SCORE_SHEET("data/Fonts/levelMenuScoreFont.png"),
		LEVEL_NUMBER_FONT("data/Fonts/levelNumberFont.fnt"),
		LEVEL_NUMBER_FONT_SHEET("data/Fonts/levelNumberFont.png"),
		LEVEL_FAILED_SHEET("data/images/LevelFailed.png"),
		LEVEL_ONE_ANNOTATIONS("data/images/LevelOneAnnotations.png"),
		LEVEL_THREE_ANNOTAIONS("data/images/Level3Annotation.png"),
		LEVEL_FOUR_ANNOTATIONS("data/images/Level4Annotations.png"),
		LEVEL_FIVE_ANNOTATIONS("data/images/Level5Annotations.png"),
		LEVEL_SEVEN_ANNOTATIONS("data/images/Level7Annotations.png"),
		EXIT_BOX("data/images/ExitBox.png"),
		BUTTON_CLICK_S("data/Sounds/ButtonClick.mp3"),
		LEVEL_BACKGROUND_M("data/Sounds/LevelBackgroundSound.mp3"),
		LEVEL_FAILED_S("data/Sounds/LevelFailedSound.mp3"),
		MENU_SLIDE_S("data/Sounds/MenuSlide.mp3"),
		MENUS_MUSIC_M("data/Sounds/MenusMusic.mp3"),
		F_INSANE_BOUNCE_S("data/Sounds/FruitSounds/FruitAtInsanelyBouncyBar.mp3"),
		F_MAGICPOT_S("data/Sounds/FruitSounds/FruitAtMagicPot.mp3"),
		F_SACK_S("data/Sounds/FruitSounds/FruitAtSack.mp3"),
		F_SLUGGY_BOUNCE_S("data/Sounds/FruitSounds/FruitAtSluggyBar.mp3"),
		F_SPIKY_BOUNCE_S("data/Sounds/FruitSounds/FruitAtSpikyBar.mp3"),
		F_STAR_BOUNCE_S("data/Sounds/FruitSounds/FruitAtStar.mp3"),
		F_THROWN_S("data/Sounds/FruitSounds/FruitThrown.mp3");
		
		final private String path;
		FILES(String path) {
			this.path = path;
		}
		
	}

	private AssetManager assetManager = new AssetManager();
	
	private void loadTextures() {
		assetManager.load(FILES.BACKGROUND_SHEET.path, Texture.class);
		assetManager.load(FILES.GAME_SHEET.path, Texture.class);
		assetManager.load(FILES.MAGIC_POT_BEF_SHEET.path, Texture.class);
		assetManager.load(FILES.MAGIC_POT_AFT_SHEET.path, Texture.class);
		assetManager.load(FILES.CHARACTER_SHEET.path, Texture.class);
		assetManager.load(FILES.OBSTACLE_SHEET.path, Texture.class);
		assetManager.load(FILES.ORANGE_DIS_SHEET.path, Texture.class);
		assetManager.load(FILES.APPLE_DIS_SHEET.path, Texture.class);
		assetManager.load(FILES.STAR_DIS_SHEET.path, Texture.class);
		assetManager.load(FILES.STAR_GLOW_SHEET.path, Texture.class);
		assetManager.load(FILES.TRAJECTORY_SHEET.path, Texture.class);
		assetManager.load(FILES.TOP_BAR_BUT_SHEET.path, Texture.class);
		assetManager.load(FILES.IN_GAME_FONT_SHEET.path, Texture.class);
		assetManager.load(FILES.PAUSE_MENU_SHEET.path, Texture.class);
		assetManager.load(FILES.TRANSLUCENT_SHEET.path, Texture.class);
		assetManager.load(FILES.LEVEL_CLEARED_SHEET.path, Texture.class);
		assetManager.load(FILES.MAIN_MENU_SHEET.path, Texture.class);
		assetManager.load(FILES.MENU_BACKGROUND_1.path, Texture.class);
		assetManager.load(FILES.WORLD_MENU_SHEET.path, Texture.class);
		assetManager.load(FILES.LEVEL_MENU_W1_SHEET.path, Texture.class);
		assetManager.load(FILES.LEVEL_MENU_SCORE_SHEET.path, Texture.class);
		assetManager.load(FILES.LEVEL_NUMBER_FONT_SHEET.path, Texture.class);
		assetManager.load(FILES.LEVEL_FAILED_SHEET.path, Texture.class);
		assetManager.load(FILES.ABOUT_MENU_SHEET.path, Texture.class);
		assetManager.load(FILES.LEVEL_ONE_ANNOTATIONS.path, Texture.class);
		assetManager.load(FILES.LEVEL_THREE_ANNOTAIONS.path, Texture.class);
		assetManager.load(FILES.LEVEL_FOUR_ANNOTATIONS.path, Texture.class);
		assetManager.load(FILES.LEVEL_FIVE_ANNOTATIONS.path, Texture.class);
		assetManager.load(FILES.LEVEL_SEVEN_ANNOTATIONS.path, Texture.class);
		assetManager.load(FILES.EXIT_BOX.path, Texture.class);
	}
	
	private void loadFonts() {
		assetManager.load(FILES.IN_GAME_FONT.path, BitmapFont.class);
		assetManager.load(FILES.LEVEL_MENU_SCORE_FONT.path, BitmapFont.class);
		assetManager.load(FILES.LEVEL_NUMBER_FONT.path, BitmapFont.class);
	}
	
	private void loadSounds() {
		assetManager.load(FILES.BUTTON_CLICK_S.path, Sound.class);
		assetManager.load(FILES.LEVEL_BACKGROUND_M.path, Music.class);
		assetManager.load(FILES.LEVEL_FAILED_S.path, Sound.class);
		assetManager.load(FILES.MENU_SLIDE_S.path, Sound.class);
		assetManager.load(FILES.MENUS_MUSIC_M.path, Music.class);
		assetManager.load(FILES.F_INSANE_BOUNCE_S.path, Sound.class);
		assetManager.load(FILES.F_MAGICPOT_S.path, Sound.class);
		assetManager.load(FILES.F_SACK_S.path, Sound.class);
		assetManager.load(FILES.F_SLUGGY_BOUNCE_S.path, Sound.class);
		assetManager.load(FILES.F_SPIKY_BOUNCE_S.path, Sound.class);
		assetManager.load(FILES.F_STAR_BOUNCE_S.path, Sound.class);
		assetManager.load(FILES.F_THROWN_S.path, Sound.class);
		
	}
	
	public void unloadAssets() {
		assetManager.unload(FILES.BACKGROUND_SHEET.path);
		assetManager.unload(FILES.GAME_SHEET.path);
		assetManager.unload(FILES.MAGIC_POT_BEF_SHEET.path);
		assetManager.unload(FILES.MAGIC_POT_AFT_SHEET.path);
		assetManager.unload(FILES.CHARACTER_SHEET.path);
		assetManager.unload(FILES.OBSTACLE_SHEET.path);
		assetManager.unload(FILES.ORANGE_DIS_SHEET.path);
		assetManager.unload(FILES.APPLE_DIS_SHEET.path);
		assetManager.unload(FILES.STAR_DIS_SHEET.path);
		assetManager.unload(FILES.STAR_GLOW_SHEET.path);
		assetManager.unload(FILES.TRAJECTORY_SHEET.path);
		assetManager.unload(FILES.TOP_BAR_BUT_SHEET.path);
		assetManager.unload(FILES.PAUSE_MENU_SHEET.path);
		assetManager.unload(FILES.TRANSLUCENT_SHEET.path);
		assetManager.unload(FILES.LEVEL_CLEARED_SHEET.path);
		assetManager.unload(FILES.MAIN_MENU_SHEET.path);
		assetManager.unload(FILES.MENU_BACKGROUND_1.path);
		assetManager.unload(FILES.WORLD_MENU_SHEET.path);
		assetManager.unload(FILES.LEVEL_MENU_W1_SHEET.path);
		
		assetManager.unload(FILES.IN_GAME_FONT_SHEET.path);
		assetManager.unload(FILES.IN_GAME_FONT.path);
	}
	
	public Texture getTexture(FILES fileName) {
		return assetManager.get(fileName.path, Texture.class);
	}
	
	public BitmapFont.BitmapFontData getBitmapFontData(FILES fileName) {
		return assetManager.get(fileName.path, BitmapFont.class).getData();
	}
	
	public BitmapFont getBitmapFont(FILES fileName) {
		return assetManager.get(fileName.path, BitmapFont.class);
	}
	
	public Sound getSound(FILES fileName) {
		return assetManager.get(fileName.path, Sound.class);
	}
	
	public Music getMusic(FILES fileName) {
		return assetManager.get(fileName.path, Music.class);
	}
	
	public void loadAssets() {
		loadTextures();
		loadFonts();
		loadSounds();
	}
	
	public boolean getAssetManagerStatus() {	
		//while(!assetManager.update()) {}
		
		return assetManager.update();
	}
}
