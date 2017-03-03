package com.rps.fruitfilch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.rps.fruitfilch.bars.BouncyBar;
import com.rps.fruitfilch.bars.IBouncyBar;
import com.rps.fruitfilch.bars.MagicPot;
import com.rps.fruitfilch.bars.NormalBar;
import com.rps.fruitfilch.bars.SpikyBar;
import com.rps.fruitfilch.bars.StickyBar;
import com.rps.fruitfilch.gameutils.ConfigWriter;

public class LevelCreator implements Screen, InputProcessor {
	private LevelCreator localThis = this;
	private boolean testMode = false;
	
	private class Menu {
		public Bar currentlySelected = null;
		
		private Texture menuSheet = new Texture("data/images/Menu.png");
		public TextureRegion downState = new TextureRegion(menuSheet, 0, 0, 107, 32);
		public TextureRegion bulbWhite = new TextureRegion(menuSheet, 0, 127, 43, 44);
		public TextureRegion bulbYellow = new TextureRegion(menuSheet, 177, 66, 43, 44);
		
		private ImageButton generateButton = new ImageButton(new TextureRegion(menuSheet, 0, 66, 107, 32), downState) {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				if(!testMode && currentlySelected == null) {
					super.touchDown(x, y, pointer);
					return true;
				}
				return false;
			}
			@Override
			public void touchUp(float x, float y, int pointer) {
				super.touchUp(x, y, pointer);
				ArrayList barCollection = new ArrayList<Bar>();
				barCollection.addAll(normalBarArr);
				barCollection.addAll(bouncyBarArr);
				barCollection.addAll(iBouncyBarArr);
				barCollection.addAll(stickyBarArr);
				barCollection.addAll(spikyBarArr);
				barCollection.addAll(magicPotArr);
				try {
					ConfigWriter.writeConfigurationFile("src/World_X.fr", barCollection);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		private ImageButton toggleTestMode = new ImageButton(new TextureRegion(menuSheet, 108, 33, 107, 32), downState, new TextureRegion(menuSheet, 0, 33, 107, 32)){
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				if(currentlySelected == null) {
					super.touchDown(x, y, pointer);
					return true;
				}
				return false;
			}
			@Override
			public void touchUp(float x, float y, int pointer) {
				super.touchUp(x, y, pointer);
				if(testMode) {
					testMode = false;
					multiplexer.addProcessor(1, localThis);					
					destroyBox2dWorld();
				}
				else {
					testMode = true;
					multiplexer.removeProcessor(1);					
					createBox2dWorld();
				}
			}
		};		
		private ImageButton removeBar = new ImageButton(new TextureRegion(menuSheet, 108, 0, 107, 32), downState){
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				if(menu.currentlySelected != null && !testMode) {
					super.touchDown(x, y, pointer);
					return true;
				}
				return false;
			}			
			@Override
			public void touchUp(float x, float y, int pointer) {
				super.touchUp(x, y, pointer);
				BAR_NAME currentRemoval = menu.currentlySelected.barName;
				Iterator i;
				switch(currentRemoval) {
				case NORMAL_BAR:
					i = normalBarArr.iterator();
					normalBarArr.remove(currentlySelected);
					currentlySelected.removeBarFromStage();
					currentlySelected = null;
					switchBulbState();
					break;
				case SPIKY_BAR:
					i = spikyBarArr.iterator();
					spikyBarArr.remove(currentlySelected);
					currentlySelected.removeBarFromStage();
					currentlySelected = null;
					switchBulbState();
					break;
				case STICKY_BAR:
					i = stickyBarArr.iterator();
					stickyBarArr.remove(currentlySelected);
					currentlySelected.removeBarFromStage();
					currentlySelected = null;
					switchBulbState();
					break;
				case BOUNCY_BAR:
					i = bouncyBarArr.iterator();
					bouncyBarArr.remove(currentlySelected);
					currentlySelected.removeBarFromStage();
					currentlySelected = null;
					switchBulbState();
					break;
				case INSANELY_BOUNCY_BAR:
					i = iBouncyBarArr.iterator();
					iBouncyBarArr.remove(currentlySelected);
					currentlySelected.removeBarFromStage();
					currentlySelected = null;
					switchBulbState();
					break;
				case MAGIC_POT:
					i = magicPotArr.iterator();
					magicPotArr.remove(currentlySelected);
					currentlySelected.removeBarFromStage();
					currentlySelected = null;
					switchBulbState();
					break;
				}				
			}
		};		
		private ImageButton normalBarButton = new ImageButton(new TextureRegion(menuSheet, 73, 99, 69, 10), downState) {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				if(menu.currentlySelected == null && !testMode) {
					super.touchDown(x, y, pointer);
					return true;
				}
				return false;
			}			
			@Override
			public void touchUp(float x, float y, int pointer) {
				super.touchUp(x, y, pointer);
				addNormalBar();
			}
		};
		private ImageButton spikyBarButton = new ImageButton(new TextureRegion(menuSheet, 0, 99, 72, 12), downState) {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				if(menu.currentlySelected == null && !testMode) {
					super.touchDown(x, y, pointer);
					return true;
				}
				return false;
			}
			@Override
			public void touchUp(float x, float y, int pointer) {
				super.touchUp(x, y, pointer);
				addSpikyBar();
			}
		};
		private ImageButton stickyBarButton = new ImageButton(new TextureRegion(menuSheet, 0, 112, 70, 14), downState) {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				if(menu.currentlySelected == null && !testMode) {
					super.touchDown(x, y, pointer);
					return true;
				}
				return false;
			}
			@Override
			public void touchUp(float x, float y, int pointer) {
				super.touchUp(x, y, pointer);
				addStickyBar();
			}
		};
		private ImageButton  bouncyBarButton = new ImageButton(new TextureRegion(menuSheet, 73, 110, 68, 15), downState) {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				if(menu.currentlySelected == null && !testMode) {
					super.touchDown(x, y, pointer);
					return true;
				}
				return false;
			}
			@Override
			public void touchUp(float x, float y, int pointer) {
				super.touchUp(x, y, pointer);
				addBouncyBar();
			}
		};
		private ImageButton iBouncyBarButton = new ImageButton(new TextureRegion(menuSheet, 108, 66, 68, 19), downState) {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				if(menu.currentlySelected == null && !testMode) {
					super.touchDown(x, y, pointer);
					return true;
				}
				return false;
			}
			@Override
			public void touchUp(float x, float y, int pointer) {
				super.touchUp(x, y, pointer);
				addIBouncyBar();
			}
		};
		private ImageButton magicalBarButton = new ImageButton(new TextureRegion(menuSheet, 108, 86, 66, 11), downState) {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				if(menu.currentlySelected == null && !testMode) {
					super.touchDown(x, y, pointer);
					return true;
				}
				return false;
			}
			@Override
			public void touchUp(float x, float y, int pointer) {
				super.touchUp(x, y, pointer);
				addMagicPot();
			}
		};
		private Image bulb = new Image(bulbWhite) {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				if(currentlySelected != null) {
					currentlySelected = null;
					this.setRegion(bulbWhite);
					return true;
				}
				return false;
			}
		};
		
		private NinePatch ninePatch = new NinePatch(new TextureRegion(menuSheet, 0, 172, 25, 22), 8, 8, 8, 8);
		private SliderStyle sliderStyle = new SliderStyle(ninePatch, new TextureRegion(menuSheet, 44, 127, 25, 25));
		public Slider slider = new Slider(0, 360, 1, sliderStyle) {
			@Override
			public void touchDragged(float x, float y, int pointer) {
				super.touchDragged(x, y, pointer);
				if(currentlySelected != null) {
					currentlySelected.rotation = this.getValue();
				}
			}
		};
		
		
		public Menu() {
			generateButton.x = 480; generateButton.y = 16;
			toggleTestMode.x = 595; toggleTestMode.y = 480 - (432 + 32);
			removeBar.x = 12; removeBar.y = 480 - (19 + 32);
			normalBarButton.x = 29; normalBarButton.y = 480 - (427 + 11);
			spikyBarButton.x = 27; spikyBarButton.y = 480 - (452 + 12);
			stickyBarButton.x = 119; stickyBarButton.y = 480 - (424 + 14);
			bouncyBarButton.x = 119; bouncyBarButton.y = 480 - (449 + 15);
			iBouncyBarButton.x = 207; iBouncyBarButton.y = 480 - (423 + 19);
			magicalBarButton.x = 209; magicalBarButton.y = 480 - (453 + 11);
			bulb.x = 18; bulb.y = 480 - (61 + 44);
			slider.x = 300; slider.y = 480 - (437 + 23);
			slider.setValue(0);
		}
		
		public void addMenuToStage() {
			localStage.addActor(generateButton);
			localStage.addActor(toggleTestMode);
			localStage.addActor(removeBar);
			localStage.addActor(normalBarButton);
			localStage.addActor(spikyBarButton);
			localStage.addActor(stickyBarButton);
			localStage.addActor(bouncyBarButton);
			localStage.addActor(iBouncyBarButton);
			localStage.addActor(magicalBarButton);
			localStage.addActor(bulb);
			localStage.addActor(slider);
		}
		
		public void switchBulbState() {
			if(menu.currentlySelected != null)
				bulb.setRegion(bulbYellow);
			else bulb.setRegion(bulbWhite);
		}
	}
	
	public static enum FRUIT_NAME {
		ORANGE,
		APPLE,
		STRAWBERRY;
	}
	
	public static enum BAR_NAME {
		NORMAL_BAR,
		BOUNCY_BAR,
		INSANELY_BOUNCY_BAR,
		STICKY_BAR,
		SPIKY_BAR,
		MAGIC_POT;
	}
	
	private class Regions {		
		private Texture gameSheet = new Texture("data/images/GameSheet.png");
		private Texture magicPotBefSheet = new Texture("data/images/MagicPotBefore.png");
		private Texture magicPotAftSheet = new Texture("data/images/MagicPotAfter.png");
		public TextureRegion[] magicPotBefFrames = new TextureRegion[4];
		public TextureRegion[] magicPotAftFrames = new TextureRegion[14];
		{
			magicPotBefFrames[0] = new TextureRegion(magicPotBefSheet, 0, 0, 63, 48);
			magicPotBefFrames[1] = new TextureRegion(magicPotBefSheet, 64, 0, 63, 48);
			magicPotBefFrames[2] = new TextureRegion(magicPotBefSheet, 0, 49, 63, 48);
			magicPotBefFrames[3] = new TextureRegion(magicPotBefSheet, 64, 49, 63, 48);
			
			magicPotAftFrames[0] = new TextureRegion(magicPotAftSheet, 0, 0, 69, 48);
			magicPotAftFrames[1] = new TextureRegion(magicPotAftSheet, 0, 98, 69, 48);
			magicPotAftFrames[2] = new TextureRegion(magicPotAftSheet, 0, 147, 69, 48);
			magicPotAftFrames[3] = new TextureRegion(magicPotAftSheet, 70, 98, 69, 48);
			magicPotAftFrames[4] = new TextureRegion(magicPotAftSheet, 0, 196, 69, 48);
			magicPotAftFrames[5] = new TextureRegion(magicPotAftSheet, 70, 147, 69, 48);
			magicPotAftFrames[6] = new TextureRegion(magicPotAftSheet, 140, 98, 69, 48);
			magicPotAftFrames[7] = new TextureRegion(magicPotAftSheet, 70, 196, 69, 48);
			magicPotAftFrames[8] = new TextureRegion(magicPotAftSheet, 140, 147, 69, 48);
			magicPotAftFrames[9] = new TextureRegion(magicPotAftSheet, 0, 49, 69, 48);
			magicPotAftFrames[10] = new TextureRegion(magicPotAftSheet, 70, 0, 69, 48);
			magicPotAftFrames[11] = new TextureRegion(magicPotAftSheet, 70, 49, 69, 48);
			magicPotAftFrames[12] = new TextureRegion(magicPotAftSheet, 140, 0, 69, 48);
			magicPotAftFrames[13] = new TextureRegion(magicPotAftSheet, 140, 49, 69, 48);
		}		
		
		public TextureRegion background = new TextureRegion(gameSheet, 0, 0, 720, 480);
		public TextureRegion normalBar = new TextureRegion(gameSheet, 144, 481, 69, 10);
		public TextureRegion spikyBar = new TextureRegion(gameSheet, 0, 481, 72, 12);
		public TextureRegion stickyBar = new TextureRegion(gameSheet, 73, 481, 70, 14);
		public TextureRegion bouncyBar = new TextureRegion(gameSheet, 721, 0, 68, 15);
		public TextureRegion iBouncyBar = new TextureRegion(gameSheet, 214, 481, 68, 19);
		public TextureRegion orange = new TextureRegion(gameSheet, 721, 59, 31, 30);
		public TextureRegion strawberry = new TextureRegion(gameSheet, 753, 28, 31, 30);
		public TextureRegion apple = new TextureRegion(gameSheet, 721, 28, 31, 30);
	}
	
	private final class GameContactListener implements ContactListener {

		@Override
		public void beginContact(Contact contact) {
			if(contact.getFixtureA().getFilterData().categoryBits == 0x0011) {
				contact.getFixtureB().getBody().setLinearVelocity(0, 0);
			}
			else if(contact.getFixtureA().getFilterData().categoryBits == 0x0010) {
				if(testFruitMagicPotOverlap()) {
					currentFruit.removeFruitFromStage();
					currentFruit = null;
				}
			}
		}

		@Override
		public void endContact(Contact contact) {
						
		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
			
		}
		
	}
	GameContactListener myContactListener = new GameContactListener();
	
	private Menu menu;
	private FruitRage localGameRef;
	private static World world;
	private static Stage localStage;
	private Regions tRegions;
	private Image background;
	private InputMultiplexer multiplexer;
	
	private float stateTime = 0;
	public final static float RATIO = 80.0f;	
	private static Body groundBody;
	private static BodyDef groundBodyDef;
	private static PolygonShape groundPolygon;
	private static FixtureDef groundFixtureDef;
	
	private Fruit currentFruit = null;
	
	private ArrayList<Fruit> fruitArr;
	private ArrayList<NormalBar> normalBarArr;
	private ArrayList<SpikyBar> spikyBarArr;
	private ArrayList<StickyBar> stickyBarArr;
	private ArrayList<BouncyBar> bouncyBarArr;
	private ArrayList<IBouncyBar> iBouncyBarArr;	
	private ArrayList<MagicPot> magicPotArr;
	
	
	public LevelCreator(FruitRage passed) {
		localGameRef = passed;
		world = new World(new Vector2(0, -10f), true);
		
		groundPolygon = new PolygonShape();
		groundPolygon.setAsBox((FruitRage.STAGE_W/RATIO) / 2, (1)/RATIO / 2);
		
		groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyType.StaticBody;
		groundBodyDef.position.set((FruitRage.STAGE_W/RATIO) / 2, (FruitRage.STAGE_H * 13/100)/RATIO);
		
		groundFixtureDef = new FixtureDef();
		groundFixtureDef.shape = groundPolygon;
		groundFixtureDef.density = 1000;		
	}
	
	@Override
	public void show() {
		localStage = new Stage(FruitRage.STAGE_W, FruitRage.STAGE_H, false);
		menu = new Menu();
		tRegions = new Regions();
		background = new Image(tRegions.background);
		
		fruitArr = new ArrayList<Fruit>();
		normalBarArr = new ArrayList<NormalBar>();	
		spikyBarArr = new ArrayList<SpikyBar>();
		stickyBarArr = new ArrayList<StickyBar>();
		bouncyBarArr = new ArrayList<BouncyBar>();
		iBouncyBarArr = new ArrayList<IBouncyBar>();
		magicPotArr = new ArrayList<MagicPot>();
		
		localStage.addActor(background);
		menu.addMenuToStage();
		populateFruitArray();		
		
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(0, localStage);
		multiplexer.addProcessor(1, localThis);		
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);		
		world.step(Gdx.graphics.getDeltaTime(), 8, 4);		
		stateTime = stateTime + Gdx.graphics.getDeltaTime();
		
		localStage.draw();	
		localStage.act(stateTime);
		
		//Gdx.app.log(String.valueOf(localStage.getActors().size()), "Actor quan");
	}

	@Override
	public void resize(int width, int height) {
		localStage.setViewport(FruitRage.STAGE_W, FruitRage.STAGE_H, false);
		localStage.getCamera().position.set(FruitRage.STAGE_W/2, FruitRage.STAGE_H/2, 0);
		
	}
	
	private void populateFruitArray() {
		fruitArr.add(new Fruit(tRegions.orange, 15, FRUIT_NAME.ORANGE, 300, 300, RATIO, world));
		fruitArr.add(new Fruit(tRegions.apple, 15, FRUIT_NAME.APPLE, 300, 300, RATIO, world));
		fruitArr.add(new Fruit(tRegions.strawberry, 15, FRUIT_NAME.STRAWBERRY, 300, 300, RATIO, world));
	}
	
	private void addMagicPot() {
		MagicPot toAdd = new MagicPot(tRegions.magicPotBefFrames, tRegions.magicPotAftFrames, 0, BAR_NAME.MAGIC_POT, FruitRage.STAGE_W/2, FruitRage.STAGE_H/2, RATIO, world);
		toAdd.addBarToStage(localStage);
		magicPotArr.add(toAdd);
	}
	
	private void addIBouncyBar() {
		IBouncyBar toAdd = new IBouncyBar(tRegions.iBouncyBar, 0, BAR_NAME.INSANELY_BOUNCY_BAR, FruitRage.STAGE_W/2, FruitRage.STAGE_H/2, RATIO, world);
		toAdd.addBarToStage(localStage);
		iBouncyBarArr.add(toAdd);
	}
	
	private void addBouncyBar() {
		BouncyBar toAdd = new BouncyBar(tRegions.bouncyBar, 0, BAR_NAME.BOUNCY_BAR, FruitRage.STAGE_W/2, FruitRage.STAGE_H/2, RATIO, world);
		toAdd.addBarToStage(localStage);
		bouncyBarArr.add(toAdd);
	}
	
	private void addStickyBar() {
		StickyBar toAdd = new StickyBar(tRegions.stickyBar, 0, BAR_NAME.STICKY_BAR, FruitRage.STAGE_W/2, FruitRage.STAGE_H/2, RATIO, world);
		toAdd.addBarToStage(localStage);
		stickyBarArr.add(toAdd);
	}
	
	private void addSpikyBar() {
		SpikyBar toAdd = new SpikyBar(tRegions.spikyBar, 0, BAR_NAME.SPIKY_BAR, FruitRage.STAGE_W/2, FruitRage.STAGE_H/2, RATIO, world);
		toAdd.addBarToStage(localStage);
		spikyBarArr.add(toAdd);
	}
	
	private void addNormalBar() {
		NormalBar toAdd = new NormalBar(tRegions.normalBar, 0, BAR_NAME.NORMAL_BAR, FruitRage.STAGE_W/2, FruitRage.STAGE_H/2, RATIO, world);
		toAdd.addBarToStage(localStage);
		normalBarArr.add(toAdd);
	}
	
	private void createBox2dWorld() {
		groundBody = world.createBody(groundBodyDef);
		groundBody.createFixture(groundFixtureDef);
		
		currentFruit = (Fruit) fruitArr.get(0).clone();
		if(currentFruit != null)
			currentFruit.addFruitToStage(localStage);
		
		Iterator i = null;
		i = normalBarArr.iterator();
		while(i.hasNext()) {
			NormalBar currentNBar = (NormalBar) i.next();
			currentNBar.createBarBody();
		}
		i = spikyBarArr.iterator();
		while(i.hasNext()) {
			SpikyBar currentSpBar = (SpikyBar) i.next();
			currentSpBar.createBody();
		}
		i = stickyBarArr.iterator();
		while(i.hasNext()) {
			StickyBar currentBar = (StickyBar) i.next();
			currentBar.createBarBody();
		}
		i = bouncyBarArr.iterator();
		while(i.hasNext()) {
			BouncyBar currentBar = (BouncyBar) i.next();
			currentBar.createBody();
		}
		i = iBouncyBarArr.iterator();
		while(i.hasNext()) {
			IBouncyBar currentBar = (IBouncyBar) i.next();
			currentBar.createBarBody();
		}
		i = magicPotArr.iterator();
		while(i.hasNext()) {
			MagicPot currentBar = (MagicPot) i.next();
			currentBar.createBarBody();
		}
		
		world.setContactListener(myContactListener);
	}
	
	private void destroyBox2dWorld() {
		world.destroyBody(groundBody);
		if(currentFruit != null)
			currentFruit.removeFruitFromStage();
		currentFruit = null;
		
		Iterator i = null;
		i = normalBarArr.iterator();
		while(i.hasNext()) {
			NormalBar currentNBar = (NormalBar) i.next();
			currentNBar.destroyBarBody();
		}
		i = spikyBarArr.iterator();
		while(i.hasNext()) {
			SpikyBar currentSpBar = (SpikyBar) i.next();
			currentSpBar.destroyBarBody();
		}
		i = stickyBarArr.iterator();
		while(i.hasNext()) {
			StickyBar currentBar = (StickyBar) i.next();
			currentBar.destroyBarBody();
		}
		i = bouncyBarArr.iterator();
		while(i.hasNext()) {
			BouncyBar currentBar = (BouncyBar) i.next();
			currentBar.destroyBarBody();
		}
		i = iBouncyBarArr.iterator();
		while(i.hasNext()) {
			IBouncyBar currentBar = (IBouncyBar) i.next();
			currentBar.destroyBarBody();
		}
		i = magicPotArr.iterator();
		while(i.hasNext()) {
			MagicPot currentBar = (MagicPot) i.next();
			currentBar.destroyBarBody();
		}
	}
	
	
	/** COMPATIBLE This method checks whether the currentFruit is in a particular MagicPot
	 * and returns true if the fruit is in a particular MagicPot. */
	private boolean testFruitMagicPotOverlap() {
		if(currentFruit != null) {
			Vector2 fruitCoor = currentFruit.getFruitCenter();
			Iterator i = magicPotArr.iterator();
			while(i.hasNext()) {
				MagicPot current = (MagicPot) i.next();
				float[] vertices = new float[8]; 
				vertices[0] = current.x;
				vertices[1] = current.y;
				vertices[2] = current.x + current.width;
				vertices[3] = current.y;
				vertices[4] = current.x + current.width;
				vertices[5] = current.y + current.height;
				vertices[6] = current.x;
				vertices[7] = current.y + current.height;
				Polygon magicPolygon = new Polygon(vertices);	
				magicPolygon.setOrigin(current.x, current.y);
				magicPolygon.rotate(current.rotation);
				if(magicPolygon.contains(fruitCoor.x, fruitCoor.y)) {
					current.playMagicAnimation();
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		Vector2 point = new Vector2();
		Iterator i = normalBarArr.iterator();
		while(i.hasNext()) {
			NormalBar current = (NormalBar) i.next();
			localStage.toStageCoordinates(x, y, point);
			current.toLocalCoordinates(point);
			if(current.hit(point.x, point.y) != null) {
				menu.currentlySelected = current;
				menu.switchBulbState();
				return true;
			}
			point.set(0, 0);
		}
		i = spikyBarArr.iterator();
		while(i.hasNext()) {
			SpikyBar current = (SpikyBar) i.next();
			localStage.toStageCoordinates(x, y, point);
			current.toLocalCoordinates(point);
			if(current.hit(point.x, point.y) != null) {
				menu.currentlySelected = current;
				menu.switchBulbState();
				return true;
			}
			point.set(0, 0);
		}
		i = stickyBarArr.iterator();
		while(i.hasNext()) {
			StickyBar current = (StickyBar) i.next();
			localStage.toStageCoordinates(x, y, point);
			current.toLocalCoordinates(point);
			if(current.hit(point.x, point.y) != null) {
				menu.currentlySelected = current;
				menu.switchBulbState();
				return true;
			}
			point.set(0, 0);
		}
		i = bouncyBarArr.iterator();
		while(i.hasNext()) {
			BouncyBar current = (BouncyBar) i.next();
			localStage.toStageCoordinates(x, y, point);
			current.toLocalCoordinates(point);
			if(current.hit(point.x, point.y) != null) {
				menu.currentlySelected = current;
				menu.switchBulbState();
				return true;
			}
			point.set(0, 0);
		}
		i = iBouncyBarArr.iterator();
		while(i.hasNext()) {
			IBouncyBar current = (IBouncyBar) i.next();
			localStage.toStageCoordinates(x, y, point);
			current.toLocalCoordinates(point);
			if(current.hit(point.x, point.y) != null) {
				menu.currentlySelected = current;
				menu.switchBulbState();
				return true;
			}
			point.set(0, 0);
		}
		i = magicPotArr.iterator();
		while(i.hasNext()) {
			MagicPot current = (MagicPot) i.next();
			localStage.toStageCoordinates(x, y, point);
			current.toLocalCoordinates(point);
			if(current.hit(point.x, point.y) != null) {
				menu.currentlySelected = current;
				menu.switchBulbState();
				return true;
			}
			point.set(0, 0);
		}
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		Vector2 point = new Vector2();
		Vector2 sliderPoint = new Vector2();
		localStage.toStageCoordinates(x, y, point);
		localStage.toStageCoordinates(x, y, sliderPoint);
		menu.slider.toLocalCoordinates(sliderPoint);
		if(!menu.slider.isDragging() && menu.currentlySelected != null) {
			menu.currentlySelected.changeBarPosition(point.x, point.y);
		}
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
