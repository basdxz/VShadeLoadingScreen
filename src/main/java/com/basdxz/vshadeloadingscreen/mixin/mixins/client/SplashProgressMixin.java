package com.basdxz.vshadeloadingscreen.mixin.mixins.client;

import com.basdxz.vshadeloadingscreen.Tags;
import com.basdxz.vshadeloadingscreen.scene.LoadingScreenScene;
import com.falsepattern.lib.api.DependencyLoader;
import com.falsepattern.lib.api.SemanticVersion;
import cpw.mods.fml.client.SplashProgress;
import lombok.*;
import org.lwjgl.opengl.*;
import org.spongepowered.asm.mixin.*;

@Mixin(SplashProgress.class)
public abstract class SplashProgressMixin {
    @Shadow
    private static Drawable d;
    @Shadow
    private static Thread thread;
    @Shadow
    private static boolean enabled;
    @Shadow
    private static volatile boolean done = false;

    /**
     * @author basdxz
     */
    @Overwrite
    @SneakyThrows
    public static void start() {
        d = new SharedDrawable(Display.getDrawable());
        Display.getDrawable().releaseContext();
        d.makeCurrent();

        // Thread.sleep(200);
        DependencyLoader.addMavenRepo("https://repo1.maven.org/maven2/");
        DependencyLoader.builder()
                .loadingModId(Tags.MODID)
                .groupId("org.joml")
                .artifactId("joml")
                .minVersion(new SemanticVersion(1, 10, 0))
                .maxVersion(new SemanticVersion(1, 10, Integer.MAX_VALUE))
                .preferredVersion(new SemanticVersion(1, 10, 2))
                .build();

        LoadingScreenScene.show();
        thread = new Thread(LoadingScreenScene.getInstance());
        thread.start();
        checkThreadState();
    }

    /**
     * @author basdxz
     */
    @Overwrite
    @SneakyThrows
    public static void finish() {
        checkThreadState();
        LoadingScreenScene.hide();
        thread.join();
        d.releaseContext();
        Display.getDrawable().makeCurrent();
    }

    @Shadow
    private static void checkThreadState() {
    }
}
