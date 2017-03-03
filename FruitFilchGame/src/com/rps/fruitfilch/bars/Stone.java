package com.rps.fruitfilch.bars;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.rps.fruitfilch.Bar;
import com.rps.fruitfilch.Level.BAR_NAME;

public final class Stone extends Bar {

	public Stone(TextureRegion barRegion, BAR_NAME name, float x, float y, float rotation,
			float RATIO, World world) {
		super(barRegion, name, x, y, RATIO, world);
		barBodyDef = new BodyDef();
		barBodyDef.position.set(x/RATIO, y/RATIO);
		barBodyDef.type = BodyType.StaticBody;
		
		barFixtureDef = new FixtureDef();
		barFixtureDef.density  = 1;
		barFixtureDef.friction = 0.5f;
		barFixtureDef.restitution = 0.0f;
		
		this.rotation = rotation;
	}
	
	public void createStoneBody() {
		barBodyDef.angle = (float) Math.toRadians(this.rotation);
		barBody = worldRef.createBody(barBodyDef);
		loader3.attachFixture(barBody, "Stones", barFixtureDef, this.width/this.RATIO);
	}	

}
