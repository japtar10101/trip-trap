package com.games.deception.element;

import org.anddev.andengine.entity.sprite.Sprite;

import com.badlogic.gdx.physics.box2d.Body;

public abstract class BaseElement {
	/* ===========================================================
	 * Members
	 * =========================================================== */
	/** Physics of this element. */
	protected Body mPhysicsBody;
	/** Sprite of this element. */
	protected Sprite mSprite;
	
	/* ===========================================================
	 * Getters/Setters
	 * =========================================================== */
	/** @return mPhysicsBody. */
	public Body getPhysicsBody() {
		return mPhysicsBody;
	}
	/** @return mSprite. */
	public Sprite getSprite() {
		return mSprite;
	}
}
