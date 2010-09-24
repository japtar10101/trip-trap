package com.games.deception.singleton;

import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.input.touch.TouchEvent;

import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.games.deception.element.controllable.ControllableElement;

//TODO: take a better look at HoldDetector
/**
 * Singleton class.  Controls the player's physics body.
 * @author japtar10101
 */
public class PullPlayerController implements IUpdateHandler, IOnSceneTouchListener {
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
	private ControllableElement mElement;
	
	/* ===========================================================
	 * Constructors
	 * =========================================================== */
	
	/**
	 * Constructor.  Sets {@link mElement} to null.
	 */
	private PullPlayerController() {
		// Construct vectors
		mTargetPoint = new Vector2();
		mTargetForce = new Vector2();
		
		// Set sprite related stuff to null
		this.setElement(null);
	}
	
	/* ===========================================================
	 * Overrides
	 * =========================================================== */
	
	/**
	 * Determines the state to take based on a touch event
	 * @param scene not used
	 * @param sceneTouchEvent the condition of the user's action
	 * @return true if any action took placed; false, otherwise.
	 */
	@Override
	public boolean onSceneTouchEvent(final Scene scene,
			final TouchEvent sceneTouchEvent) {
		// If element is null, ignore all actions
		if(mElement == null)
			return false;
		
		// Otherwise, determine which action to take, if any
		boolean validAction = true;
		switch(sceneTouchEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				this.startHold(
						sceneTouchEvent.getX(), sceneTouchEvent.getY());
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				this.endHold();
				break;
			default:
				validAction = false;
		}
		return validAction;
	}
	
	/**
	 * Progressively moves the player to where one touched.
	 * @param pSecondsElapsed not used
	 */
	@Override
	public void onUpdate(final float pSecondsElapsed) {
		// Update the player's position when holding onto screen
		if(mElement != null && mIsMovingToTarget == true && Float.compare(pSecondsElapsed, 0.01f) > 0) {
			this.updateForce();
			this.movePlayer();
		}
	}
	
	/**
	 * Defaults the player's controls
	 * @see PullPlayerController
	 */
	@Override
	public void reset() {
		this.setElement(null);
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
	public ControllableElement getElement() {
		return mElement;
	}
	
	/* ===========================================================
	 * Setters
	 * =========================================================== */

	/** @param physicsBody sets {@link mElement} */
	public void setElement(ControllableElement element) {
		mElement = element;
		if(element == null) {
			mIsMovingToTarget = false;
		}
	}
	
	/* ===========================================================
	 * Private/Protected Methods
	 * =========================================================== */
	
	/** Updates {@link mTargetForce} */
	private void updateForce() {
		// Determine the amount of force necessary to push the player
		mTargetForce.set(mElement.getFront());
		
		// proportion the amount of force applicable
		mTargetForce.x = mTargetPoint.x - mTargetForce.x;
		mTargetForce.y = mTargetPoint.y - mTargetForce.y;
	}
	
	/** Pulls the player to the {@link mTargetPoint} */
	private void movePlayer() {		
		// Apply the force to the body
		mElement.getPhysicsBody().applyLinearImpulse(mTargetForce, mElement.getFront());
	}
	
	/** Initiates or updates the hold condition */
	private void startHold(float holdX, float holdY) {
		mIsMovingToTarget = true;
		mTargetPoint.set(holdX, holdY);
	}

	/** Ends the hold condition */
	private void endHold() {
		mIsMovingToTarget = false;
	}
}
