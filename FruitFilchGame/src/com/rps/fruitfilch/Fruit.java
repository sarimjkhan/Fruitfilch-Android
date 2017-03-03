package com.rps.fruitfilch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.rps.fruitfilch.Level.FRUIT_NAME;

public final class Fruit extends Image implements Cloneable {
	
	private Animation explAnim;
	private Texture animSheet;
	private FruitAnim fruitAnimRegions;
	private boolean explode = false;
	private boolean inHand = false;
	private boolean isRemoved = false;
	
	
	//private boolean drawTraj = false;
	private boolean toDestroy = false;
	private final float RATIO;
	private TextureRegion fruitRegion;
	//private TextureRegion trajDot;
	private FRUIT_NAME fruitName;
		
	private World worldRef;
	private Body fruitBody; //Private due to Danish
	private BodyDef fruitBodyDef;
	private FixtureDef fruitFixtureDef;
	private CircleShape fruitShape;
	private float stateTime = 0;
	
	private class FruitAnim {
		public TextureRegion[] disAnim = new TextureRegion[7];
		{
			disAnim[0] = new TextureRegion(animSheet, 0, 0, 91, 30);
			disAnim[1] = new TextureRegion(animSheet, 0, 31, 91, 30);
			disAnim[2] = new TextureRegion(animSheet, 0, 62, 91, 30);
			disAnim[3] = new TextureRegion(animSheet, 0, 93, 91, 30);
			disAnim[4] = new TextureRegion(animSheet, 92, 0, 91, 30);
			disAnim[5] = new TextureRegion(animSheet, 92, 31, 91, 30);
			disAnim[6] = new TextureRegion(animSheet, 92, 62, 91, 30);
		}
	}
	
	public Fruit(TextureRegion fruitRegion, Texture animSheet, float fruitRadius, FRUIT_NAME name, float x, float y, float RATIO, World world) {
		super(fruitRegion);
		this.animSheet = animSheet;
		fruitAnimRegions = new FruitAnim();
		//this.trajDot = trajDot;
		//trajectory = new ArrayList<Image>();
		this.RATIO = RATIO;		
		worldRef = world;
		
		fruitBodyDef = new BodyDef();
		fruitBodyDef.position.set(x / this.RATIO, y / this.RATIO);
		fruitBodyDef.type = BodyType.DynamicBody;
		
		fruitShape = new CircleShape();
		fruitShape.setRadius(fruitRadius / this.RATIO);
		
		fruitFixtureDef = new FixtureDef();
		fruitFixtureDef.shape = fruitShape;
		fruitFixtureDef.density = 0.2f;
		fruitFixtureDef.restitution = 0.7f;
		fruitFixtureDef.friction = 0.8f;
		fruitFixtureDef.filter.categoryBits = 0x0101;
		
		this.originX = this.width / 2;
		this.originY = this.height / 2;
		
		this.x = (fruitBodyDef.position.x - fruitShape.getRadius()) * this.RATIO;
		this.y = (fruitBodyDef.position.y - fruitShape.getRadius()) * this.RATIO;
		
		fruitName = name;
		explAnim = new Animation(0.08f, fruitAnimRegions.disAnim);
	}
	
	
	/** COMPATIBLE 
	 * Please be careful when using this function 
	 * This method will schedule a safe removal of the libgdx Actor and its associated Box2d body. */
	public void removeWithoutExplosion() {
		toDestroy = true;
	}
	
	public void addToHand(Stage stage) {
		stage.addActor(this);
		inHand = true;		
	}
	
	public void addFruitToStage (Stage stage) {
		stage.addActor(this);
		createFruitBody();
	}
	
	public void setLinearVelocity(Vector2 velocity) {
		if(fruitBody != null)
			fruitBody.setLinearVelocity(velocity);
		else
			Gdx.app.error("Create fruit body before setting linear velocity", "(Note) Fruit body is created when addFruitToStage method is called");
		
	}
	
	private void createFruitBody() {
		fruitBody = worldRef.createBody(fruitBodyDef);
		fruitBody.createFixture(fruitFixtureDef);
	}
	
	private Vector2 prevFruitStageCenter;
	
	private void update() {
		prevFruitStageCenter= fruitBody.getPosition();
		prevFruitStageCenter.x *= this.RATIO; prevFruitStageCenter.y *= this.RATIO;
		this.x = (fruitBody.getPosition().x - fruitShape.getRadius()) * this.RATIO;
		this.y = (fruitBody.getPosition().y - fruitShape.getRadius()) * this.RATIO;
		this.rotation = (float) Math.toDegrees(fruitBody.getAngle());
		/*Gdx.app.log(String.valueOf(this.x = (fruitBody.getPosition().x - fruitShape.getRadius()) * this.RATIO),
				String.valueOf((fruitBody.getPosition().y - fruitShape.getRadius()) * this.RATIO));*/
	}
	
	/*public void setfruitName(FRUIT_NAME name) {
		this.fruitName = name;
	}
	
	public FRUIT_NAME getfruitName() {
		return fruitName;
	}*/
	
	private float animX, animY = 0;
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		//stateTime += Gdx.app.getGraphics().getDeltaTime();
		if(!toDestroy) {
			if(fruitBody != null) {
				update();
				//if(drawTraj) 
					//drawDot();
				Vector2 tmp = this.getFruitCenterInStageCoor();
				animX = tmp.x - 40;
				animY = tmp.y - 20;
			}
			super.draw(batch, parentAlpha);
		}
		else {
			if(explode) {
				this.setRegion(null);
				this.rotation = 0;
				this.x = animX; this.y = animY;
				this.width = fruitAnimRegions.disAnim[0].getRegionWidth();
				this.height = fruitAnimRegions.disAnim[0].getRegionHeight();
				this.setRegion(explAnim.getKeyFrame(stateTime, false));
				super.draw(batch, parentAlpha);
				stateTime += Gdx.graphics.getDeltaTime();
				if(explAnim.isAnimationFinished(stateTime) && !worldRef.isLocked()) {
					destroyFruitBody();
					this.markToRemove(true);
					explode = false;
					stateTime = 0;
					isRemoved = true;
				}
			} else if(!explode && !worldRef.isLocked()) {
				destroyFruitBody();
				this.markToRemove(true);
				stateTime = 0;
				isRemoved = true;
			}
		}
	}
	
	public boolean isCompleteleyRemoved() {
		return isRemoved;
	}
	
	private void destroyFruitBody() {
		if(fruitBody != null) {
			worldRef.destroyBody(fruitBody);
			fruitBody = null;
		}
	}
	
	//private float prevStateTime = 0;
	
	/*private void drawDot() {
		if(stateTime - prevStateTime >= 0.01f) {
			prevStateTime = stateTime;
			Vector2 vel = fruitBody.getLinearVelocity();
			if(drawTraj) {
				if(vel.x != 0 && vel.y != 0) {
					Image dot = new Image(this.trajDot);
					Vector2 pos = getFruitCenter();
					pos.x = pos.x - dot.width/2;
					pos.y = pos.y - dot.height/2;
					dot.x = pos.x;
					dot.y = pos.y; 
					stage.addActor(dot);
				}
			}
		}
	}*/
	
	public Object clone() {
		Fruit clone = null;
		try {
			clone = (Fruit) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;		
	}
	
	public Vector2 getFruitCenterInStageCoor() {
		Vector2 tmp = this.fruitBody.getPosition();
		tmp.x  *= this.RATIO; tmp.y *= this.RATIO;
		return tmp;
	}
	
	public void removeWithExplosion() { explode = true; toDestroy = true; }

	public boolean isGonnaExplode() {
		return (explode || toDestroy);
	}

	public Body getBodyStatus() {
		return fruitBody;
	}
	
	/*private void drawFruitTraj(boolean bool) {
		drawTraj = bool;
		if(!drawTraj) {
			for(Image current: trajectory)
				stage.removeActor(current);
			trajectory.clear();
		}
		
	}*/

}
