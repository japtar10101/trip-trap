package com.games.deception;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Deception extends BaseGameActivity implements IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_HEIGHT = 720;
	private static final int CAMERA_WIDTH = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Texture mTexture;
	private TextureRegion mPlayerTextureRegion;
	private Sprite mPlayer;
	
	private PhysicsWorld mPhysicsWorld;
	
	private final Vector2 mTempForce = new Vector2();
	private final Vector2 mTempPoint = new Vector2();

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Engine onLoadEngine() {
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera));
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		TextureRegionFactory.setAssetBasePath("images/");
		
		this.mPlayerTextureRegion = TextureRegionFactory.createFromAsset(
				this.mTexture, this, "marbleBase.png", 0, 0); // 64x64
		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		final Scene scene = new Scene(1);
		scene.setBackground(new ColorBackground(0.2f, 0.2f, 0.2f));
		scene.setOnSceneTouchListener(this);
		
		// Generate the physics system
		this.mTempForce.set(0, 0);
		this.mPhysicsWorld = new PhysicsWorld(this.mTempForce, false);
		this.mPhysicsWorld.setGravity(mTempForce);
		scene.registerUpdateHandler(this.mPhysicsWorld);
		
		// Generate the player sprite
		setupPlayerSprite(scene);
		
		return scene;
	}

	@Override
	public void onLoadComplete() {}
	
	@Override
	public boolean onSceneTouchEvent(final Scene scene,
			final TouchEvent sceneTouchEvent) {
		if(this.mPhysicsWorld != null) {
			if(sceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
				this.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						Deception.this.pullPlayer(sceneTouchEvent.getX(), sceneTouchEvent.getY());
					}
				});
				return true;
			}
		}
		return false;
	}
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	private void setupPlayerSprite(final Scene scene) {
		// Calculate the coordinates for the face, so its centered on the camera.
		final int centerX = (CAMERA_WIDTH -
				this.mPlayerTextureRegion.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT -
				this.mPlayerTextureRegion.getHeight()) / 2;

		// Create the face and add it to the scene.
		this.mPlayer = new Sprite(centerX, centerY, this.mPlayerTextureRegion);
		scene.getTopLayer().addEntity(mPlayer);
		
		final FixtureDef objectFixtureDef =
			PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		final Body body = PhysicsFactory.createCircleBody(this.mPhysicsWorld,
				this.mPlayer, BodyType.DynamicBody, objectFixtureDef);
		
		this.mPlayer.setUpdatePhysics(false);
		scene.getTopLayer().addEntity(this.mPlayer);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				this.mPlayer, body, true, true, false, false));
	}
	
	private void pullPlayer(final float targetX, final float targetY) {
		// Force the velocity to be "pulled" into the direction touched
		mTempPoint.set(targetX, targetY);
		mTempForce.set(
				mPlayer.getX() - targetX,
				mPlayer.getY() - targetY);
		
		// Grab the body associated with the player
		final Body faceBody = this.mPhysicsWorld.
			getPhysicsConnectorManager().findBodyByShape(this.mPlayer);
		
		// Pull the player
		faceBody.applyLinearImpulse(this.mTempForce, this.mTempPoint);
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
