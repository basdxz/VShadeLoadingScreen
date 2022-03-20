package com.basdxz.vshadeloadingscreen.mixin.mixins.client;

import com.basdxz.vshadeloadingscreen.scene.LoadingScreenScene;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

import java.util.concurrent.Semaphore;

@Mixin(value = FMLClientHandler.class, remap = false)
public class FMLClientHandlerMixin {
    @Redirect(method = "processWindowMessages()V",
            at = @At(value = "INVOKE",
                    target = "Ljava/util/concurrent/Semaphore;tryAcquire ()Z"),
            require = 1)
    @SideOnly(Side.CLIENT)
    private boolean redirectMutexTryAcquire(Semaphore instance) {
        return LoadingScreenScene.getMutex().tryAcquire();
    }

    @Redirect(method = "processWindowMessages()V",
            at = @At(value = "INVOKE",
                    target = "Ljava/util/concurrent/Semaphore;release ()V"),
            require = 1)
    @SideOnly(Side.CLIENT)
    private void redirectMutexRelease(Semaphore instance) {
        LoadingScreenScene.getMutex().release();
    }
}
