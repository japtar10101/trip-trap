/**
 * 
 */
package com.games.deception.behavior;

import java.util.HashSet;
import java.util.Set;

import org.anddev.andengine.entity.layer.ILayer;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.games.deception.constant.GamePhysics;
import com.games.deception.element.BaseElement;
import com.games.deception.element.controllable.ControllableElement;

/**
 * TODO: add a description
 * @author japtar10101
 */
public class HarmBehavior implements ContactListener {
	/* ===========================================================
	 * Constants
	 * =========================================================== */
	private final Runnable mcRemoveElements = new Runnable() {
		@Override
		public void run() {
			PhysicsConnector facePhysicsConnector;
			for(ControllableElement removeElement : mRemoveElements) {
				facePhysicsConnector = GamePhysics.PHYSICS_WORLD.
					getPhysicsConnectorManager().findPhysicsConnectorByShape(
							removeElement.getSprite());

				GamePhysics.PHYSICS_WORLD.unregisterPhysicsConnector(facePhysicsConnector);
				GamePhysics.PHYSICS_WORLD.destroyBody(facePhysicsConnector.getBody());
				
				mSpriteResource.removeEntity(removeElement.getSprite());
			}
			mVulnerableElements.removeAll(mRemoveElements);
			mRemoveElements.clear();
		}
	};
	
	/* ===========================================================
	 * Members
	 * =========================================================== */
	private final Set<ControllableElement> mVulnerableElements;
	private final Set<BaseElement> mHarmfulElements;
	private final Set<ControllableElement> mRemoveElements;
	private final ILayer mSpriteResource;
	private final BaseGameActivity mActivity;

	/* ===========================================================
	 * Constructors
	 * =========================================================== */
	public HarmBehavior(BaseGameActivity activity, ILayer spriteLayer) {
		mVulnerableElements = new HashSet<ControllableElement>();
		mHarmfulElements = new HashSet<BaseElement>();
		mRemoveElements = new HashSet<ControllableElement>(); 
		mSpriteResource = spriteLayer;
		mActivity = activity;
	}
	
	/* ===========================================================
	 * Overrides
	 * =========================================================== */
	@Override
	public void beginContact(final Contact pContact) {
		Body fixA = pContact.getFixtureA().getBody();
		Body fixB = pContact.getFixtureB().getBody();
		
		Body harmBody = null;
		
		// Figure out if the harmful element is in the set
		// TODO: make the list retain Fixtures, instead.
		for(BaseElement harm : mHarmfulElements) {
			if(harm.equals(fixA)) {
				harmBody = fixA;
				break;
			} else if(harm.equals(fixB)) {
				harmBody = fixB;
				break;
			}
		}
		
		if(harmBody != null) {
			for(ControllableElement vulnerable : mVulnerableElements) {
				if(mRemoveElements.contains(vulnerable)) {
					// Skip elements to be removed
					continue;
				} else if(vulnerable.equals(fixA) || vulnerable.equals(fixB)) {
					/*
					// Decrement health
					vulnerable.addHealth((byte) -1);
					
					// If health is at 0, remove this element
					if(vulnerable.getHealth() <= 0) {
					*/
						mRemoveElements.add(vulnerable);
					//}
					break;
				}
			}
		}
		
		if(!mRemoveElements.isEmpty()) {
			mActivity.runOnUpdateThread(mcRemoveElements);
		}
	}

	@Override
	public void endContact(final Contact pContact) {}
	
	/* ===========================================================
	 * Public Methods
	 * =========================================================== */

	public void addVulnerableElement(final ControllableElement ... elements) {
		for(ControllableElement ele : elements) {
			mVulnerableElements.add(ele);
		}
	}
	
	public void addHarmfulElement(final BaseElement ... elements) {
		for(BaseElement ele : elements) {
			mHarmfulElements.add(ele);
		}
	}
	
	public void reset() {
		mVulnerableElements.clear();
		mHarmfulElements.clear();
		mRemoveElements.clear();
	}
	
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
