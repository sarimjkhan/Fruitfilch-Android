package com.rps.fruitfilch.worlds.worldone;

import java.util.ArrayList;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.rps.fruitfilch.FruitRageGame;
import com.rps.fruitfilch.Level;

public final class WorldOne {
	
	public static enum WORLD_ONE {
		LEVEL1, LEVEL2, LEVEL3, LEVEL4, LEVEL5, LEVEL6, LEVEL7, LEVEL8, LEVEL9, LEVEL10, LEVEL11, LEVEL12, LEVEL13, LEVEL14, LEVEL15;
	}

	private static FileHandle fileHandle;
	private static String configString;
	private FruitRageGame localGameRef;
	
	private static ArrayList<String> levelStrings;
	
	public WorldOne(FruitRageGame ref) {
		localGameRef = ref;
		fileHandle = Gdx.files.internal("data/WorldFiles/WorldOne.fr");
		configString = fileHandle.readString();
		levelStrings = new ArrayList<String>();
		for(int i = 1; i < 16; ++i) {
			levelStrings.add(getLevelConfigString(WORLD_ONE.valueOf("LEVEL" + i)));
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Level> T createLevel(WorldOne.WORLD_ONE level) {
		String levelConfigString = getStringFromArray(level);
		switch(level) {
		case LEVEL1:			
			if(levelConfigString != null)
				return (T) new Level1(localGameRef, levelConfigString);
			break;
		case LEVEL2:
			if(levelConfigString != null) 
				return (T) new Level2(localGameRef, levelConfigString);
			break;
		case LEVEL3:
			if(levelConfigString != null) 
				return (T) new Level3(localGameRef, levelConfigString);
			break;
		case LEVEL4:
			if(levelConfigString != null) 
				return (T) new Level4(localGameRef, levelConfigString);
			break;
		case LEVEL5:
			if(levelConfigString != null) 
				return (T) new Level5(localGameRef, levelConfigString);
			break;
		case LEVEL6:
			if(levelConfigString != null) 
				return (T) new Level6(localGameRef, levelConfigString);
			break;
		case LEVEL7:
			if(levelConfigString != null) 
				return (T) new Level7(localGameRef, levelConfigString);
			break;
		case LEVEL8:
			if(levelConfigString != null) 
				return (T) new Level8(localGameRef, levelConfigString);
			break;
		case LEVEL9:
			if(levelConfigString != null) 
				return (T) new Level9(localGameRef, levelConfigString);
			break;
		case LEVEL10:
			if(levelConfigString != null) 
				return (T) new Level10(localGameRef, levelConfigString);
			break;
		case LEVEL11:
			if(levelConfigString != null) 
				return (T) new Level11(localGameRef, levelConfigString);
			break;
		case LEVEL12:
			if(levelConfigString != null) 
				return (T) new Level12(localGameRef, levelConfigString);
			break;
		case LEVEL13:
			if(levelConfigString != null) 
				return (T) new Level13(localGameRef, levelConfigString);
			break;
		case LEVEL14:
			if(levelConfigString != null) 
				return (T) new Level14(localGameRef, levelConfigString);
			break;			
		case LEVEL15:
			if(levelConfigString != null) 
				return (T) new Level15(localGameRef, levelConfigString);
			break;
		}
		Gdx.app.error("Empty String Generated", "Check for errors");
		return null;
	}
	
	private String getStringFromArray(WORLD_ONE level) {
		String tmp = level.toString();
		String num = tmp.toString().substring(5, tmp.length());
		int levelNumber = Integer.valueOf(num);
			return levelStrings.get(levelNumber - 1);
	}
	
	private String getLevelConfigString(WORLD_ONE level) {
		Scanner scanner = new Scanner(configString);
		String levelConfigString = new String();
		switch(level) {
		case LEVEL1:
			return customScan("Level1", "Level2", levelConfigString, scanner);
		case LEVEL2:
			return customScan("Level2", "Level3", levelConfigString, scanner);
		case LEVEL3:
			return customScan("Level3", "Level4", levelConfigString, scanner);
		case LEVEL4:
			return customScan("Level4", "Level5", levelConfigString, scanner);
		case LEVEL5:
			return customScan("Level5", "Level6", levelConfigString, scanner);
		case LEVEL6:
			return customScan("Level6", "Level7", levelConfigString, scanner);
		case LEVEL7:
			return customScan("Level7", "Level8", levelConfigString, scanner);
		case LEVEL8:
			return customScan("Level8", "Level9", levelConfigString, scanner);
		case LEVEL9:
			return customScan("Level9", "Level10", levelConfigString, scanner);
		case LEVEL10:
			return customScan("Level10", "Level11", levelConfigString, scanner);
		case LEVEL11:
			return customScan("Level11", "Level12", levelConfigString, scanner);
		case LEVEL12:
			return customScan("Level12", "Level13", levelConfigString, scanner);
		case LEVEL13:
			return customScan("Level13", "Level14", levelConfigString, scanner);
		case LEVEL14:
			return customScan("Level14", "Level15", levelConfigString, scanner);
		case LEVEL15:
			return customScan("Level15", "None", levelConfigString, scanner);
		}
		return null;
	}
	
	private String customScan(String level, String nextLevel, String levelConfigString, Scanner scanner) {
		while(scanner.hasNext()) {
			if(scanner.hasNext(level)) {
				scanner.next();
				while(scanner.hasNext()) {
					levelConfigString = levelConfigString + scanner.nextLine();
					levelConfigString = levelConfigString + " ";
					if(nextLevel != "None")
						if(scanner.hasNext())
							if(scanner.hasNext(nextLevel)) break;
				}
				return levelConfigString;
			}
			scanner.next();
		}
		return null;
	}
	
}
