package com.rps.fruitfilch.bars;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.rps.fruitfilch.Bar;
import com.rps.fruitfilch.Level.BAR_NAME;

public final class NormalBar extends Bar {
		
	public NormalBar(TextureRegion barRegion, float angle, BAR_NAME name, float x, float y, float RATIO, World world) {
		super(barRegion, name, x, y, RATIO, world);
		barBodyDef = new BodyDef();
		barBodyDef.position.set(x/RATIO, y/RATIO);
		barBodyDef.type = BodyType.StaticBody;
		
		barFixtureDef = new FixtureDef();
		barFixtureDef.density = 1;
		barFixtureDef.friction = 0.5f;
		barFixtureDef.restitution = 0.3f;
		
		this.rotation = angle;
	}
	
	public void createBarBody() {
		barBodyDef.angle = (float) Math.toRadians(this.rotation);
		barBody = worldRef.createBody(barBodyDef);
		loader1.attachFixture(barBody, "NormalBar", barFixtureDef, this.width/this.RATIO);		
	}	

}
