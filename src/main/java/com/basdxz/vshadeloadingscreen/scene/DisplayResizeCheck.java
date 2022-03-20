package com.basdxz.vshadeloadingscreen.scene;

import org.lwjgl.opengl.*;

public class DisplayResizeCheck {
    protected int width = -1;
    protected int height = -1;

    public boolean hasResized() {
        if (width != Display.getWidth() || height != Display.getWidth()) {
            width = Display.getWidth();
            height = Display.getWidth();
            return true;
        }
        return false;
    }
}
