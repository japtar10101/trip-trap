/**
 * 
 */
package com.games.deception.behavior;

import org.anddev.andengine.engine.handler.IUpdateHandler;

/**
 * TODO: add a description
 * @author japtar10101
 */
public abstract class BaseBehavior implements IUpdateHandler {
	/* ===========================================================
	 * Overrides
	 * =========================================================== */
	
	/**
	 * TODO: add a description
	 * @see org.anddev.andengine.engine.handler.IUpdateHandler#onUpdate(float)
	 */
	@Override
	public void onUpdate(float pSecondsElapsed) {
		if(this.isEnabled()) {
			behave(pSecondsElapsed);
		}
	}

	/* ===========================================================
	 * Abstract Methods
	 * =========================================================== */
	
	/**
	 * TODO: add a description
	 * @return
	 */
	public abstract boolean isEnabled();
	
	/**
	 * TODO: add a description
	 * @param secondsElapsed
	 */
	protected abstract void behave(float secondsElapsed);

}
