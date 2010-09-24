package com.games.deception.constant;

import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.math.Vector2;

public interface GamePhysics {
	/* ===========================================================
	 * Constants
	 * =========================================================== */
	public static final PhysicsWorld PHYSICS_WORLD =
		new PhysicsWorld(new Vector2(0,0), true);
}
