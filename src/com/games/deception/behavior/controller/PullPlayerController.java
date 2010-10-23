package com.games.deception.behavior.controller;

import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.input.touch.TouchEvent;

import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.games.deception.element.controllable.ControllableElement;

//TODO: take a better look at HoldDetector. This class is too slow.
/**
 * Singleton class.  Controls the player's physics body.
 * 
 *  TODO: this is technically a behavior as well.  Consider refactoring as such
 * @author japtar10101
 */
public class PullPlayerController implements
		IUpdateHandler, IOnSceneTouchListener {
	/* ===========================================================
	 * Constants
	 * =========================================================== */
	
	private static float MAX_TIME_BETWEEN_MOVE_ACTION = 0.5f;
	private static float NO_TIME = -1;
	
	/* ===========================================================
	 * Members
	 * =========================================================== */
	
	/** Singleton variable */
	private static PullPlayerController msInstance = null;
	
	// Variables to update at run
	/** If true, move the player to the point */
	private boolean mIsMovingToTarget;
	/** The point the player will move to */
	private final Vector2 mTargetPoint  = new Vector2();
	/** The amount of force used to pull the player to the target */
	private final Vector2 mTargetForce = new Vector2();
//	private final Vector2 mBalanceForce = new Vector2();
	
	// Physics-related variable
	/** The body affected by player's action. Can be null. */
	private ControllableElement mElement;
	
	// Touch-related variables
	private float mTimeBetweenMoveAction = NO_TIME;
	
	/* ===========================================================
	 * Constructors
	 * =========================================================== */
	
	/**
	 * Constructor.  Sets {@link mElement} to null.
	 */
	private PullPlayerController() {
		// Set sprite related stuff to null
		this.setElement(null);
	}
	
	/* ===========================================================
	 * Overrides
	 * =========================================================== */
	
	// TODO: you can optimize this method by doing less: keeping
	// track of time and motion event.  Use any action on a separate thread.
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
			// Only call update if the the time passed from
			// the last move is long enough
			case MotionEvent.ACTION_MOVE:
				validAction =
					mTimeBetweenMoveAction > MAX_TIME_BETWEEN_MOVE_ACTION;
			
			// On action down flag for moving player
			case MotionEvent.ACTION_DOWN:
				this.startHold(
						sceneTouchEvent.getX(), sceneTouchEvent.getY());
				if(validAction) {
					mTimeBetweenMoveAction = 0;
				}
				break;
			
			// On up or cancel, drop the hold flag
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				this.endHold();
				break;
							
			// Rest are invalid actions
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
		if(mElement != null && mIsMovingToTarget == true) {
			this.updateForce();
			this.movePlayer();
			if(mTimeBetweenMoveAction > NO_TIME)
				mTimeBetweenMoveAction += pSecondsElapsed;
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
	// TODO: refactor this part to its own "attraction" class
	private void updateForce() {
		// Determine the amount of force necessary to push the player
		mTargetForce.set(mTargetPoint);
		mTargetForce.sub(mElement.getFront());
		
		// lower the magnitude of the force
		float magnitude = mTargetForce.dst(0, 0);
		magnitude *= mElement.SpeedMultiplier;
		if(magnitude > mElement.MaxSpeed) {
			magnitude = mElement.MaxSpeed;
		}
		
		// Apply the magnitude to force
		if(Float.compare(magnitude, 0.001f) <= 0) {
			mTargetForce.set(0, 0);
		} else {
			mTargetForce.nor();
			mTargetForce.mul(magnitude);
		}
	}
	
	/** Pulls the player to the {@link mTargetPoint} */
	private void movePlayer() {
		// Apply force to the body, if force is greater than 0
		if(Float.compare(mTargetForce.x, 0) != 0 &&
				Float.compare(mTargetForce.y, 0) != 0) {
			mElement.getPhysicsBody().applyLinearImpulse(
					mTargetForce, mElement.getFront());
		}
		
		// Straighten the body
//		mBalanceForce.set(mTargetForce);
//		mBalanceForce.mul(0.05f);
//		mElement.getPhysicsBody().applyLinearImpulse(
//				mBalanceForce, mElement.getFront());
//		mElement.getPhysicsBody().applyLinearImpulse(
//				mBalanceForce.mul(-1f), mElement.getBack());
	}
	
	/** Initiates or updates the hold condition */
	private void startHold(float holdX, float holdY) {
		mIsMovingToTarget = true;
		mTargetPoint.set(holdX, holdY);
	}

	/** Ends the hold condition */
	private void endHold() {
		mIsMovingToTarget = false;
		mTimeBetweenMoveAction = NO_TIME;
	}
}
