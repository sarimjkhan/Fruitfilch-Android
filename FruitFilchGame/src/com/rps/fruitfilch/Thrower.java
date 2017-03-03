package com.rps.fruitfilch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Thrower extends Image {
	
	public Image hand;
	TextureRegion normHand, throwHand;
	
	public Thrower(TextureRegion region, TextureRegion normHand, TextureRegion throwHand, int x, int y) {
		super(region);
		this.x = x;
		this.y = y;
		hand = new Image(normHand);
		hand.x = this.x + 13;
		hand.y = this.y + 130;
	}
	
	public void addToStage(Stage stage) {
		stage.addActor(this);
		stage.addActor(hand);
	}
	
	public void removeFromStage() {
		this.getStage().removeActor(this);
		hand.getStage().removeActor(hand);
	}
	
	public void changeToThrowHand() {
		hand.setRegion(throwHand);
	}
	
	public void changeToNormHand() {
		hand.setRegion(normHand);
	}

}
