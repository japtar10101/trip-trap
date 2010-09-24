package com.games.deception.singleton;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.detector.HoldDetector;
import org.anddev.andengine.input.touch.detector.HoldDetector.IHoldDetectorListener;

import com.badlogic.gdx.math.Vector2;
import com.games.deception.element.BaseElement;

// TODO: if this doesn't work, try copying HoldDetector
// FIXME: Nope, this does not work.  Turn it into a IOnSceneTouchListener and IUpdateHandler
/**
 * Singleton class.  Controls the player's physics body.
 * @author japtar10101
 */
public class PullPlayerController implements IHoldDetectorListener {
	/* ===========================================================
	 * Members
	 * =========================================================== */
	
	/** Singleton variable */
	private static PullPlayerController msInstance = null;
	
	// Variables to update at run
	/** If true, move the player to the point */
	private boolean mIsMovingToTarget;
	/** The point the player will move to */
	private final Vector2 mTargetPoint;
	/** The amount of force used to pull the player to the target */
	private final Vector2 mTargetForce;
	
	// Physics-related variable
	/** The body affected by player's action. Can be null. */
	private BaseElement mElement;
	
	/* ===========================================================
	 * Constructors
	 * =========================================================== */
	
	/**
	 * Constructor.
	 * @param physicsBody sets mPhysicsBody
	 */
	private PullPlayerController() {
		// Set the primary variables to their default value
		mIsMovingToTarget = false;
		
		// Construct vectors
		mTargetPoint = new Vector2();
		mTargetForce = new Vector2();
		
		// Set sprite related stuff to null
		mElement = null;
	}
	
	/* ===========================================================
	 * Overrides
	 * =========================================================== */
	
	@Override
	public void onHold(HoldDetector pHoldDetector, long holdTimeMilliseconds,
			float holdX, float holdY) {
		if(mElement != null) {
			mIsMovingToTarget = true;
			mTargetPoint.set(holdX, holdY);
			this.updateForce();
			this.movePlayer();
		}
	}

	@Override
	public void onHoldFinished(HoldDetector pHoldDetector,
			long pHoldTimeMilliseconds, float pHoldX, float pHoldY) {
		if(mElement != null) {
			mIsMovingToTarget = false;
		}
	}
	
	/* ===========================================================
	 * Public Methods
	 * =========================================================== */
	
	/**
	 * @return {@link msInstance}
	 */
	public static PullPlayerController getInstance() {
		if (msInstance == null) {
			synchronized(PullPlayerController.class) {
				if (msInstance == null) {
					msInstance = new PullPlayerController();
				}
		    }
		  }
		  return msInstance;
	}
	
	/** @param element the element to control
	  * @return {@link msInstance} */
	public static PullPlayerController startController(final BaseElement element) {
		PullPlayerController toReturn = PullPlayerController.getInstance();
		toReturn.setElement(element);
		return toReturn;
	}
	
	/** Terminates controls */
	public static void endController() {
		startController(null);
	}
	
	/* ===========================================================
	 * Getters
	 * =========================================================== */
	
	/**
	 * @return {@link mIsMovingToTarget}
	 */
	public boolean isMovingToTarget() {
		return mIsMovingToTarget;
	}

	/**
	 * @return {@link mTargetPoint}
	 */
	public Vector2 getTargetPoint() {
		return mTargetPoint;
	}

	/**
	 * @return {@link mTargetForce}
	 */
	public Vector2 getTargetForce() {
		return mTargetForce;
	}

	/** @return {@link mElement} */
	public BaseElement getElement() {
		return mElement;
	}
	
	/* ===========================================================
	 * Setters
	 * =========================================================== */

	/** @param physicsBody sets {@link mElement} */
	public void setElement(BaseElement element) {
		mElement = element;
	}
	
	/* ===========================================================
	 * Private/Protected Methods
	 * =========================================================== */
	
	/** Updates {@link mTargetForce} */
	private void updateForce() {
		// Determine the amount of force necessary to push the player
		final Sprite playerPos = mElement.getSprite();
		
		// TODO: proportion and limit the amount of force applicable
		mTargetForce.set(playerPos.getBaseX() - mTargetPoint.x,
				playerPos.getBaseY() - mTargetPoint.y);
		//final float magnitude = mTargetForce.dst(0, 0);
	}
	
	/** Pulls the player to the {@link mTargetPoint} */
	private void movePlayer() {		
		// Apply the force to the body
		mElement.getPhysicsBody().applyLinearImpulse(
				mTargetForce, mTargetPoint);
	}
}
