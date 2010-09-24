package com.games.deception.element.controllable;

import com.badlogic.gdx.math.Vector2;
import com.games.deception.element.BaseElement;

public abstract class ControllableElement extends BaseElement {
	protected byte mHealth;
	private Vector2 mSpriteFront;
	
	public ControllableElement() {
		super();
		mSpriteFront = new Vector2();
	}
	
	public Vector2 getFront() {
		final float coord[] = mSprite.convertLocalToSceneCoordinates(
				mSprite.getWidth() / 2f, 0);
		mSpriteFront.set(coord[0], coord[1]);
		return mSpriteFront;
	}
}
