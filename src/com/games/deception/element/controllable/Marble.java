package com.games.deception.element.controllable;

import org.anddev.andengine.entity.sprite.Sprite;

import com.badlogic.gdx.physics.box2d.Body;

public class Marble extends ControllableElement {
	/**
	 * TODO: add a description
	 * @param sprite
	 * @param physicsBody
	 */
	public Marble(final Sprite sprite, final Body physicsBody) {
		super(0.05f, 20f, (byte) 3);
		mSprite = sprite;
		mPhysicsBody = physicsBody;
	}
}
