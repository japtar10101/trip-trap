package com.games.deception.element;

import org.anddev.andengine.entity.sprite.Sprite;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class BaseElement {
	/* ===========================================================
	 * Members
	 * =========================================================== */
	
	/** Physics of this element. */
	protected Body mPhysicsBody = null;
	
	/** Sprite of this element. */
	protected Sprite mSprite = null;
	
	/** TODO: add a description */
	final private Vector2 mTempVector = new Vector2();
	
	/* ===========================================================
	 * Public Methods
	 * =========================================================== */
	
	/**
	 * Wrapper for getting the center of the sprite.
	 * @return The middle coordinate of a sprite
	 */
	public Vector2 getMiddle() {
		return convertLocalToGlobalCoord(
				mSprite.getWidth() * 0.5f, mSprite.getHeight() * 0.5f);
	}
	
	/* ===========================================================
	 * Getters/Setters
	 * =========================================================== */
	
	/** @return {@link mPhysicsBody}. */
	public Body getPhysicsBody() {
		return mPhysicsBody;
	}
	/** @return {@link mSprite}. */
	public Sprite getSprite() {
		return mSprite;
	}
	
	/* ===========================================================
	 * Private/Protected Methods
	 * =========================================================== */
	
	/**
	 * Helper function; converts a size 2 array into a vector
	 * @param array coordinates to convert
	 * @return a vector with the first 2 element of the array,
	 * set as coordinates.
	 */
	protected Vector2 convertLocalToGlobalCoord(final float x, final float y) {
		final float coord[] = mSprite.convertLocalToSceneCoordinates(x, y);
		mTempVector.set(coord[0], coord[1]);
		return mTempVector;
	}
}
