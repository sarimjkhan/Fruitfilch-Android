package com.rps.fruitfilch;

public interface SQLHelperLibgdx {
	
	public void openDatabase();
	public void closeDatabase();
	public void updateLevelScore(int level,  int score, int stars);
	public int getScore(int levelID);
	public int getStars(int levelID);
	public boolean isLevelCleared(int levelID);
}
