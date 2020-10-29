package com.laodev.tictic.KeyBoard;

/**
 * Created by AQEEL on 4/2/2019.
 */

public interface KeyboardHeightObserver {
    /**
     * Called when the keyboard height has changed, 0 means keyboard is closed,
     * >= 1 means keyboard is opened.
     *
     * @param height        The height of the keyboard in pixels
     * @param orientation   The orientation either: Configuration.ORIENTATION_PORTRAIT or
     *                      Configuration.ORIENTATION_LANDSCAPE
     */
    void onKeyboardHeightChanged(int height, int orientation);
}
