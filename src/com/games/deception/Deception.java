package com.games.deception;

import java.util.Random;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.games.deception.behavior.AttractToControllableBehavior;
import com.games.deception.behavior.controller.PullPlayerController;
import com.games.deception.constant.GameDimension;
import com.games.deception.constant.GamePhysics;
import com.games.deception.element.Wall;
import com.games.deception.element.controllable.Bee;
import com.games.deception.element.controllable.Marble;

public class Deception extends BaseGameActivity {
	/* ===========================================================
	 * Constants
	 * =========================================================== */
	
	private static final byte NUM_BEES = 4;
	private static final byte NUM_WALLS = 5;
	
	/* ===========================================================
	 * Members
	 * =========================================================== */

	// TODO: accumulate these 4 stuff into a Model (Model-View-Controller)
	// Textures goes under a flyweight design, as well as fixtures
	private Texture mMarbleTexture;
	private TextureRegion mMarbleTextureRegion;
	// Sprite and body should be held individually
	private Sprite mMarbleSprite;
	private Body mMarbleBody;
	// Remember we need to parse and generate these things (possibly
	// a factory design)

	private Texture mBeeTexture;
	private TextureRegion mBeeTextureRegion;
	private Sprite mBeeSprite[] = new Sprite[NUM_BEES];
	private Body mBeeBody[] = new Body[NUM_BEES];
	
	private Wall mWall[] = new Wall[NUM_WALLS];
	
	private Camera mCamera;

	// TODO: refactor these so that they share a common class
	private AttractToControllableBehavior mBehavior = null;
	private PullPlayerController mControls = null;
	
	/* ===========================================================
	 * Overrides
	 * =========================================================== */

	@Override
	public Engine onLoadEngine() {
		mCamera = new Camera(0, 0, GameDimension.CAMERA_WIDTH,
				GameDimension.CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, GameDimension.ORIENTATION,
				new RatioResolutionPolicy(GameDimension.CAMERA_WIDTH,
						GameDimension.CAMERA_HEIGHT), mCamera));
	}

	@Override
	public void onLoadResources() {
		setupMarbleTexture();
		setupBeeTexture();
	}

	@Override
	public Scene onLoadScene() {
		final Scene scene = new Scene(2);
		scene.setBackground(new ColorBackground(0.2f, 0.2f, 0.2f));
		
		// Generate the physics system
		scene.registerUpdateHandler(GamePhysics.PHYSICS_WORLD);
		
		// Generate the player sprite
		setupMarbleSprite(scene);
		setupBeeSprite(scene);
		setupWalls(scene);
		
		// Have the camera chase the player
		mCamera.setChaseShape(mMarbleSprite);
		
		// Setup controls
		final Marble marble = new Marble(mMarbleSprite, mMarbleBody);
		mControls = PullPlayerController.getInstance();
		mControls.setElement(marble);
		scene.registerUpdateHandler(mControls);
		scene.setOnSceneTouchListener(mControls);
		
		//Setup behavior
		mBehavior = new AttractToControllableBehavior(marble);
		for(byte index = 0; index < NUM_BEES; index++) {
			mBehavior.addElement(new Bee(mBeeSprite[index], mBeeBody[index]));
		}
		scene.registerUpdateHandler(mBehavior);
		
		return scene;
	}

	@Override
	public void onLoadComplete() {}
	
	/* ===========================================================
	 * Private Methods
	 * =========================================================== */
		
	private void setupMarbleTexture() {
		mMarbleTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		TextureRegionFactory.setAssetBasePath("images/");
		
		mMarbleTextureRegion = TextureRegionFactory.createFromAsset(
				mMarbleTexture, this, "marbleBase.png", 0, 0); // 64x64
		mEngine.getTextureManager().loadTexture(mMarbleTexture);
	}
	
	private void setupBeeTexture() {
		mBeeTexture = new Texture(32, 32, TextureOptions.BILINEAR);
		TextureRegionFactory.setAssetBasePath("images/");
		
		mBeeTextureRegion = TextureRegionFactory.createFromAsset(
				mBeeTexture, this, "beeBase.png", 0, 0); // 64x64
		mEngine.getTextureManager().loadTexture(mBeeTexture);
	}
	
	private void setupMarbleSprite(final Scene scene) {
		// Calculate the coordinates for the face, so its centered on the camera.
		final int centerX = (GameDimension.CAMERA_WIDTH -
				mMarbleTextureRegion.getWidth()) / 4;
		final int centerY = (GameDimension.CAMERA_HEIGHT -
				mMarbleTextureRegion.getHeight()) / 4;

		// Create the face and add it to the scene.
		mMarbleSprite = new Sprite(centerX, centerY, mMarbleTextureRegion);
		scene.getTopLayer().addEntity(mMarbleSprite);
		
		// Create a physics body
		final FixtureDef objectFixtureDef =
			PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		mMarbleBody = PhysicsFactory.createCircleBody(GamePhysics.PHYSICS_WORLD,
				mMarbleSprite, BodyType.DynamicBody, objectFixtureDef);
		mMarbleBody.setLinearDamping(5f);
		mMarbleBody.setAngularDamping(1f);
		final MassData massData = new MassData();
		massData.mass = 8;
		mMarbleBody.setMassData(massData);
		
		// Associate the physics body with physics world and sprite
		mMarbleSprite.setUpdatePhysics(false);
		scene.getTopLayer().addEntity(mMarbleSprite);
		GamePhysics.PHYSICS_WORLD.registerPhysicsConnector(new PhysicsConnector(
				mMarbleSprite, mMarbleBody, true, true, false, false));
	}
	
	private void setupBeeSprite(final Scene scene) {
		final Random generator = new Random();
		final MassData massData = new MassData();
		massData.mass = 1;
		final FixtureDef objectFixtureDef =
			PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		
		for(byte index = 0; index < NUM_BEES; index++) {
			// Create the face and add it to the scene.
			mBeeSprite[index] = new Sprite(
					generator.nextInt(GameDimension.CAMERA_WIDTH - 64) + 32,
					generator.nextInt(GameDimension.CAMERA_HEIGHT - 64) + 32,
					mBeeTextureRegion);
			scene.getTopLayer().addEntity(mBeeSprite[index]);
			
			// Create a physics body
			mBeeBody[index] = PhysicsFactory.createCircleBody(GamePhysics.PHYSICS_WORLD,
					mBeeSprite[index], BodyType.DynamicBody, objectFixtureDef);
			mBeeBody[index].setLinearDamping(5f);
			mBeeBody[index].setAngularDamping(5f);
			
			// Associate the physics body with physics world and sprite
			mBeeSprite[index].setUpdatePhysics(false);
			scene.getTopLayer().addEntity(mBeeSprite[index]);
			GamePhysics.PHYSICS_WORLD.registerPhysicsConnector(new PhysicsConnector(
					mBeeSprite[index], mBeeBody[index], true, true, false, false));
		}
	}
	
	/**
	 * TODO: add a description
	 * @param scene
	 */
	private void setupWalls(Scene scene) {
		byte index = 0;
		mWall[index] = new Wall(0, GameDimension.CAMERA_HEIGHT - 2,
				GameDimension.CAMERA_WIDTH, 2);
		scene.getTopLayer().addEntity(mWall[index].getSprite());
		
		++index;
		mWall[index] = new Wall(0, 0, GameDimension.CAMERA_WIDTH, 2);
		scene.getTopLayer().addEntity(mWall[index].getSprite());
		
		++index;
		mWall[index] = new Wall(0, 0, 2, GameDimension.CAMERA_HEIGHT);
		scene.getTopLayer().addEntity(mWall[index].getSprite());
		
		++index;
		mWall[index] = new Wall(GameDimension.CAMERA_WIDTH - 2, 0,
				2, GameDimension.CAMERA_HEIGHT);
		scene.getTopLayer().addEntity(mWall[index].getSprite());
		
		++index;
		final int halfWidth = GameDimension.CAMERA_WIDTH / 2;
		final int halfHeight = GameDimension.CAMERA_HEIGHT / 2;
		mWall[index] = new Wall(halfWidth - 16, halfHeight - 16, 32, 32);
		scene.getTopLayer().addEntity(mWall[index].getSprite());
	}
}
