package com.games.deception;

import org.anddev.andengine.input.touch.TouchEvent;

import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class PlayerControl implements Runnable {
	/* ===========================================================
	 * Members
	 * =========================================================== */
	
	// Temporary variables
	private int mRecentAction;
	private final Vector2 mRecentTarget;
	
	// Variables to update at run
	private boolean mMoveToTarget;
	private final Vector2 mTargetPoint;
	private final Vector2 mTargetForce;
	
	/** The body affected by player's action */
	private Body mPhysicsBody;
	
	/* ===========================================================
	 * Constructors
	 * =========================================================== */
	
	public PlayerControl() {
		this(null);
	}
	
	public PlayerControl(final Body physicsBody) {
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
	
	@Override
	public void run() {
		switch(mRecentAction) {
		
		case MotionEvent.ACTION_DOWN:
			mMoveToTarget = true;
		
		case MotionEvent.ACTION_MOVE:
			mTargetPoint.set(mRecentTarget);
			break;
			
		case MotionEvent.ACTION_UP:
			mMoveToTarget = false;
			break;
			
		}
		// TODO Auto-generated method stub

	}
	
	/* ===========================================================
	 * Public Methods
	 * =========================================================== */
	
	public void setTouchEvent(final TouchEvent sceneTouchEvent) {
		if(sceneTouchEvent != null) {
			mRecentAction = sceneTouchEvent.getAction();
			mRecentTarget.set(sceneTouchEvent.getX(), sceneTouchEvent.getY());
		}
	}
	
	/* ===========================================================
	 * Private/Protected Methods
	 * =========================================================== */
	
	private void pullPlayer() {
		
	}
	/*
	private void pullPlayer(final float targetX, final float targetY) {
		// Force the velocity to be "pulled" into the direction touched
		mTempPoint.set(targetX, targetY);
		mTempForce.set(
				targetX - mPlayer.getX(),
				targetY - mPlayer.getY());
		
		// Grab the body associated with the player
		final Body faceBody = this.mPhysicsWorld.
			getPhysicsConnectorManager().findBodyByShape(this.mPlayer);
		
		// Pull the player
		faceBody.applyLinearImpulse(this.mTempForce, this.mTempPoint);
	}
	*/

}
