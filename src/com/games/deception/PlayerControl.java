package com.games.deception;

import org.anddev.andengine.input.touch.TouchEvent;

import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Singleton class.  Controls the player's physics body.
 * @author japtar10101
 */
public class PlayerControl implements Runnable {
	/* ===========================================================
	 * Members
	 * =========================================================== */
	
	/** Singleton variable */
	private static PlayerControl msController = null;
	
	// Temporary variables
	/** The most recent touch event */
	private int mRecentAction;
	/** The most recently touch point */
	private final Vector2 mRecentTarget;
	
	// Variables to update at run
	/** If true, move the player to the point */
	private boolean mMoveToTarget;
	/** The point the player will move to */
	private final Vector2 mTargetPoint;
	/** The amount of force used to pull the player to the target */
	private final Vector2 mTargetForce;
	
	// Physics-related variable
	/** The body affected by player's action. Can be null. */
	private Body mPhysicsBody;
	
	/* ===========================================================
	 * Constructors
	 * =========================================================== */
	
	/**
	 * Constructor.
	 * @param physicsBody sets mPhysicsBody
	 */
	private PlayerControl(final Body physicsBody) {
		// Set the primary variables to their default value
		mRecentAction = MotionEvent.ACTION_CANCEL;
		mMoveToTarget = false;
		
		// Construct vectors
		mTargetPoint = new Vector2();
		mTargetForce = new Vector2();
		mRecentTarget = new Vector2();
		
		mPhysicsBody = physicsBody;
	}
	
	/* ===========================================================
	 * Overrides
	 * =========================================================== */
	
	/** Updates the PlayerControl's coordinates */
	@Override
	public void run() {
		switch(mRecentAction) {
		
		case MotionEvent.ACTION_DOWN:
			mMoveToTarget = true;
		
		case MotionEvent.ACTION_MOVE:
			mTargetPoint.set(mRecentTarget);
			break;
			
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_OUTSIDE:
			mMoveToTarget = false;
			break;
		}
		movePlayer();
	}
	
	/* ===========================================================
	 * Public Methods
	 * =========================================================== */
	
	/** @param physicsBody body to control
	  * @return msController */
	public static PlayerControl startController(final Body physicsBody) {
		if(msController == null) {
			msController = new PlayerControl(physicsBody);
		} else {
			msController.setPhysicsBody(physicsBody);
		}
		return msController;
	}
	
	/** Terminates controls on any sprite */
	public static void endController() {
		startController(null);
	}
	
	/** Helper function that determines the type of action
	 * this controller recognizes
	 * @return true if the action is recognized, else false */
	public static boolean isActionValid(final int action) {
		switch(action) {
		
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_MOVE:
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_OUTSIDE:
			return true;
		
		default:
			return false;
		}
	}
	
	/** Updates coordinates based on touch event */
	public boolean setTouchEvent(final TouchEvent sceneTouchEvent) {
		boolean validAction = false;
		
		if(sceneTouchEvent != null && mPhysicsBody != null) {
			final int action = sceneTouchEvent.getAction();
			validAction = isActionValid(action);
			
			if(validAction) {
				this.setRecentAction(action);
				this.setRecentTarget(sceneTouchEvent.getX(), sceneTouchEvent.getY());
			}
		}
		
		return validAction;
	}
	
	/** Updated the player's position */
	private void movePlayer() {
		if(mMoveToTarget && mPhysicsBody != null) {
			this.pullPlayer();
		}
	}
	
	/* ===========================================================
	 * Getters
	 * =========================================================== */
	
	/** @return msController */
	public static PlayerControl getController() {
		if(msController == null) {
			startController(null);
		}
		return msController;
	}
	
	/** @return mRecentTarget */
	public Vector2 getRecentAction() {
		return mRecentTarget;
	}
	
	/** @return mPhysicsBody */
	public Body getPhysicsBody() {
		return mPhysicsBody;
	}
	
	/* ===========================================================
	 * Setters
	 * =========================================================== */
	
	/** @param action sets mRecentAction */
	public void setRecentAction(final int action) {
		mRecentAction = action;
	}
	
	/** @param targetX sets mRecentTarget's x-coordinate
	  * @param targetY sets mRecentTarget's y-coordinate */
	public void setRecentTarget(final float targetX, final float targetY) {
		mRecentTarget.set(targetX, targetY);
	}
	
	/** @param physicsBody sets mPhysicsBody */
	public void setPhysicsBody(Body physicsBody) {
		mPhysicsBody = physicsBody;
	}
	
	/* ===========================================================
	 * Private/Protected Methods
	 * =========================================================== */
	
	private void pullPlayer() {
		// Determine the amount of force necessary to push the player
		mTargetForce.set(mPhysicsBody.getPosition());
		mTargetForce.set(mTargetForce.x - mTargetPoint.x,
				mTargetForce.y - mTargetPoint.y);
		
		// TODO: proption and limit the amount of force applicable
		// FIXME: normalizing for testing purposes
		final float magnitude = mTargetForce.dst(0, 0);
		mTargetForce.mul(1f/magnitude);
		
		// Apply the force to the body
		mPhysicsBody.applyLinearImpulse(mTargetForce, mTargetPoint);
	}
}
