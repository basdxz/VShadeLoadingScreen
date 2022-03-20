package com.basdxz.vshadeloadingscreen.scene;

import com.basdxz.vshade.example.Profiler;
import lombok.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.*;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class LoadingScreenScene implements Runnable {
    private static final int FPS_LIMIT = 100;

    @Getter
    private static final LoadingScreenScene instance = new LoadingScreenScene();
    @Getter
    private static final Semaphore mutex = new Semaphore(1);
    private static final Lock lock = new ReentrantLock(true);
    private static final Profiler profiler = new Profiler();
    private static final HexagonScene scene = new HexagonScene(800, 600);
    private static boolean visible = false;

    @Override
    public void run() {
        setGL();
        scene.reset();
        while (visible) {
            render();
            mutex.acquireUninterruptibly();
            Display.update();
            mutex.release();
            Display.sync(FPS_LIMIT);
        }
        clearGL();
    }

    public static void render() {
        profiler.updateTime();
        scene.update(profiler);
    }

    @SneakyThrows
    private static void setGL() {
        lock.lock();
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
        lock.unlock();
    }

    public static void show() {
        visible = true;
    }

    public static void hide() {
        visible = false;
    }
}
