package com.games.deception.constant;

import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;

public interface GameDimension {
    /* ===========================================================
     * Constants
     * =========================================================== */
    // TODO: fix the Camera height and width to the actual height and width of the screen
    public static final int CAMERA_HEIGHT = 480;
    public static final int CAMERA_WIDTH = 720;
    public static final ScreenOrientation ORIENTATION =
        ScreenOrientation.PORTRAIT;
}
