package org.didierdominguez.util;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class ScreenSize {
    private static ScreenSize instance;
    private Rectangle2D screenBounds;

    private ScreenSize() {
        screenBounds = Screen.getPrimary().getBounds();
    }

    public static ScreenSize getInstance() {
        if (instance == null) {
            instance = new ScreenSize();
        }
        return instance;
    }

    public Double getX() {
        return screenBounds.getWidth();
    }

    public Double getY() {
        return screenBounds.getHeight();
    }
}
