/**
 * 
 */
package com.games.deception.element;

import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.games.deception.constant.GamePhysics;

/**
 * TODO: add a description
 * @author japtar10101
 */
public class Wall extends ResizableElement {
	/* ===========================================================
	 * Constants
	 * =========================================================== */
	private final FixtureDef FIXTURE = PhysicsFactory.createFixtureDef(
			0, 0.5f, 0.5f);

	/* ===========================================================
	 * Members
	 * =========================================================== */

	/* ===========================================================
	 * Constructors
	 * =========================================================== */
	public Wall(float x, float y, float width, float height) {
		this.mSprite = new Rectangle(x, y, width, height);
		this.mPhysicsBody = PhysicsFactory.createBoxBody(
				GamePhysics.PHYSICS_WORLD, this.mSprite,
				BodyType.StaticBody, FIXTURE);
	}

	/* ===========================================================
	 * Overrides
	 * =========================================================== */

	/* ===========================================================
	 * Public Methods
	 * =========================================================== */

	/* ===========================================================
	 * Getters
	 * =========================================================== */

	/* ===========================================================
	 * Setters
	 * =========================================================== */

	/* ===========================================================
	 * Private/Protected Methods
	 * =========================================================== */

}
