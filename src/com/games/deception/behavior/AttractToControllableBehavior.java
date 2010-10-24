/**
 * 
 */
package com.games.deception.behavior;

import java.util.LinkedList;

import com.badlogic.gdx.math.Vector2;
import com.games.deception.element.controllable.ControllableElement;

/**
 * TODO: Consider turning this into a set
 * 
 * TODO: This also shares similarities to PullPlayerController
 * @author japtar10101
 */
public class AttractToControllableBehavior extends BaseBehavior {
	/* ===========================================================
	 * Constants
	 * =========================================================== */
	
	final private LinkedList<ControllableElement> mBehavingElements =
		new LinkedList<ControllableElement>();
	final private Vector2 mTempVector = new Vector2();

	/* ===========================================================
	 * Members
	 * =========================================================== */
	
	private ControllableElement mAttractedTo;

	/* ===========================================================
	 * Constructors
	 * =========================================================== */
	
	public AttractToControllableBehavior(final ControllableElement attraction) {
		mAttractedTo = attraction;
	}

	/* ===========================================================
	 * Overrides
	 * =========================================================== */
	
	/**
	 * TODO: add a description
	 * @see org.anddev.andengine.engine.handler.IUpdateHandler#reset()
	 */
	@Override
	public void reset() {
		mAttractedTo = null;
		mBehavingElements.clear();
	}
	
	@Override
	public boolean isEnabled() {
		return mAttractedTo != null;
	}
	
	@Override
	protected void behave(float secondsElapsed) {
		
		// Find the attraction point
		final Vector2 attractionPos = mAttractedTo.getMiddle();
		
		// Move every element
		for(final ControllableElement element : mBehavingElements) {
			
			// Check the distance of target to element
			mTempVector.set(element.getFront());
			if(Float.compare(mTempVector.dst(attractionPos), 0.01f) >= 0) {
				
				// Calculate the direction
				mTempVector.x = attractionPos.x - mTempVector.x;
				mTempVector.y = attractionPos.y - mTempVector.y;
				
				// Apply maximum force
				mTempVector.nor();
				mTempVector.mul(element.MaxSpeed);
				
				// Apply force to the body, if force is greater than 0
				element.getPhysicsBody().applyLinearImpulse(
						mTempVector, element.getMiddle());
			}
		}
	}
	/* ===========================================================
	 * Public Methods
	 * =========================================================== */
	
	public void addElement(final ControllableElement ... elements) {
		for(ControllableElement ele : elements) {
			mBehavingElements.add(ele);
		}
	}

	public void removeElement(final ControllableElement ... elements) {
		for(ControllableElement ele : elements) {
			mBehavingElements.remove(ele);
		}
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
}
