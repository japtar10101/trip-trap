/**
 * 
 */
package com.games.deception.behavior;

import java.util.LinkedList;

import org.anddev.andengine.engine.handler.IUpdateHandler;

import com.badlogic.gdx.math.Vector2;
import com.games.deception.element.controllable.ControllableElement;

/**
 * TODO: Consider turning this into a set
 * 
 * TODO: This also shares similarities to PullPlayerController
 * @author japtar10101
 */
public class AttractToControllableBehavior implements
		IUpdateHandler {
	/* ===========================================================
	 * Members
	 * =========================================================== */
	
	private ControllableElement mAttractedTo;
	final private LinkedList<ControllableElement> mBehavingElements;
	final Vector2 mTempVector = new Vector2();

	/* ===========================================================
	 * Constructors
	 * =========================================================== */
	
	public AttractToControllableBehavior(final ControllableElement attraction) {
		mAttractedTo = attraction;
		mBehavingElements = new LinkedList<ControllableElement>();
	}

	/* ===========================================================
	 * Overrides
	 * =========================================================== */
	
	/**
	 * TODO: add a description
	 * @see org.anddev.andengine.engine.handler.IUpdateHandler#onUpdate(float)
	 */
	@Override
	public void onUpdate(float pSecondsElapsed) {
		if(mAttractedTo != null) {
			attraction();
		}
	}
	/**
	 * TODO: add a description
	 * @see org.anddev.andengine.engine.handler.IUpdateHandler#reset()
	 */
	@Override
	public void reset() {
		mAttractedTo = null;
		mBehavingElements.clear();
	}
	
	/* ===========================================================
	 * Public Methods
	 * =========================================================== */
	
	public void addElement(final ControllableElement ele) {
		mBehavingElements.add(ele);
	}

	public void removeElement(final ControllableElement ele) {
		mBehavingElements.remove(ele);
	}
	
	public void clearElements() {
		mBehavingElements.clear();
	}
	
	/* ===========================================================
	 * Getters
	 * =========================================================== */
	
	/**
	 * @return {@link mAttractedTo}
	 */
	public ControllableElement getAttractedTo() {
		return mAttractedTo;
	}

	/**
	 * @return {@link mBehavingElements}
	 */
	public LinkedList<ControllableElement> getBehavingElements() {
		return mBehavingElements;
	}

	/* ===========================================================
	 * Setters
	 * =========================================================== */
	
	/**
	 * @param AttractedTo sets {@link mAttractedTo}
	 */
	public void setAttractedTo(ControllableElement AttractedTo) {
		this.mAttractedTo = AttractedTo;
	}
	
	/* ===========================================================
	 * Private/Protected Methods
	 * =========================================================== */
	
	private void attraction() {
		final Vector2 attractionPos = mAttractedTo.getMiddle();
		for(ControllableElement ele : mBehavingElements) {
			// TODO: refactor this part to its own "attraction" class
			mTempVector.set(attractionPos);
			mTempVector.sub(ele.getFront());
			
			// lower the magnitude of the force
			float magnitude = mTempVector.dst(0, 0);
			magnitude *= ele.SpeedMultiplier;
			if(magnitude > ele.MaxSpeed) {
				magnitude = ele.MaxSpeed;
			}
			
			// Apply the magnitude to force
			if(Float.compare(magnitude, 0.001f) <= 0) {
				mTempVector.set(0, 0);
			} else {
				mTempVector.nor();
				mTempVector.mul(magnitude);
			}
				
			// Apply force to the body, if force is greater than 0
			if(Float.compare(mTempVector.x, 0) != 0 &&
					Float.compare(mTempVector.y, 0) != 0) {
				ele.getPhysicsBody().applyLinearImpulse(
						mTempVector, ele.getMiddle());
			}
		}
	}
}
