package com.rps.fruitfilch;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.rps.fruitfilch.Level.BAR_NAME;

public abstract class Bar extends Image {
	
	protected TextureRegion barRegion;
	protected final float RATIO;
	protected float localStateTime = 0f;
	protected World worldRef;
	public final BAR_NAME barName;
	
	protected Body barBody;
	protected BodyDef barBodyDef;
	protected FixtureDef barFixtureDef;
	
	protected static BodyEditorLoader loader1 = new BodyEditorLoader(Gdx.files.internal("data/JSON/Bars1.json"));
	protected static BodyEditorLoader loader2 = new BodyEditorLoader(Gdx.files.internal("data/JSON/Bars2.json"));
	protected static BodyEditorLoader loader3 = new BodyEditorLoader(Gdx.files.internal("data/JSON/Bars3.json"));
	protected static BodyEditorLoader loader4 = new BodyEditorLoader(Gdx.files.internal("data/JSON/Bars4.json"));
	
	public Bar(TextureRegion barRegion, BAR_NAME name, float x, float y,float RATIO, World world) {
		super(barRegion);
		worldRef = world;
		this.RATIO = RATIO;
		barName = name;
		this.x = x;
		this.y = y;
	}
	
	public void destroyBody() {
		if(barBody != null) {
			worldRef.destroyBody(barBody);
			barBody = null;
		}
		else Gdx.app.error("Attention", "You tried to destroy a non existing box2d body!");
	}
	
	public void changePosition(float x, float y) {
		barBodyDef.position.set(x/RATIO, y/RATIO);
		this.x = x; this.y = y;
	}
	
	public void changeAngle(double degreeAngle) {
		this.rotation = (float) degreeAngle;
	}
	
	public void addToStage(Stage stage) {
		stage.addActor(this);
	}
	
	public void removeFromStage() {
		this.getStage().removeActor(this);
	}

}
