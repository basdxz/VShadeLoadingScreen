package com.basdxz.vshadeloadingscreen.mixin.mixins.client;

import com.basdxz.vshadeloadingscreen.scene.LoadingScreenScene;
import cpw.mods.fml.client.FMLClientHandler;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.*;
import org.spongepowered.asm.mixin.*;

@Mixin(value = FMLClientHandler.class, remap = false)
public class FMLClientHandlerMixin {

    /**
     * @author basdxz
     */
    @Overwrite
    public void processWindowMessages() {
        if (LWJGLUtil.getPlatform() != LWJGLUtil.PLATFORM_WINDOWS)
            return;
        if (!LoadingScreenScene.getMutex().tryAcquire())
            return;
        Display.processMessages();
        LoadingScreenScene.getMutex().release();
    }
}
