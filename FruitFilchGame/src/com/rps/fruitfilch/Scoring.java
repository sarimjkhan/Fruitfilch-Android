package com.rps.fruitfilch;

import com.badlogic.gdx.Gdx;

public final class Scoring {
	
	private final int threshold = 1000;
	private int score = 0;
	private float timer = 0;
	
	public static enum SCORE {
		MAGIC_POT(1500),
		POT(1000),
		STAR(1000);
		
		private final int val;		
		SCORE(int val) { this.val = val; }
	}	
	
	public Scoring() {}
	
	public void update() {
		this.timer += Gdx.graphics.getDeltaTime(); 
	}
	
	public void add(SCORE scoreType) {
		switch(scoreType) {
		case MAGIC_POT:
			this.score += scoreType.val;
			break;
		case POT:
			this.score += scoreType.val;
			break;
		case STAR:
			this.score += scoreType.val;
			break;
		}
	}
	
	public void add(int val) {
		this.score += val;
	}
	
	public int getFinalScore() {
		int addValue = ((threshold - (this.timer * 9)) > 0) ? threshold - (int) (this.timer * 9) : 0;
		return this.score = this.score + addValue;
	}
	
	public int getScore() {
		return score;
	}

}
