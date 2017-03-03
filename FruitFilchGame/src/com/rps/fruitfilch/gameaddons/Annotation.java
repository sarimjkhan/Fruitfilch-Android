package com.rps.fruitfilch.gameaddons;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.rps.fruitfilch.gameutils.Coor;

public class Annotation extends Image {
	
	private Texture animSpriteSheet;
	private TextureRegion currentRegion;
	private TextureRegion[] animSpriteSheetSequences;
	private Animation animation;	
	private final int numOfSequences;
	private ArrayList<Coor> coor;
	private float stateTime = 0;
	private boolean myMarkToRemove = false;
	
	public boolean isCompletelyRemoved = false;
	
	public Annotation(Texture animSheet, ArrayList<Coor> coor) {
		super(new TextureRegion(animSheet, coor.get(0).x, coor.get(0).y, coor.get(0).width, coor.get(0).height));
		this.currentRegion = this.getRegion();
		this.x = 0; this.y = 0;
		this.numOfSequences = coor.size();
		this.animSpriteSheet = animSheet;
		this.coor = coor;
		animSpriteSheetSequences = new TextureRegion[numOfSequences];
		animSpriteSheetSequences[0] = this.getRegion();
		
		if(this.numOfSequences > 1) {
			for(int i = 1; i < this.numOfSequences; ++i) {
				Coor currCoor = coor.get(i);
				animSpriteSheetSequences[i] = new TextureRegion(animSheet, currCoor.x, currCoor.y, currCoor.width, currCoor.height);
			}
		}
		
		animation = new Animation(0.15f, animSpriteSheetSequences);
	}
	
	public Annotation(Texture animSheet, ArrayList<Coor> coor, float frameDuration) {
		super(new TextureRegion(animSheet, coor.get(0).x, coor.get(0).y, coor.get(0).width, coor.get(0).height));
		this.currentRegion = this.getRegion();
		this.x = 0; this.y = 0;
		this.numOfSequences = coor.size();
		this.animSpriteSheet = animSheet;
		this.coor = coor;
		animSpriteSheetSequences = new TextureRegion[numOfSequences];
		animSpriteSheetSequences[0] = this.getRegion();
		
		if(this.numOfSequences > 1) {
			for(int i = 1; i < this.numOfSequences; ++i) {
				Coor currCoor = coor.get(i);
				animSpriteSheetSequences[i] = new TextureRegion(animSheet, currCoor.x, currCoor.y, currCoor.width, currCoor.height);
			}
		}
		
		animation = new Animation(frameDuration, animSpriteSheetSequences);
	}
	
	public Annotation(Texture animSheet, ArrayList<Coor> coor, float frameDuration, String name) {
		super(new TextureRegion(animSheet, coor.get(0).x, coor.get(0).y, coor.get(0).width, coor.get(0).height), Scaling.none, 0, name);
		this.currentRegion = this.getRegion();
		this.x = 0; this.y = 0;
		this.numOfSequences = coor.size();
		this.animSpriteSheet = animSheet;
		this.coor = coor;
		animSpriteSheetSequences = new TextureRegion[numOfSequences];
		animSpriteSheetSequences[0] = this.getRegion();
		
		if(this.numOfSequences > 1) {
			for(int i = 1; i < this.numOfSequences; ++i) {
				Coor currCoor = coor.get(i);
				animSpriteSheetSequences[i] = new TextureRegion(animSheet, currCoor.x, currCoor.y, currCoor.width, currCoor.height);
			}
		}
		
		animation = new Animation(frameDuration, animSpriteSheetSequences);
	}
	
	public Annotation(Texture animSheet, float x, float y, ArrayList<Coor> coor) {
		super(new TextureRegion(animSheet, coor.get(0).x, coor.get(0).y, coor.get(0).width, coor.get(0).height));
		this.currentRegion = this.getRegion();
		this.x = x; this.y = y;
		this.numOfSequences = coor.size();
		this.animSpriteSheet = animSheet;
		this.coor = coor;
		animSpriteSheetSequences = new TextureRegion[numOfSequences];
		animSpriteSheetSequences[0] = this.getRegion();
		
		if(this.numOfSequences > 1) {
			for(int i = 1; i < this.numOfSequences; ++i) {
				Coor currCoor = coor.get(i);
				animSpriteSheetSequences[i] = new TextureRegion(animSheet, currCoor.x, currCoor.y, currCoor.width, currCoor.height);
			}
		}
		
		animation = new Animation(0.1f, animSpriteSheetSequences);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		this.currentRegion = animation.getKeyFrame(stateTime, true);
		this.setRegion(this.currentRegion);
		if(!this.isMarkedToRemove())
			stateTime += Gdx.graphics.getDeltaTime();
		else stateTime = 0;
		if(myMarkToRemove) {
			this.color.a -= 0.0125f;
			if(this.color.a <= 0) {
				this.markToRemove(true);
				myMarkToRemove = false;
				stateTime = 0;
				isCompletelyRemoved = true;
			}
		}
	}
	
	public void setPosition(float x, float y) {
		this.x = x; this.y = y;
	}
	
	public boolean removeWithAnimation(boolean markToRemove) {
		isCompletelyRemoved = false;
		return this.myMarkToRemove = markToRemove;		
	}
	
	public void addToStage(Stage localStage) {
		localStage.addActor(this);
		isCompletelyRemoved = false;
	}

}
