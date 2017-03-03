package com.rps.fruitfilch.bars;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.rps.fruitfilch.Bar;
import com.rps.fruitfilch.Level.BAR_NAME;

public final class StickyBar extends Bar {
	
	public StickyBar(TextureRegion barRegion, float angle, BAR_NAME name, float x, float y, float RATIO, World world) {
		super(barRegion, name, x, y, RATIO, world);
		barBodyDef = new BodyDef();
		barBodyDef.position.set(x/RATIO, y/RATIO);
		barBodyDef.type = BodyType.StaticBody;
		
		barFixtureDef = new FixtureDef();
		barFixtureDef.density = 1f;
		barFixtureDef.friction = 10f;
		barFixtureDef.restitution = 0.0f;
		barFixtureDef.filter.categoryBits = 0x0011;
		
		this.rotation = angle;
	}
	
	public void createBarBody() {
		barBodyDef.angle = (float) Math.toRadians(this.rotation);
		barBody = worldRef.createBody(barBodyDef);
		loader1.attachFixture(barBody, "StickyBar", barFixtureDef, this.width/this.RATIO);
	}
}
