package com.basdxz.vshadeloadingscreen.scene;

import lombok.*;
import lombok.experimental.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.*;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static lombok.AccessLevel.PRIVATE;

@Accessors(fluent = true, chain = true)
@NoArgsConstructor(access = PRIVATE)
public final class LoadingScreenScene implements Runnable {
    private static final int FPS_LIMIT = 100;

    @Getter
    private static final LoadingScreenScene instance = new LoadingScreenScene();
    @Getter
    private static final Semaphore mutex = new Semaphore(1);
    private static final Lock lock = new ReentrantLock(true);
    private static final DisplayResizeHandler displayResizeHandler = new DisplayResizeHandler(LoadingScreenScene::onResize);
    private static final HexagonScene scene = new HexagonScene();
    private static boolean visible = false;

    @Override
    public void run() {
        setGL();
        scene.reset();
        while (visible) {
            displayResizeHandler.checkForResize();
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            render();
            mutex.acquireUninterruptibly();
            Display.update();
            mutex.release();
            Display.sync(FPS_LIMIT);
        }
        clearGL();
    }

    private static void onResize(int width, int height) {
        GL11.glViewport(0, 0, width, height);
    }

    public static void render() {
        scene.update();
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
