package com.rps.fruitfilch.bars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.rps.fruitfilch.Bar;
import com.rps.fruitfilch.Level.BAR_NAME;

public class MagicPot extends Bar {
	
	private myAnimation currentAnimation;
	private myAnimation befAnimation;
	private myAnimation aftAnimation;
	private FixtureDef vFixtureDef;
	
	public enum ANIM_TYPE {
		BEF_ANIMATION,
		AFT_ANIMATION;
	}
	
	private class myAnimation extends Animation {
		public final ANIM_TYPE animType;
		
		public myAnimation(ANIM_TYPE animType, float frameDuration,  TextureRegion...keyFrames) {
			super(frameDuration, keyFrames);
			this.animType = animType;
		}
	}

	public MagicPot(TextureRegion[] befPotFrames, TextureRegion[] aftPotFrames, float angle, BAR_NAME name, float x, float y, float RATIO, World world) {
		super(befPotFrames[0], name, x, y, RATIO, world);
		barBodyDef = new BodyDef();
		barBodyDef.position.set(x/RATIO, y/RATIO);
		barBodyDef.type = BodyType.StaticBody;
		
		barFixtureDef = new FixtureDef();
		barFixtureDef.density = 1;
		barFixtureDef.friction = 0.5f;
		barFixtureDef.restitution = 0.0f;
		barFixtureDef.filter.categoryBits = 0x0010;
		
		vFixtureDef = new FixtureDef();
		vFixtureDef.density = 1;
		vFixtureDef.friction = 0.5f;
		vFixtureDef.restitution = 0.0f;
		vFixtureDef.isSensor = true;
		vFixtureDef.filter.categoryBits = 0x1001;
		
		befAnimation = new myAnimation(ANIM_TYPE.BEF_ANIMATION, 0.2f, befPotFrames);
		aftAnimation = new myAnimation(ANIM_TYPE.AFT_ANIMATION, 0.08f, aftPotFrames);
		currentAnimation = befAnimation;
		this.rotation = angle;
	}
	
	public void createBarBody() {
		barBodyDef.angle = (float) Math.toRadians(this.rotation);
		barBody = worldRef.createBody(barBodyDef);
		loader2.attachFixture(barBody, "MagicPot", barFixtureDef, this.width/this.RATIO);		
		loader2.attachFixture(barBody, "MagicPotPoly", vFixtureDef, this.width/this.RATIO);
		barBody.setUserData(this);
	}
	
	public void playMagicAnimation() {
		currentAnimation = aftAnimation;
		localStateTime = 0;
	}
	
	@Override
	public void draw (SpriteBatch batch, float parentAlpha) {
		localStateTime += Gdx.graphics.getDeltaTime();
		if(currentAnimation != null) {
			switch(currentAnimation.animType) {
			case BEF_ANIMATION:
				this.setRegion(currentAnimation.getKeyFrame(localStateTime, true));
				break;
			case AFT_ANIMATION:
				this.setRegion(currentAnimation.getKeyFrame(localStateTime, false));
				if(currentAnimation.isAnimationFinished(localStateTime))
					currentAnimation = befAnimation;
				break;				
			}
		}
		super.draw(batch, parentAlpha);
	}
	
	
}
