package com.basdxz.vshadeloadingscreen.scene;

import com.basdxz.vshade.example.ModelScene;
import com.basdxz.vshade.example.Profiler;
import lombok.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.*;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class LoadingScreenScene implements Runnable {
    private static final LoadingScreenScene INSTANCE = new LoadingScreenScene();
    private static final int FPS_LIMIT = 100;

    private boolean visible = false;
    private final Profiler profiler = new Profiler();

    @Override
    public void run() {
        setGL();
        val scene = new ModelScene(800, 600);
        scene.reset();
        while (visible) {
            scene.update(profiler);
            Display.update();
            Display.sync(FPS_LIMIT);
            profiler.updateTime();
        }
        clearGL();
    }

    @SneakyThrows
    private static void setGL() {
        Display.getDrawable().makeCurrent();
        GL11.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    @SneakyThrows
    private void clearGL() {
        Minecraft mc = Minecraft.getMinecraft();
        mc.displayWidth = Display.getWidth();
        mc.displayHeight = Display.getHeight();
        mc.resize(mc.displayWidth, mc.displayHeight);
        GL11.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        Display.getDrawable().releaseContext();
    }

    public static LoadingScreenScene getInstance() {
        return INSTANCE;
    }

    public static void show() {
        INSTANCE.visible = true;
    }

    public static void hide() {
        INSTANCE.visible = false;
    }
}
