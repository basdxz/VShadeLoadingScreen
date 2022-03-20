package com.basdxz.vshadeloadingscreen.scene;

import lombok.*;
import org.lwjgl.opengl.*;

import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class DisplayResizeHandler {
    protected final BiConsumer<Integer, Integer> onResize;
    protected int oldWidth = -1;
    protected int oldWeight = -1;

    public void checkForResize() {
        val newWidth = Display.getWidth();
        val newHeight = Display.getHeight();

        if (newWidth != oldWidth || newHeight != oldWeight) {
            onResize.accept(newWidth, newHeight);
            oldWidth = newWidth;
            oldWeight = newHeight;
        }
    }
}
