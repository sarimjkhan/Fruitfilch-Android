package com.rps.fruitfilch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.rps.fruitfilch.Level.BAR_NAME;

public final class Star extends Bar {
	private final float scale1 = 0.0039f;
	private final float scale2 = 0.0069f;
	private final float scale3 = 0.0099f;
	private float currScale = 1;
	
	private Texture starDisAnimSheet;
	private TextureRegion starRegion;
	private AnimRegions animRegions;
	private Animation starDisAnim; // The disappear animation
	private Image glow;
	private int phasor = 0;
	private boolean isHit = false;
	private boolean isGlow = true;
	private float stateTime = 0;
	private boolean isRemoved = false;
	private boolean pause = false;
	
	private class AnimRegions {
		public TextureRegion[] starDisAnimSeq = new TextureRegion[10];
		{
			starDisAnimSeq[0] = new TextureRegion(starDisAnimSheet, 0, 0, 89, 77);
			starDisAnimSeq[1] = new TextureRegion(starDisAnimSheet, 0, 78, 89, 77);
			starDisAnimSeq[2] = new TextureRegion(starDisAnimSheet, 0, 156, 89, 77);
			starDisAnimSeq[3] = new TextureRegion(starDisAnimSheet, 90, 78, 89, 77);
			starDisAnimSeq[4] = new TextureRegion(starDisAnimSheet, 90, 156, 89, 77);
			starDisAnimSeq[5] = new TextureRegion(starDisAnimSheet, 180, 0, 89, 77);
			starDisAnimSeq[6] = new TextureRegion(starDisAnimSheet, 180, 78, 89, 77);
			starDisAnimSeq[7] = new TextureRegion(starDisAnimSheet, 270, 0, 89, 77);
			starDisAnimSeq[8] = new TextureRegion(starDisAnimSheet, 180, 156, 89, 77);
			starDisAnimSeq[9] = new TextureRegion(starDisAnimSheet, 90, 0, 89, 77);
		}
	}	
	
	public Star(TextureRegion region, TextureRegion glowRegion, Texture disAnimSheet, float x, float y, World worldRef) {
		super(region, BAR_NAME.STAR, x, y, Level.RATIO, worldRef);
		this.starRegion = new TextureRegion(region);
		this.x = x;
		this.y = y;
		this.originX = this.width / 2;
		this.originY = this.height / 2;
		this.starDisAnimSheet = disAnimSheet;
		animRegions = new AnimRegions();
		this.starDisAnim = new Animation(0.1f, animRegions.starDisAnimSeq);		
		glow = new Image(glowRegion);
		glow.x = this.x - 6;
		glow.y = this.y - 8;
		glow.color.a = 0.5f;
		
		barBodyDef = new BodyDef();
		barBodyDef.position.set(x / this.RATIO, y / this.RATIO);
		barBodyDef.type = BodyType.StaticBody;
		
		barFixtureDef = new FixtureDef();
		barFixtureDef.isSensor = true;
		barFixtureDef.filter.categoryBits = 0x0100;
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if(!isHit) {
			super.draw(batch, parentAlpha);
			animateBigStar();
			isRemoved = false;
		}
		else if(isHit) {
			if(isGlow) {
				glow.getStage().removeActor(glow);
				isGlow = false;
			}
			this.scaleX = this.scaleY = 1;
			this.setRegion(starDisAnim.getKeyFrame(stateTime, false));
			super.draw(batch, parentAlpha);
			if(!pause) stateTime += Gdx.graphics.getDeltaTime();
			if(starDisAnim.isAnimationFinished(stateTime) && !worldRef.isLocked()) {
				this.markToRemove(true);
				isHit = false;
				stateTime = 0;
				try {
					worldRef.destroyBody(barBody);
					barBody = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
				isRemoved = true;
			}
		}
	}
	
	public boolean isCompletelyRemoved() {
		return isRemoved;
	}
	
	/*private void destroyStarBody() {
		if(this.barBody != null) {
			worldRef.destroyBody(barBody);
		}
	}*/
	
	private void animateBigStar() {
		phasor = (phasor <= 120) ? ++phasor : 0;
		if(phasor > 0 && phasor <= 60) {
			if(phasor <= 30) {
				currScale -= scale1;
				this.scaleX = currScale; this.scaleY = currScale;
			}
			else if(phasor <= 50) {
				currScale -= scale2;
				this.scaleX = currScale; this.scaleY = currScale;
			}
			else {
				currScale -= scale3;
				this.scaleX = currScale; this.scaleY = currScale;
			}
		}
		
		else if(phasor > 60 && phasor <= 120) {
			if(phasor <= 70) {
				currScale += scale3;
				this.scaleX = currScale; this.scaleY = currScale;
			}
			else if(phasor <= 90) {
				currScale += scale2;
				this.scaleX = currScale; this.scaleY = currScale;
			}
			else {
				currScale += scale1;
				this.scaleX = currScale; this.scaleY = currScale;
			}
		}
		
	}
		
	public void createStarBody() {
		barBodyDef.angle = 0;
		barBody = this.worldRef.createBody(barBodyDef);		
		loader3.attachFixture(barBody, "Star", barFixtureDef, this.width/this.RATIO);
		barBody.setUserData(this);
	}
	
	@Override
	public void changePosition(float x, float y) {
		this.x = x - this.width / 2;
		this.y = y - this.height / 2;
		glow.x = this.x - 6;
		glow.y = this.y - 8;
		barBodyDef.position.set(this.x/this.RATIO, this.y/this.RATIO);
	}	
	
	@Override
	public void addToStage(Stage stage) {
		stage.addActor(glow);
		this.setRegion(starRegion);
		stage.addActor(this);	
		isGlow = true;
	}
	
	@Override
	public void removeFromStage() {
			this.getStage().removeActor(this);
			if(isGlow) glow.getStage().removeActor(glow);		
	}
	
	public void hitStar() {
		isHit = true;
	}
	
	public boolean isHit() {
		return isHit;
	}
	
	public void pause() {
		pause = true;
	}
	
	public void resume() {
		pause = false;
	}
	
	public Body getBody() {
		return barBody;
	}
	
	
	/*public Star(Texture starAnimSheet, Texture disAnimSheet, float x, float y) {
		super(new TextureRegion(starAnimSheet, 0, 0, 32, 30));
		this.x = x; this.y = y;
		this.originX = this.width / 2; this.originY = this.height / 2;
		this.starNormAnimSheet = starAnimSheet;
		this.starDisAnimSheet = disAnimSheet;
		animRegions = new AnimRegions();
		starNormAnim = new Animation(0.3f, animRegions.starNormAnimSeq);
		starDisAnim = new Animation(0.1f, animRegions.starDisAnimSeq);
	}*/
	
	/*@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if(!isHit) {			
			this.setRegion(starNormAnim.getKeyFrame(stateTime1, true));
			stateTime1 += Gdx.graphics.getDeltaTime();
			super.draw(batch, parentAlpha);
		}
		else {
			this.setRegion(starDisAnim.getKeyFrame(stateTime2, false));
			stateTime2 += Gdx.graphics.getDeltaTime();
			super.draw(batch, parentAlpha);
			if(starDisAnim.isAnimationFinished(stateTime2)) {
				this.markToRemove(true);
				isHit = false;
				stateTime1 = stateTime2 = 0;
			}
		}
	}*/

}
