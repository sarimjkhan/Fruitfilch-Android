package com.rps.fruitfilch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.rps.fruitfilch.Level.BAR_NAME;

public final class Catcher extends Bar {
	
	private FixtureDef vFixtureDef;
	

	public Catcher(TextureRegion barRegion, BAR_NAME name, float x, float y,
			float RATIO, World world) {
		super(barRegion, name, x, y, RATIO, world);
		barBodyDef = new BodyDef();
		barBodyDef.position.set(x/RATIO, y/RATIO);
		barBodyDef.type = BodyType.StaticBody;
		
		barFixtureDef = new FixtureDef();
		barFixtureDef.density = 1;
		barFixtureDef.friction = 0.5f;
		barFixtureDef.restitution = 0.0f;
		barFixtureDef.filter.categoryBits = 0x0111;
		
		vFixtureDef = new FixtureDef();
		vFixtureDef.density = 1;
		vFixtureDef.friction = 0.5f;
		vFixtureDef.restitution = 0.0f;
		vFixtureDef.isSensor = true;
		vFixtureDef.filter.categoryBits = 0x1000;		
	}
	
	public void createBody() {
		barBodyDef.angle = (float) Math.toRadians(this.rotation);
		barBody = worldRef.createBody(barBodyDef);
		loader4.attachFixture(barBody, "Catcher", barFixtureDef, this.width/this.RATIO);
		loader4.attachFixture(barBody, "CatcherPoly", vFixtureDef, this.width/this.RATIO);
	}

}
