package com.basdxz.vshadeloadingscreen.mixin.mixins.client;

import com.basdxz.vshadeloadingscreen.Tags;
import com.basdxz.vshadeloadingscreen.scene.LoadingScreenScene;
import com.falsepattern.lib.api.DependencyLoader;
import com.falsepattern.lib.api.SemanticVersion;
import cpw.mods.fml.client.SplashProgress;
import lombok.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(value = SplashProgress.class, remap = false)
public abstract class SplashProgressMixin {
    @Shadow
    private static Thread thread;

    static {
        // TODO Avoid shadow deps, load as much as possible via FalseLib etc
        DependencyLoader.addMavenRepo("https://repo1.maven.org/maven2/");
        DependencyLoader.builder()
                .loadingModId(Tags.MODID)
                .groupId("org.joml")
                .artifactId("joml")
                .minVersion(new SemanticVersion(1, 10, 0))
                .maxVersion(new SemanticVersion(1, 10, Integer.MAX_VALUE))
                .preferredVersion(new SemanticVersion(1, 10, 2))
                .build();
    }

    @Inject(method = "start()V",
            at = @At(value = "INVOKE",
                    shift = At.Shift.AFTER,
                    target = "Ljava/lang/Thread;currentThread ()Ljava/lang/Thread;"),
            cancellable = true,
            require = 1)
    @SneakyThrows
    private static void startInjection(CallbackInfo ci) {
        LoadingScreenScene.show();
        thread = new Thread(LoadingScreenScene.instance());
        thread.start();
        checkThreadState();
        ci.cancel();
    }

    @Shadow
    private static void checkThreadState() {
    }

    @Inject(method = "finish()V",
            at = @At(value = "INVOKE",
                    shift = At.Shift.AFTER,
                    target = "Lcpw/mods/fml/client/SplashProgress;checkThreadState ()V"),
            require = 1)
    private static void finishInjection(CallbackInfo ci) {
        LoadingScreenScene.hide();
    }

    @Inject(method = "finish()V",
            at = @At(value = "INVOKE",
                    shift = At.Shift.AFTER,
                    target = "Lorg/lwjgl/opengl/Drawable;makeCurrent ()V"),
            cancellable = true,
            require = 1)
    private static void cancelFinishTextureDelete(CallbackInfo ci) {
        ci.cancel();
    }
}
