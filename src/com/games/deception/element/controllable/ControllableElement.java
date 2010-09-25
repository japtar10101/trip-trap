package com.games.deception.element.controllable;

import com.badlogic.gdx.math.Vector2;
import com.games.deception.element.BaseElement;

/**
 * TODO: add a description
 * @author japtar10101
 */
public abstract class ControllableElement extends BaseElement {
	/* ===========================================================
	 * Constants
	 * =========================================================== */

	/** TODO: add a description */
	public final byte MaxHealth;

	/** TODO: add a description */
	public final float MaxSpeed;
	
	/** TODO: add a description */
	public final float SpeedMultiplier;
	
	/* ===========================================================
	 * Members
	 * =========================================================== */
	
	/** TODO: add a description */
	private byte mHealth;
	
	/* ===========================================================
	 * Constructors
	 * =========================================================== */
	
	/**
	 * TODO: add a description
	 * @param speedMultiplier
	 * @param maxSpeed
	 * @param maxHealth
	 */
	public ControllableElement(final float speedMultiplier,
			final float maxSpeed, final byte maxHealth) {
		super();
		
		// Set constants
		SpeedMultiplier = speedMultiplier;
		MaxSpeed = maxSpeed;
		MaxHealth = maxHealth;
		
		// Set member variables
		mHealth = maxHealth;
	}
	
	/* ===========================================================
	 * Public Methods
	 * =========================================================== */
	
	/**
	 * TODO: add a description
	 * @param add
	 * @return
	 */
	public byte addHealth(final byte add) {
		// Increment health ONLY if add is positive
		if(add > 0) {
			final byte origHealth = mHealth;
			mHealth += add;
			
			// if health is greater than max health, OR
			// through overflowing, the health is less than
			// or equal to its previous value, set health to
			// MaxHealth
			if(mHealth > MaxHealth || mHealth <= origHealth) {
				mHealth = MaxHealth;
			}
		}
		return mHealth;
	}
	
	/**
	 * TODO: add a description
	 * @param add
	 * @return
	 */
	public byte subHealth(final byte sub) {
		// Increment health ONLY if add is positive
		if(sub > 0) {
			final byte origHealth = mHealth;
			mHealth -= sub;
			
			// if health is less than 0, OR
			// through overflowing, the health is greater than
			// or equal to its previous value, set health to 0
			if(mHealth < 0 || mHealth >= origHealth) {
				mHealth = 0;
			}
		}
		return mHealth;
	}
	
	/**
	 * Calculates the top (the front) of a sprite, considering
	 * both scaling and rotation in mind.
	 * @return The front coordinate of a sprite
	 */
	public Vector2 getFront() {
		return convertLocalToGlobalCoord(
				mSprite.getWidth() * 0.5f, 0);
	}
	
	/**
	 * Calculates the bottom (the back) of a sprite, considering
	 * both scaling and rotation in mind.
	 * @return The back coordinate of a sprite
	 */
	public Vector2 getBack() {
		return convertLocalToGlobalCoord(
				mSprite.getWidth() * 0.5f, mSprite.getHeight());
	}
	
	/* ===========================================================
	 * Getters
	 * =========================================================== */
	
	/**
	 * @return {link @mHealth}
	 */
	public byte getHealth() {
		return mHealth;
	}
}
