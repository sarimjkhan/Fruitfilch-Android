package com.rps.fruitfilch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.rps.fruitfilch.Assets.FILES;
import com.rps.fruitfilch.Scoring.SCORE;
import com.rps.fruitfilch.bars.BouncyBar;
import com.rps.fruitfilch.bars.Box;
import com.rps.fruitfilch.bars.IBouncyBar;
import com.rps.fruitfilch.bars.MagicPot;
import com.rps.fruitfilch.bars.NormalBar;
import com.rps.fruitfilch.bars.SpikyBar;
import com.rps.fruitfilch.bars.StickyBar;
import com.rps.fruitfilch.bars.Stone;
import com.rps.fruitfilch.gameaddons.Annotation;
import com.rps.fruitfilch.worlds.worldone.WorldOne;

public abstract class Level implements Screen, InputProcessor {
	
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
		MAGIC_POT,
		STAR,
		BOX_OBS,
		STONE_OBS,
		CATCHER;
	}
	
	
	private static boolean spikeStrike = false;
	private final class GameContactListener implements ContactListener {
		
		private boolean fruitPottedAtLeastOnce = false;
		private boolean fruitPotted = false;
		private ArrayList <Star> starMap = new ArrayList<Star>();

		@Override
		public void beginContact(Contact contact) {
			//Sticky
			if(contact.getFixtureA().getFilterData().categoryBits == 0x0011
					&& contact.getFixtureB().getFilterData().categoryBits == 0x0101) {
				contact.getFixtureB().getBody().setLinearVelocity(0, 0);
					fruitSluggySound.play();
			}
			//Insanely Bouncy
			else if(contact.getFixtureA().getFilterData().categoryBits == 0x1010
					&& contact.getFixtureB().getFilterData().categoryBits == 0x0101) {
				fruitInsaneSound.play();
			}
				
			//Star
			else if(contact.getFixtureA().getFilterData().categoryBits == 0x0100
					&& contact.getFixtureB().getFilterData().categoryBits == 0x0101) {
				Star tmp = (Star) contact.getFixtureA().getBody().getUserData();
				if(!tmp.isHit() && currentFruit != null && !currentFruit.isGonnaExplode()) {
					starMap.add(tmp);
					tmp.hitStar();
					fruitStarSound.play();
					wasStarTakenWithoutPot = true;
				}
			}
			//Spiky
			else if(contact.getFixtureA().getFilterData().categoryBits == 0x0110
					&& contact.getFixtureB().getFilterData().categoryBits == 0x0101) {
				if(currentFruit != null) {
					currentFruit.removeWithExplosion();
					fruitSpikySound.play();
					if(fruitIndex >= 0)
						topBar.nextFruit.setRegion(fruitArr.get(fruitIndex).getRegion());
					else
						topBar.nextFruit.setRegion(topBar.qMark);
					currentFruit = null;
					spikeStrike = true;
					//starMap.clear();
				}
			}
			//Magic Pot
			else if(contact.getFixtureA().getFilterData().categoryBits == 0x0010
					&& contact.getFixtureB().getFilterData().categoryBits == 0x0101) {				
				if(currentFruit != null) {
					ArrayList<Fixture> temp = contact.getFixtureA().getBody().getFixtureList();
					for(Fixture current : temp) {
						if(current.getFilterData().categoryBits == 0x1001) {
							if(current.testPoint(contact.getFixtureB().getBody().getPosition())) {
								MagicPot tMPot = (MagicPot) contact.getFixtureA().getBody().getUserData();
								tMPot.playMagicAnimation();
								fruitMagicPotSound.play();
								currentFruit.removeWithoutExplosion();
								if(fruitIndex >= 0)
									topBar.nextFruit.setRegion(fruitArr.get(fruitIndex).getRegion());
								else
									topBar.nextFruit.setRegion(topBar.qMark);
								currentFruit = null;
								if(!starMap.isEmpty()) {
									scoring.add(SCORE.MAGIC_POT);
									for(Star taken : starMap) {
										starArr.remove(taken);
										scoring.add(SCORE.STAR);
									}
								} else if(starMap.isEmpty()) { //Code added to add score even without taking stars
									scoring.add(SCORE.MAGIC_POT);
								}
								fruitPotted = true;
								fruitPottedAtLeastOnce = true;
								//starMap.clear();
								wasStarTakenWithoutPot = false;
							}
						}
					}
				}
			}
			//POT
			else if(contact.getFixtureA().getFilterData().categoryBits == 0x0111
					&& contact.getFixtureB().getFilterData().categoryBits == 0x0101) {
				/*Vector2 tmp = contact.getFixtureB().getBody().getLinearVelocity();
				tmp.x = 0;
				tmp.y = 0;*/
				contact.getFixtureB().getBody().setLinearVelocity(0, 0);
				if(currentFruit != null) {
					ArrayList<Fixture> temp = contact.getFixtureA().getBody().getFixtureList();
					for(Fixture current : temp) {
						if(current.getFilterData().categoryBits == 0x1000) {
							if(current.testPoint(contact.getFixtureB().getBody().getPosition())) {
								currentFruit.removeWithoutExplosion();
								//if(!starMap.isEmpty())
								fruitSackSound.play();
								if(fruitIndex >= 0)
									topBar.nextFruit.setRegion(fruitArr.get(fruitIndex).getRegion());
								else
									topBar.nextFruit.setRegion(topBar.qMark);
								currentFruit = null;
								if(!starMap.isEmpty()) {
									scoring.add(SCORE.POT);									
									for(Star taken : starMap) {
										starArr.remove(taken);
										scoring.add(SCORE.STAR);
									}
								} else if(starMap.isEmpty()) { //Code added to add score even without taking stars
									scoring.add(SCORE.POT);
								}
								fruitPotted = true;
								fruitPottedAtLeastOnce = true;
								//starMap.clear();
								wasStarTakenWithoutPot = false;
							}
						}
					}
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
	
	//public static final int STAR_TH_1 = 999, STAR_TH_2 = 1999, STAR_TH_3 = 2999;
	private Sound fruitStarSound, levelFailedSound, fruitInsaneSound, fruitMagicPotSound, fruitSackSound, fruitSluggySound, fruitSpikySound, fruitThrownSound;
	
	
	private FruitRageGame localGameRef;
	protected World world;
	protected Stage localStage;
	protected Regions tRegions;
	private Image background;
	private Image foreground;
	private Image scenery;
	private Thrower thrower;
	private Catcher catcher;
	protected Image shotBlob;
	private Trajectory traj;
	private InputMultiplexer multiplexer;
	protected final String levelConfigString;
	protected Scoring scoring;
	protected Rectangle playRegion;
	protected Image nextFruitTopBar;
	protected int fruitIndex;
	protected TopBar topBar;
	protected PauseMenu pauseMenu;
	protected LevelClearedMenu levelClearedMenu;
	protected LevelFailedMenu levelFailedMenu;
	protected boolean isPaused = false;
	protected boolean levelClearedOnStage = false;
	/*protected int starTakenPrev = 0;
	protected int starTakenNow = 0;*/
	protected boolean wasStarTakenWithoutPot = false;
	protected boolean skipAnnoTouched = false;
	protected ArrayList<Annotation> annotations;
	
	private float stateTime = 0;
	public final static float RATIO = 80.0f;	
	private Body groundBody;
	private BodyDef groundBodyDef;
	private PolygonShape groundPolygon;
	private FixtureDef groundFixtureDef;
	
	protected Fruit currentFruit = null;
	
	protected ArrayList<Fruit> fruitArr;
	protected ArrayList<Fruit> fruitArrBackUp;
	private ArrayList<NormalBar> normalBarArr;
	private ArrayList<SpikyBar> spikyBarArr;
	private ArrayList<StickyBar> stickyBarArr;
	private ArrayList<BouncyBar> bouncyBarArr;
	private ArrayList<IBouncyBar> iBouncyBarArr;	
	private ArrayList<MagicPot> magicPotArr;
	private ArrayList<Star> starArr;
	private ArrayList<Box> boxArr;
	private ArrayList<Stone> stoneArr;
	
	protected float blobX, blobY, blobStageOriginX, blobStageOriginY;
	private Circle shotArea;
	protected boolean shotBlobTouched = false;
	protected boolean waitingForUserInput = true;
	
	protected int levelNumber;
	
	public Level(FruitRageGame passed, String levelConfigString, int levelNumber) {
		localGameRef = passed;		
		this.levelConfigString = levelConfigString;
		this.levelNumber = levelNumber;
		annotations = new ArrayList<Annotation>();
	}
	
	Music gameMusic;
	
	@Override
	public void show() {
		Gdx.app.log("Show initialized", "M.RAFAY");
		world = new World(new Vector2(0, -10f), true);
		
		groundPolygon = new PolygonShape();
		groundPolygon.setAsBox((FruitRageGame.getSTAGE_W()/RATIO) / 2, (1)/RATIO / 2);
		
		groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyType.StaticBody;
		groundBodyDef.position.set((FruitRageGame.getSTAGE_W()/RATIO) / 2, 62.4f / RATIO); //Hard Coded value
		
		groundFixtureDef = new FixtureDef();
		groundFixtureDef.shape = groundPolygon;
		groundFixtureDef.density = 1000;	
		
		localStage = new Stage(FruitRageGame.getSTAGE_W(), FruitRageGame.getSTAGE_H(), false);
		tRegions = new Regions();
		background = new Image(tRegions.stageBackgroundW1);
		foreground = new Image(tRegions.stageForegroundW1);
		scenery = new Image(tRegions.bgSceneryW1);
		thrower = new Thrower(tRegions.thrower, tRegions.throwerNormHand, tRegions.throwerThrowHand, 145, 67);
		catcher = new Catcher(tRegions.catcher, BAR_NAME.CATCHER, 650, 62, RATIO, world);
		blobX = thrower.hand.x + 30;
		blobY = thrower.hand.y + 30;
		shotBlob = new Image(tRegions.shotBlob);
		shotBlob.x = blobX;
		shotBlob.y = blobY;
		shotArea = new Circle(shotBlob.x + shotBlob.width/2, shotBlob.y + shotBlob.height/2, 80);
		shotBlob.originX = shotBlob.width / 2;
		shotBlob.originY = shotBlob.height / 2;
		blobStageOriginX = blobX + shotBlob.width / 2;
		blobStageOriginY = blobY + shotBlob.height / 2;		
		
		fruitArr = new ArrayList<Fruit>();
		normalBarArr = new ArrayList<NormalBar>();	
		spikyBarArr = new ArrayList<SpikyBar>();
		stickyBarArr = new ArrayList<StickyBar>();
		bouncyBarArr = new ArrayList<BouncyBar>();
		iBouncyBarArr = new ArrayList<IBouncyBar>();
		magicPotArr = new ArrayList<MagicPot>();	
		starArr = new ArrayList<Star>();
		boxArr = new ArrayList<Box>();
		stoneArr = new ArrayList<Stone>();
		try {
			populateActorArrays();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		localStage.addActor(background);
		localStage.addActor(scenery);
		localStage.addActor(foreground);	
		thrower.addToStage(localStage);
		localStage.addActor(shotBlob);
		for(Annotation current : annotations) {
			current.addToStage(localStage);
		}
		localStage.addActor(catcher);
		try {
			populateStage();
			populateFruitArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		fruitArrBackUp = new ArrayList<Fruit>();			
		
		fruitIndex = fruitArr.size() - 1;	
		
		traj = new Trajectory(localStage, tRegions.trajectoryDot, world.getGravity());
		scoring = new Scoring();
		topBar = new TopBar();
		topBar.addToStage();
		pauseMenu = new PauseMenu();
		levelClearedMenu = new LevelClearedMenu();	
		levelFailedMenu = new LevelFailedMenu();
		
		float playRegionX = (localStage.width() > 854) ? 860 : 854;
		playRegion = new Rectangle(0, 0, playRegionX, localStage.height() + 1000);
		
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(this);
		multiplexer.addProcessor(localStage);
		Gdx.input.setInputProcessor(multiplexer);		
		world.setContactListener(myContactListener);
		Gdx.app.log("Show finished", "M.RAFAY");
		
		gameMusic = localGameRef.assets.getMusic(FILES.LEVEL_BACKGROUND_M);
		gameMusic.setLooping(true);
		gameMusic.setVolume(0.25f);
		gameMusic.play();
		fruitStarSound = localGameRef.assets.getSound(FILES.F_STAR_BOUNCE_S);
		levelFailedSound = localGameRef.assets.getSound(FILES.LEVEL_FAILED_S);
		fruitInsaneSound = localGameRef.assets.getSound(FILES.F_INSANE_BOUNCE_S);
		fruitMagicPotSound = localGameRef.assets.getSound(FILES.F_MAGICPOT_S);
		fruitSackSound = localGameRef.assets.getSound(FILES.F_SACK_S);
		fruitSluggySound = localGameRef.assets.getSound(FILES.F_SLUGGY_BOUNCE_S);
		fruitSpikySound = localGameRef.assets.getSound(FILES.F_SPIKY_BOUNCE_S);
		fruitThrownSound = localGameRef.assets.getSound(FILES.F_THROWN_S);
	}
	
	private boolean isSafeToCreateNewStars = false;
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		if(!isPaused && !levelClearedMenu.menuOnStage) {
			world.step(Gdx.graphics.getDeltaTime(), 8, 4);		
			stateTime = stateTime + Gdx.graphics.getDeltaTime();		
			scoring.update();
			localStage.act(stateTime);				
			for(Star star : myContactListener.starMap) { if(star.isCompletelyRemoved()) isSafeToCreateNewStars = true;
				else {
					isSafeToCreateNewStars = false; 
					break;
				}
			}			
			if(myContactListener.starMap.isEmpty()) isSafeToCreateNewStars = true;
			
			//main condition 1
			if((currentFruit != null && currentFruit.getBodyStatus() != null && isSafeToCreateNewStars && !skipCurrentAttempt) /*||
					(currentFruit != null && currentFruit.getBodyStatus() != null && myContactListener.starMap.isEmpty()) && !skipCurrentAttempt*/) {				
				if(currentFruit.getFruitCenterInStageCoor().x > playRegion.width ||
						currentFruit.getFruitCenterInStageCoor().y > playRegion.height ||
						currentFruit.getFruitCenterInStageCoor().x < playRegion.x ||
						currentFruit.getFruitCenterInStageCoor().y < playRegion.y) {
					currentFruit.removeWithoutExplosion();
					waitingForUserInput = true;
					currentFruit = null;
					myContactListener.starMap.clear();
					if(fruitIndex >= 0)
						topBar.nextFruit.setRegion(fruitArr.get(fruitIndex).getRegion());
					else
						topBar.nextFruit.setRegion(topBar.qMark);
					if(wasStarTakenWithoutPot) {
						for(Star taken : starArr) {
							taken.addToStage(localStage);
							if(taken.getBody() == null) taken.createStarBody();
						}
						wasStarTakenWithoutPot = false;
					}
					Gdx.app.log("Level Render", "main condition 1");
				}
			}
			//main condition 2
			else if((currentFruit != null && currentFruit.getBodyStatus() != null && isSafeToCreateNewStars && skipCurrentAttempt)/* ||
					((currentFruit != null && currentFruit.getBodyStatus() != null && myContactListener.starMap.isEmpty() && skipCurrentAttempt))*/) {
				Gdx.app.log("Level Render", "main condition 2");
				currentFruit.removeWithoutExplosion();
				waitingForUserInput = true;
				currentFruit = null;
				myContactListener.starMap.clear();
				if(fruitIndex >= 0)
					topBar.nextFruit.setRegion(fruitArr.get(fruitIndex).getRegion());
				else
					topBar.nextFruit.setRegion(topBar.qMark);
				if(wasStarTakenWithoutPot) {
					for(Star taken : starArr) {
						taken.addToStage(localStage);
						if(taken.getBody() == null) taken.createStarBody();
					}
					wasStarTakenWithoutPot = false;
				}
				skipCurrentAttempt = false;
			}
			//main condition 3
			/*else if(currentFruit == null && isSafeToCreateNewStars && !skipCurrentAttempt ||
					currentFruit == null && myContactListener.starMap.isEmpty() && skipCurrentAttempt) {
				Gdx.app.log("Level Render", "main condition 3");
				myContactListener.starMap.clear();
				if(fruitIndex > 0) fruitArr.remove(fruitIndex--);
				if(fruitIndex >= 0)
					topBar.nextFruit.setRegion(fruitArr.get(fruitIndex).getRegion());
				else
					topBar.nextFruit.setRegion(topBar.qMark);
				if(wasStarTakenWithoutPot) {
					for(Star taken : starArr) {
						Gdx.app.log("Idhr tou aya thaa main!!", "497");
						taken.addToStage(localStage);
						if(taken.getBody() == null) taken.createStarBody();
					}
					wasStarTakenWithoutPot = false;
				}
				skipCurrentAttempt = false;
			}*/
			//main condition 4
			else if(currentFruit == null && spikeStrike && isSafeToCreateNewStars && !skipCurrentAttempt) {
				Gdx.app.log("Level Render", "main condition 4");
				myContactListener.starMap.clear();
				if(fruitIndex >= 0)
					topBar.nextFruit.setRegion(fruitArr.get(fruitIndex).getRegion());
				else
					topBar.nextFruit.setRegion(topBar.qMark);
				waitingForUserInput = true;
				if(wasStarTakenWithoutPot) {
					for(Star taken : starArr) {
						Gdx.app.log("Idhr tou aya thaa main!!", "493");
						taken.addToStage(localStage);
						if(taken.getBody() == null) taken.createStarBody();
					}
					wasStarTakenWithoutPot = false;					
				}
				spikeStrike = false;
			}
			//main condition 5
			else if(currentFruit == null && myContactListener.fruitPotted && isSafeToCreateNewStars && !skipCurrentAttempt) {
				Gdx.app.log("Level Render", "main condition 5");
				myContactListener.starMap.clear();
				if(fruitIndex >= 0)
					topBar.nextFruit.setRegion(fruitArr.get(fruitIndex).getRegion());
				else
					topBar.nextFruit.setRegion(topBar.qMark);
				waitingForUserInput = true;
				myContactListener.fruitPotted = false;
			}
		}
		localStage.draw();
		localStage.getSpriteBatch().begin();
		topBar.topBarScore.draw(localStage.getSpriteBatch(), "Score: " + String.valueOf(scoring.getScore()), localStage.width() - 200, localStage.height() - 15);		
		/*topBar.topBarLog1.draw(localStage.getSpriteBatch(), "Log:" + String.valueOf(localStage.getActors().size()) + ", " +
				String.valueOf(myContactListener.starMap.size()) + ", " + fruitArr.size(), localStage.width() - 200,  localStage.height() - 40);*/		
		/*topBar.topBarLog2.drawMultiLine(localStage.getSpriteBatch(), "Heap:" + String.valueOf(Gdx.app.getJavaHeap()/1048576) + " MB\nNHeap:" + String.valueOf(Gdx.app.getNativeHeap()/1048576) + " MB", 
				localStage.width() - 200, localStage.height() - 65);*/
		localStage.getSpriteBatch().end();
		if(isLevelPassed() && !levelClearedOnStage) {	
			int tmpScore = scoring.getFinalScore();
			int tmpStars = 0;
			tmpStars = 3 - starArr.size();
			/*if(tmpScore > STAR_TH_1) tmpStars = 1;
			if(tmpScore > STAR_TH_2) tmpStars = 2;
			if(tmpScore > STAR_TH_3) tmpStars = 3;*/
			if(FruitRageGame.sqlHelperLibgdx != null)
				FruitRageGame.sqlHelperLibgdx.updateLevelScore(levelNumber, tmpScore, tmpStars);
			levelClearedMenu.addToStage();
			levelClearedOnStage = true;
		}
		if(levelClearedOnStage) { levelClearedMenu.renderScore(); }
		else if(isLevelFailed()) {
			levelFailedMenu.addToStage();
		}
	}
	
	protected boolean skipCurrentAttempt = false;
	
	@Override
	public void resize(int width, int height) {
		localStage.setViewport(FruitRageGame.getSTAGE_W(), FruitRageGame.getSTAGE_H(), false);
		localStage.getCamera().position.set(FruitRageGame.getSTAGE_W()/2, FruitRageGame.getSTAGE_H()/2, 0);		
	}
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		isPaused = true;
	}

	@Override
	public void resume() {
		isPaused = false;
		
	}

	@Override
	public void dispose() {		
		world.dispose();
		localStage.dispose();
		gameMusic.stop();
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.BACK) {
			if(!isPaused && (!levelClearedMenu.menuOnStage || !levelFailedMenu.menuOnStage)) {
				pauseGame();
				localGameRef.buttonClick.play();
				pauseMenu.addToStage();
				isPaused = true;
			} else if(isPaused) {
				isPaused = false;
				pauseMenu.removeFromStage();
				localGameRef.buttonClick.play();
				resumeGame();
			}
		}
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

	private Vector2 hitCoor = new Vector2();
	//private int touchX, touchY;
	
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		hitCoor.set(0, 0);
		localStage.toStageCoordinates(x, y, hitCoor);
		if(shotArea.contains(hitCoor) && pointer == 0 && !fruitArr.isEmpty() && !isPaused && !levelClearedMenu.menuOnStage) {
			this.shotBlobTouched = true;			
		}
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if(shotBlobTouched && pointer == 0) {
			shotBlobTouched = false;
			//shotTestPoint.set(0, 0);
			//limShotTestPoint.set(0, 0);
			//localStage.toStageCoordinates(x, y, shotTestPoint);
			//shotBlob.x = shotTestPoint.x - shotBlob.width/2;
			//if(shotTestPoint.y > BLOB_BASE_Y) 
				//shotBlob.y = shotTestPoint.y - shotBlob.height/2;
			if(currentFruit == null && !fruitArr.isEmpty() && myContactListener.starMap.isEmpty()) {
				fruitThrownSound.play();
				currentFruit = fruitArr.remove(fruitIndex--);
				topBar.nextFruit.setRegion(topBar.qMark);
				currentFruit.addFruitToStage(localStage);
				waitingForUserInput = false;;
				//limShotTestPoint.set(shotBlob.x + shotBlob.width/2, shotBlob.y + shotBlob.height/2);
				Gdx.app.log("Touch Up", "Fruit Released");
				currentFruit.setLinearVelocity(getFruitStartVelocity(limShotTestPoint));				
				catcher.addToStage(localStage);
				for(MagicPot current : magicPotArr) {
					current.addToStage(localStage);
				}
			}			
			shotBlob.x = blobX;
			shotBlob.y = blobY;
			return true;
		}
		return false;
	}
	
	private Vector2 shotTestPoint = new Vector2();
	private Vector2 tmpStartPos = new Vector2();
	private Vector2  limShotTestPoint = new Vector2();
	private final float BLOB_BASE_Y = 70;

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		shotTestPoint.set(0, 0);
		tmpStartPos.set(0, 0);
		limShotTestPoint.set(0, 0);
		if(shotBlobTouched && pointer == 0) {
			localStage.toStageCoordinates(x, y, shotTestPoint);
			shotBlob.x = shotTestPoint.x - shotBlob.width/2;
			if(shotTestPoint.y > BLOB_BASE_Y)
				shotBlob.y = shotTestPoint.y - shotBlob.height/2;
			tmpStartPos.set(blobStageOriginX, blobStageOriginY);
			limShotTestPoint.set(shotBlob.x + shotBlob.width/2, shotBlob.y + shotBlob.height/2);
			traj.drawTrajectory(tmpStartPos, getFruitStartVelocity(limShotTestPoint));
			return true;
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
	
	private void populateStage() {
		groundBody = world.createBody(groundBodyDef);
		groundBody.createFixture(groundFixtureDef);
		
		catcher.createBody();
		
		ArrayList<Bar> barCollection = new ArrayList<Bar>();
		barCollection.addAll(normalBarArr);
		barCollection.addAll(bouncyBarArr);
		barCollection.addAll(iBouncyBarArr);
		barCollection.addAll(stickyBarArr);
		barCollection.addAll(spikyBarArr);
		barCollection.addAll(magicPotArr);
		barCollection.addAll(starArr);
		barCollection.addAll(boxArr);
		barCollection.addAll(stoneArr);
		
		Iterator<Bar> i = barCollection.iterator();
		while(i.hasNext()) {
			i.next().addToStage(localStage);
		}		
		i = null;
		i = barCollection.iterator();
		while(i.hasNext()) {
			Bar currentBar = i.next();
			if(currentBar instanceof NormalBar) {
				NormalBar currentNBar = (NormalBar) currentBar;
				currentNBar.createBarBody();
			}
			else if(currentBar instanceof SpikyBar) {
				SpikyBar currentSpBar = (SpikyBar) currentBar;
				currentSpBar.createBody();
			}
			else if(currentBar instanceof StickyBar) {
				StickyBar currentStBar = (StickyBar) currentBar;
				currentStBar.createBarBody();
			}
			else if(currentBar instanceof BouncyBar) {
				BouncyBar currentBounBar = (BouncyBar) currentBar;
				currentBounBar.createBody();
			}
			else if(currentBar instanceof IBouncyBar) {
				IBouncyBar currentIBounBar = (IBouncyBar) currentBar;
				currentIBounBar.createBarBody();
			}
			else if(currentBar instanceof MagicPot) {
				MagicPot currentMBar = (MagicPot) currentBar;
				currentMBar.createBarBody();
			}
			else if(currentBar instanceof Star) {
				Star currentStarBar = (Star) currentBar;
				currentStarBar.createStarBody();
			}
			else if(currentBar instanceof Box) {
				Box currentBoxBar = (Box) currentBar;
				currentBoxBar.createBoxBody();
			}
			else if(currentBar instanceof Stone) {
				Stone currentStoneBar = (Stone) currentBar;
				currentStoneBar.createStoneBody();
			}
		}
		
	}
	
	private void populateActorArrays() {
		Scanner scanner = new Scanner(levelConfigString);
		Gdx.app.log("levelConfigString", levelConfigString);
		while(scanner.hasNext()) {
			if(scanner.hasNext("NormalBar")) {
				scanner.next();
				while(scanner.hasNext() && scanner.hasNextFloat()) {
					float x = scanner.nextFloat();
					float y = scanner.nextFloat();
					float rotation = scanner.nextFloat();
					NormalBar toAdd;
					try {
						toAdd = new NormalBar(tRegions.normalBar, rotation, BAR_NAME.NORMAL_BAR, x, y, RATIO, world);
						normalBarArr.add(toAdd);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}				
			}
			if(scanner.hasNext("BouncyBar")) {
				scanner.next();
				while(scanner.hasNext() && scanner.hasNextFloat()) {
					float x = scanner.nextFloat();
					float y = scanner.nextFloat();
					float rotation = scanner.nextFloat();
					try {
						BouncyBar toAdd = new BouncyBar(tRegions.bouncyBar, rotation, BAR_NAME.BOUNCY_BAR, x, y, RATIO, world);
						bouncyBarArr.add(toAdd);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if(scanner.hasNext("IBouncyBar")) {
				scanner.next();
				while(scanner.hasNext() && scanner.hasNextFloat()) {
					float x = scanner.nextFloat();
					float y = scanner.nextFloat();
					float rotation = scanner.nextFloat();
					try {
						IBouncyBar toAdd = new IBouncyBar(tRegions.iBouncyBar, rotation, BAR_NAME.INSANELY_BOUNCY_BAR, x, y, RATIO, world);
						iBouncyBarArr.add(toAdd);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if(scanner.hasNext("StickyBar")) {
				scanner.next();
				while(scanner.hasNext() && scanner.hasNextFloat()) {
					float x = scanner.nextFloat();
					float y = scanner.nextFloat();
					float rotation = scanner.nextFloat();
					try {
						StickyBar toAdd = new StickyBar(tRegions.stickyBar, rotation, BAR_NAME.STICKY_BAR, x, y, RATIO, world);
						stickyBarArr.add(toAdd);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}				
			}
			if(scanner.hasNext("SpikyBar")) {
				scanner.next();
				while(scanner.hasNext() && scanner.hasNextFloat()) {
					float x = scanner.nextFloat();
					float y = scanner.nextFloat();
					float rotation = scanner.nextFloat();
					try {
						SpikyBar toAdd = new SpikyBar(tRegions.spikyBar, rotation, BAR_NAME.SPIKY_BAR, x, y, RATIO, world);
						spikyBarArr.add(toAdd);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if(scanner.hasNext("MagicPot")) {
				scanner.next();
				while(scanner.hasNext() && scanner.hasNextFloat()) {
					float x = scanner.nextFloat();
					float y = scanner.nextFloat();
					float rotation = scanner.nextFloat();
					try {
						MagicPot toAdd = new MagicPot(tRegions.magicPotBefFrames, tRegions.magicPotAftFrames, rotation, BAR_NAME.MAGIC_POT, x, y, RATIO, world);
						magicPotArr.add(toAdd);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if(scanner.hasNext("Star")) {
				scanner.next();
				while(scanner.hasNext() && scanner.hasNextFloat()) {
					float x = scanner.nextFloat();
					float y = scanner.nextFloat();
					float rotation = scanner.nextFloat();
					try {
						Star toAdd = new Star(tRegions.starRegion, tRegions.starGlowRegion, tRegions.starDisAnimSheet, x, y, world);
						starArr.add(toAdd);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if(scanner.hasNext("Box")) {
				scanner.next();
				while(scanner.hasNext() && scanner.hasNextFloat()) {
					float x = scanner.nextFloat();
					float y = scanner.nextFloat();
					float rotation = scanner.nextFloat();
					try {
						Box toAdd = new Box(tRegions.boxObs, BAR_NAME.BOX_OBS, x, y, rotation, RATIO, world);
						boxArr.add(toAdd);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if(scanner.hasNext("Stone")) {
				scanner.next();
				while(scanner.hasNext() && scanner.hasNextFloat()) {
					float x = scanner.nextFloat();
					float y = scanner.nextFloat();
					float rotation = scanner.nextFloat();
					try {
						Stone toAdd = new Stone(tRegions.stoneObs, BAR_NAME.STONE_OBS, x, y, rotation, RATIO, world);
						stoneArr.add(toAdd);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		scanner.close();
	}
	
	protected abstract void populateFruitArray();
	
	private Vector2 getFruitStartVelocity(final Vector2 shotTestPoint) {
		final float normalizeFactor = 3000;
		final float originBlobX = blobStageOriginX;
		final float originBlobY = blobStageOriginY;
		float vXFactor = (originBlobX - shotTestPoint.x) / normalizeFactor;
		float vYFactor = (originBlobY - shotTestPoint.y) / normalizeFactor;
		float distance = shotTestPoint.dst(originBlobX, originBlobY);		
		return new Vector2((distance * vXFactor), (distance * vYFactor));
	}	
	
	private void resetGame() {
		this.dispose();
		localGameRef.setScreen(FruitRageGame.worldOne.createLevel(WorldOne.WORLD_ONE.valueOf("LEVEL" + levelNumber)));		
	}
	
	private void resumeGame() {
		for(Star star : myContactListener.starMap) {
			star.resume();
		}
	}
	
	private void pauseGame() {
		for(Star star : myContactListener.starMap) {
			star.pause();
		}
	}
	
	private boolean isLevelFailed() {
		if((starArr.size() == 3 && !myContactListener.fruitPottedAtLeastOnce) && fruitArr.isEmpty() && currentFruit == null && myContactListener.starMap.isEmpty())
			return true;
		return false;
	}
	
	private boolean isLevelPassed() {
		if(((starArr.size() < 3 || myContactListener.fruitPottedAtLeastOnce) && !starArr.isEmpty() && fruitArr.isEmpty() && currentFruit == null && myContactListener.starMap.isEmpty()) ||
				(starArr.isEmpty() && currentFruit == null && myContactListener.starMap.isEmpty()))
			return true;
		return false;
	}
	
	private final class LevelFailedMenu {
		private Image levelFailedLayout, translucentLayout;
		private ImageButton levelMenuButton, resetButton;
		private TextureRegion levelFailedLayoutRegion, translucentLayoutRegion, levelMenuRegion, levelMenuClickedRegion, resetRegion, resetClickedRegion;
		private boolean menuOnStage = false;
		
		public LevelFailedMenu() {
			levelFailedLayoutRegion = new TextureRegion(tRegions.levelFailedSheet, 0, 0, 532, 366);
			translucentLayoutRegion = new TextureRegion(tRegions.translucentSheet, 0, 0, (int) localStage.width(), (int) localStage.height());
			levelMenuRegion = new TextureRegion(tRegions.levelFailedSheet, 533, 0, 84, 77);
			levelMenuClickedRegion = new TextureRegion(tRegions.levelFailedSheet, 533, 78, 84, 77);
			resetRegion = new TextureRegion(tRegions.levelFailedSheet, 618, 0, 84, 77);
			resetClickedRegion = new TextureRegion(tRegions.levelFailedSheet, 533, 156, 84, 77);
			
			translucentLayout = new Image(translucentLayoutRegion);
			levelFailedLayout = new Image(levelFailedLayoutRegion);
			levelFailedLayout.x = localStage.width() / 2 - levelFailedLayout.width / 2;
			levelFailedLayout.y = localStage.height() / 2 - levelFailedLayout.height / 2;
			
			levelMenuButton = new ImageButton(levelMenuRegion, levelMenuClickedRegion) {
				@Override
				public void touchUp(float x, float y, int pointer) {
					super.touchUp(x, y, pointer);
					localGameRef.buttonClick.play();
					dispose();
					localGameRef.setScreen(localGameRef.levelMenuW1);
				}
			};
			resetButton = new ImageButton(resetRegion, resetClickedRegion) {
				@Override
				public void touchUp(float x, float y, int pointer) {
					super.touchUp(x, y, pointer);
					localGameRef.buttonClick.play();
					resetGame();
				}
			};
			levelMenuButton.x = levelFailedLayout.x + (levelFailedLayout.width/2 - levelMenuButton.width - 5);
			levelMenuButton.y = levelFailedLayout.y + 39;
			resetButton.x = levelFailedLayout.x + (levelFailedLayout.width/2 + 5);
			resetButton.y = levelFailedLayout.y + 39;			
		}
		
		public void addToStage() {
			if(!menuOnStage) {
				localStage.addActor(translucentLayout);
				localStage.addActor(levelFailedLayout);
				localStage.addActor(levelMenuButton);
				localStage.addActor(resetButton);
				menuOnStage = true;
				levelFailedSound.play();
			}
		}
	}
	
	private final class LevelClearedMenu {
		private Image levelClearedLayout, translucentLayout, star1, star2, star3;
		private ImageButton levelMenuButton, nextButton, resetButton;
		private TextureRegion levelClearedLayoutRegion, translucentLayoutRegion, levelMenu, levelMenuClicked, next, nextClicked, reset, resetClicked, starRegion;
		private boolean menuOnStage = false;
		private BitmapFont score;
		
		public LevelClearedMenu() {
			levelClearedLayoutRegion = new TextureRegion(tRegions.levelClearedSheet, 0, 0, 523, 369);
			translucentLayoutRegion = new TextureRegion(tRegions.translucentSheet, 0, 0, (int) localStage.width(), (int) localStage.height());
			levelMenu = new TextureRegion(tRegions.levelClearedSheet, 524, 0, 84, 77);
			levelMenuClicked = new TextureRegion(tRegions.levelClearedSheet, 524, 78, 84, 77);
			next = new TextureRegion(tRegions.levelClearedSheet, 609, 0, 84, 77);
			nextClicked = new TextureRegion(tRegions.levelClearedSheet, 524, 156, 84, 77);
			reset = new TextureRegion(tRegions.levelClearedSheet, 609, 78, 84, 77);
			resetClicked = new TextureRegion(tRegions.levelClearedSheet, 694, 0, 84, 77);
			starRegion = new TextureRegion(tRegions.levelClearedSheet, 524, 234, 78, 74);
			score = new BitmapFont(FruitRageGame.assets.getBitmapFontData(FILES.IN_GAME_FONT),
					 tRegions.inGameFontSheet, false);
			
			translucentLayout = new Image(translucentLayoutRegion);
			translucentLayout.x = 0; translucentLayout.y = 0;
			levelClearedLayout = new Image(levelClearedLayoutRegion);
			levelClearedLayout.x = localStage.width() / 2 - levelClearedLayout.width / 2;
			levelClearedLayout.y = localStage.height() / 2 - levelClearedLayout.height / 2;
			star1 = new Image(starRegion);
			star1.x = levelClearedLayout.x + 148; star1.y = levelClearedLayout.y + 167;
			star2 = new Image(starRegion);
			star2.x = levelClearedLayout.x + 228; star2.y = levelClearedLayout.y + 167;
			star3 = new Image(starRegion);		
			star3.x = levelClearedLayout.x + 308; star3.y = levelClearedLayout.y + 167;
			
			levelMenuButton = new ImageButton(levelMenu, levelMenuClicked) {
				@Override
				 public boolean touchDown(float x, float y, int pointer) {
					super.touchDown(x, y, pointer);
					return true;
				 }	
				@Override
				public void touchUp(float x, float y, int pointer) {
					super.touchUp(x, y, pointer);
					localGameRef.buttonClick.play();
					dispose();
					localGameRef.setScreen(localGameRef.levelMenuW1);
				}
			};
			nextButton = new ImageButton(next, nextClicked) {
				@Override
				 public boolean touchDown(float x, float y, int pointer) {
					super.touchDown(x, y, pointer);
					return true;
				 }	
				@Override
				public void touchUp(float x, float y, int pointer) {
					super.touchUp(x, y, pointer);
					localGameRef.buttonClick.play();
					int nxt = 1 + levelNumber;
					localGameRef.setScreen(FruitRageGame.worldOne.createLevel(WorldOne.WORLD_ONE.valueOf("LEVEL" + nxt)));
				}
			};
			resetButton = new ImageButton(reset, resetClicked) {
				@Override
				 public boolean touchDown(float x, float y, int pointer) {
					super.touchDown(x, y, pointer);
					return true;
				 }				 
				 @Override
				 public void touchUp(float x, float y, int pointer) {
					 super.touchUp(x, y, pointer);
					 localGameRef.buttonClick.play();
					 resetGame();
				 }
			};
			
			levelMenuButton.x = levelClearedLayout.x + 107;
			levelMenuButton.y = levelClearedLayout.y + 39;
			resetButton.x = levelClearedLayout.x + 228;
			resetButton.y = levelClearedLayout.y + 39;
			nextButton.x = levelClearedLayout.x + 348;
			nextButton.y = levelClearedLayout.y + 39;
		}
		
		private float finalScore;
		private float displayScore = 0;
		
		public void renderScore() {
			localStage.getSpriteBatch().begin();
			finalScore = scoring.getScore();
			if(displayScore != finalScore && (finalScore - displayScore) > 30) { displayScore += 30; }
			else if(displayScore != finalScore && displayScore < finalScore) { ++displayScore; }			
			score.draw(localStage.getSpriteBatch(), String.valueOf((int) displayScore), levelClearedLayout.x + 241, levelClearedLayout.y + 150);
			localStage.getSpriteBatch().end();
			int tmpScore = scoring.getScore() / 3;
			if(displayScore >= tmpScore && (3 - starArr.size()) >= 1) {
				localStage.addActor(star1);
			}
			if(displayScore >= tmpScore * 2 && (3 - starArr.size()) >= 2) {
				localStage.addActor(star2);
			}
			if(displayScore >= tmpScore * 3 && (3 - starArr.size() == 3)) {
				localStage.addActor(star3);
			}
		}
		
		public void addToStage() {
			if(!menuOnStage) {
				localStage.addActor(translucentLayout);
				localStage.addActor(levelClearedLayout);
				localStage.addActor(levelMenuButton);
				localStage.addActor(resetButton);
				if(levelNumber < 15)
					localStage.addActor(nextButton);
				menuOnStage = true;
			}
		}
		
		/*public boolean removeFromStage() {
			if(menuOnStage) {
				localStage.removeActor(translucentLayout);
				localStage.removeActor(levelClearedLayout);
				//localStage.removeActor(star1);
				//localStage.removeActor(star2);
				//localStage.removeActor(star3);
				localStage.removeActor(levelMenuButton);
				localStage.removeActor(resetButton);
				localStage.removeActor(nextButton);
				return !menuOnStage != menuOnStage;
			}
			return !menuOnStage;
		}*/
	}
	
	private final class PauseMenu {
		private Image pauseLayout, translucentLayout;
		private ImageButton resumeButton, resetButton, soundButton, levelMenuButton;
		private TextureRegion pauseLayoutRegion, translucentLayoutRegion, resume,  reset, resetClicked, sound, soundClicked, levelMenu, levelMenuClicked;
		
		public PauseMenu() {
			pauseLayoutRegion = new TextureRegion(tRegions.pauseMenuSheet, 0, 0, 248, (int) localStage.height());
			translucentLayoutRegion = new TextureRegion(tRegions.translucentSheet, 0, 0, (int) localStage.width(), (int) localStage.height());
			resume = new TextureRegion(tRegions.pauseMenuSheet, 249, 0, 133, 176);
			reset = new TextureRegion(tRegions.pauseMenuSheet, 383, 61, 62, 60);
			resetClicked = new TextureRegion(tRegions.pauseMenuSheet, 446, 0, 62, 60);
			sound = new TextureRegion(tRegions.pauseMenuSheet, 249, 238, 62, 60);
			soundClicked = new TextureRegion(tRegions.pauseMenuSheet, 312, 177, 62, 60);
			levelMenu = new TextureRegion(tRegions.pauseMenuSheet, 383, 0, 62, 60);
			levelMenuClicked = new TextureRegion(tRegions.pauseMenuSheet, 249, 177, 62, 60);
			
			pauseLayout = new Image(pauseLayoutRegion);
			pauseLayout.x = 0; pauseLayout.y = 0;
			translucentLayout = new Image(translucentLayoutRegion);
			translucentLayout.x = 0; translucentLayout.y = 0;
			
			final PauseMenu internalRef = this;
			
			resumeButton = new ImageButton(resume) {
				@Override
				public boolean touchDown(float x, float y, int pointer) {
					super.touchDown(x, y, pointer);
					return true;
				}
				@Override
				public void touchUp(float x, float y, int pointer) {
					super.touchUp(x, y, pointer);
					localGameRef.buttonClick.play();
					resumeGame();
					isPaused = false;
					internalRef.removeFromStage();
				}
			};
			resumeButton.x = pauseLayout.width/2 - resumeButton.width/2;
			resumeButton.y = pauseLayout.height/2 - resumeButton.height/2;
			
			resetButton = new ImageButton(reset, resetClicked) {
				@Override
				 public boolean touchDown(float x, float y, int pointer) {
					super.touchDown(x, y, pointer);
					return true;
				 }				 
				 @Override
				 public void touchUp(float x, float y, int pointer) {
					 super.touchUp(x, y, pointer);
					 localGameRef.buttonClick.play();
					 resetGame();
				 }
			};
			resetButton.x = pauseLayout.width/2 - resetButton.width/2;
			resetButton.y = resumeButton.y - 100;
			
			soundButton = new ImageButton(sound, soundClicked) {};
			soundButton.x = resetButton.x - 70;
			soundButton.y = resetButton.y;
			
			levelMenuButton = new ImageButton(levelMenu, levelMenuClicked) {
				@Override
				public void touchUp(float x, float y, int pointer) {
					super.touchUp(x, y, pointer);
					localGameRef.buttonClick.play();
					dispose();
					localGameRef.setScreen(localGameRef.levelMenuW1);
				}
			};
			levelMenuButton.x = resetButton.x + 70;
			levelMenuButton.y = resetButton.y;
		}
		
		public void addToStage() {
			localStage.addActor(translucentLayout);
			localStage.addActor(pauseLayout);
			localStage.addActor(resumeButton);
			localStage.addActor(resetButton);
			localStage.addActor(soundButton);
			localStage.addActor(levelMenuButton);
		}
		
		public void removeFromStage() {
			localStage.removeActor(levelMenuButton);
			localStage.removeActor(soundButton);
			localStage.removeActor(resetButton);
			localStage.removeActor(resumeButton);
			localStage.removeActor(pauseLayout);
			localStage.removeActor(translucentLayout);
		}
	}
	
	protected final class TopBar {
		 public Image nextFruit;
		 public ImageButton skipButton, pauseButton; 
		 public TextureRegion qMark, skip, skipClicked, pause, pauseClicked;
		 private BitmapFont topBarScore, topBarLog1, topBarLog2;
		 
		 public TopBar() {
			 nextFruit = new Image(fruitArr.get(fruitIndex).getRegion());			 
			 
			 qMark = new TextureRegion(tRegions.topBarButtons, 189, 0, 31, 30);
			 skip = new TextureRegion(tRegions.topBarButtons, 0, 0, 62, 60);
			 skipClicked = new TextureRegion(tRegions.topBarButtons, 63, 0, 62, 60);
			 pause = new TextureRegion(tRegions.topBarButtons, 0, 61, 62, 60);
			 pauseClicked = new TextureRegion(tRegions.topBarButtons, 63, 61, 62, 60);
			 
			 skipButton = new ImageButton(skip, skipClicked) {
				 @Override
				 public boolean touchDown(float x, float y, int pointer) {
					 if(!isPaused && (!levelClearedMenu.menuOnStage || !levelFailedMenu.menuOnStage) && currentFruit != null) {
						 super.touchDown(x, y, pointer);
						 return true;
					 } return false;
				 }
				 @Override
				 public void touchUp(float x, float y, int pointer) {
					 super.touchUp(x, y, pointer);
					 localGameRef.buttonClick.play();
					 skipCurrentAttempt = true;
					 skipAnnoTouched = true;
				 }
			 };
			 pauseButton = new ImageButton(pause, pauseClicked) {
				 @Override
				 public boolean touchDown(float x, float y, int pointer) {
					if(!isPaused && (!levelClearedMenu.menuOnStage || !levelFailedMenu.menuOnStage)
							&& !levelFailedMenu.menuOnStage && !levelClearedMenu.menuOnStage) {
						super.touchDown(x, y, pointer);
						return true;
					} return false;					
				 }
				 @Override
				 public void touchUp(float x, float y, int pointer) {
					 super.touchUp(x, y, pointer);
					 localGameRef.buttonClick.play();
					 if(!isPaused) { pauseGame(); pauseMenu.addToStage(); } //else { resumeGame(); pauseMenu.removeFromStage(); }
					 isPaused = !isPaused;
				 }
			 };
			 
			 pauseButton.x = 10; pauseButton.y = localStage.height() - 60;
			 skipButton.x = pauseButton.x + pauseButton.width + 10; skipButton.y = localStage.height() - 60;
			 nextFruit.x = skipButton.x + skipButton.width + 15;
			 nextFruit.y = localStage.height() - 43;
			 
			 topBarScore = new BitmapFont(FruitRageGame.assets.getBitmapFontData(FILES.IN_GAME_FONT),
					 tRegions.inGameFontSheet, false);
			 topBarLog1 = new BitmapFont(FruitRageGame.assets.getBitmapFontData(FILES.IN_GAME_FONT),
					 tRegions.inGameFontSheet, false);
			 topBarLog2 = new BitmapFont(FruitRageGame.assets.getBitmapFontData(FILES.IN_GAME_FONT),
					 tRegions.inGameFontSheet, false);
		 }		 
		 public void addToStage() {
			 localStage.addActor(nextFruit);
			 localStage.addActor(skipButton);
			 localStage.addActor(pauseButton);
		 }
	}
}
