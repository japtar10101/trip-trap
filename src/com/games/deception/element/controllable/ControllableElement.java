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
    /** TODO: add a description */
    private Vector2 mTempVector;
    
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
        mTempVector = new Vector2();
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
        final float coord[] = mSprite.convertLocalToSceneCoordinates(
                mSprite.getWidth() * 0.5f, 0);
        return convertArrayToVector(coord);
    }
    
    /**
     * Wrapper for getting the center of the sprite.
     * @return The middle coordinate of a sprite
     */
    public Vector2 getMiddle() {
        final float coord[] = mSprite.getSceneCenterCoordinates();
        return convertArrayToVector(coord);
    }
    
    /**
     * Calculates the bottom (the back) of a sprite, considering
     * both scaling and rotation in mind.
     * @return The back coordinate of a sprite
     */
    public Vector2 getBack() {
        final float coord[] = mSprite.convertLocalToSceneCoordinates(
                mSprite.getWidth() * 0.5f, mSprite.getHeight());
        return convertArrayToVector(coord);
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
    
    /* ===========================================================
     * Private/Protected Methods
     * =========================================================== */
    
    /**
     * Helper function; converts a size 2 array into a vector
     * @param array coordinates to convert
     * @return a vector with the first 2 element of the array,
     * set as coordinates.
     */
    private Vector2 convertArrayToVector(final float[] array) {
        if(array == null || array.length < 2) {
            mTempVector.set(0, 0);
        } else {
            mTempVector.set(array[0], array[1]);
        }
        return mTempVector;
    }
}
