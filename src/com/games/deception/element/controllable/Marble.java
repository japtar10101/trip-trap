package com.games.deception.element.controllable;

import org.anddev.andengine.entity.sprite.Sprite;

import com.badlogic.gdx.physics.box2d.Body;

public class Marble extends ControllableElement {
	public Marble(final Sprite sprite, final Body physicsBody) {
		mSprite = sprite;
		mPhysicsBody = physicsBody;
		
		mHealth = 3;
	}
}
