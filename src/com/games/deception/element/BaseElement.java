package com.games.deception.element;

import org.anddev.andengine.entity.sprite.Sprite;

import com.badlogic.gdx.physics.box2d.Body;

public abstract class BaseElement {
    /* ===========================================================
     * Members
     * =========================================================== */
    
    /** Physics of this element. */
    protected Body mPhysicsBody = null;
    /** Sprite of this element. */
    protected Sprite mSprite = null;
    
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
}
