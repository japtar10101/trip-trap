package com.games.deception;

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

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.games.deception.constant.GameDimension;
import com.games.deception.constant.GamePhysics;
import com.games.deception.element.controllable.Marble;
import com.games.deception.singleton.PullPlayerController;

public class Deception extends BaseGameActivity {
	/* ===========================================================
	 * Members
	 * =========================================================== */

	private Texture mTexture;
	private TextureRegion mPlayerTextureRegion;
	private Sprite mPlayer;
	
	private final Vector2 mTempVector = new Vector2();
	
	private PullPlayerController mControls = null;
	
	/* ===========================================================
	 * Overrides
	 * =========================================================== */

	@Override
	public Engine onLoadEngine() {
		final Camera camera = new Camera(0, 0, GameDimension.CAMERA_WIDTH, GameDimension.CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, GameDimension.ORIENTATION,
				new RatioResolutionPolicy(GameDimension.CAMERA_WIDTH, GameDimension.CAMERA_HEIGHT), camera));
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
		final Scene scene = new Scene(2);
		scene.setBackground(new ColorBackground(0.2f, 0.2f, 0.2f));
		
		// Generate the physics system
		this.mTempVector.set(0, 0);
		scene.registerUpdateHandler(GamePhysics.PHYSICS_WORLD);
		
		// Generate the player sprite
		setupPlayerSprite(scene);
		final Body playerBody = GamePhysics.PHYSICS_WORLD.
				getPhysicsConnectorManager().findBodyByShape(this.mPlayer);
		playerBody.setLinearDamping(5f);
		playerBody.setAngularDamping(5f);
		
		// Setup controls
		this.mControls = PullPlayerController.getInstance();
		this.mControls.setElement(new Marble(this.mPlayer, playerBody));
		scene.registerUpdateHandler(mControls);
		scene.setOnSceneTouchListener(mControls);
		
		return scene;
	}

	@Override
	public void onLoadComplete() {}
	
	/* ===========================================================
	 * Private Methods
	 * =========================================================== */
	
	private void setupPlayerSprite(final Scene scene) {
		// Calculate the coordinates for the face, so its centered on the camera.
		final int centerX = (GameDimension.CAMERA_WIDTH -
				this.mPlayerTextureRegion.getWidth()) / 2;
		final int centerY = (GameDimension.CAMERA_HEIGHT -
				this.mPlayerTextureRegion.getHeight()) / 2;

		// Create the face and add it to the scene.
		this.mPlayer = new Sprite(centerX, centerY, this.mPlayerTextureRegion);
		scene.getTopLayer().addEntity(mPlayer);
		
		final FixtureDef objectFixtureDef =
			PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		final Body body = PhysicsFactory.createCircleBody(GamePhysics.PHYSICS_WORLD,
				this.mPlayer, BodyType.DynamicBody, objectFixtureDef);
		
		this.mPlayer.setUpdatePhysics(false);
		scene.getTopLayer().addEntity(this.mPlayer);
		GamePhysics.PHYSICS_WORLD.registerPhysicsConnector(new PhysicsConnector(
				this.mPlayer, body, true, true, false, false));
	}
}
