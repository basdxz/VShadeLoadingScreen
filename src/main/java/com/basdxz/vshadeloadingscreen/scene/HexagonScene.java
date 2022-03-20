package com.basdxz.vshadeloadingscreen.scene;


import com.basdxz.vbuffers.texture.Texture;
import lombok.*;
import org.joml.Vector3f;
import org.lwjgl.opengl.*;

public class HexagonScene {
    protected static final float Z_OFFSET = -0.1f;
    protected static final int VERTS = 4;
    protected static final int[] RECTANGLE_INDICES = new int[]{0, 1, 3, 1, 2, 3};

    protected final Vector3f vertA = new Vector3f();
    protected final Vector3f vertB = new Vector3f();
    protected final Vector3f vertC = new Vector3f();
    protected final Vector3f vertD = new Vector3f();

    protected DisplayResizeHandler displayResizeHandler;
    protected ShaderToyShader shader;
    protected VAOHandler vaoHandler;
    protected Texture texture;

    @SneakyThrows
    public void reset() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);

        initOpenGLDebug();

        displayResizeHandler = new DisplayResizeHandler(this::onResize);
        shader = ShaderToyShader.newHexagons();
        vaoHandler = new VAOHandler(shader);

        vaoHandler.newBuffers(4, RECTANGLE_INDICES.length);
        vaoHandler.indexBuffer().buffer().asIntBuffer().put(RECTANGLE_INDICES);
        val vertBuffer = vaoHandler.vertexBuffer().buffer();
        shader.attributes().position().buffer(vertBuffer).blocks(VERTS);
        texture = Texture.loadTexture("/assets/vshadeloadingscreen/texture/AbstactBG1.png");
    }

    public void update() {
        displayResizeHandler.checkForResize();

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        shader.uniforms().update();
        shader.shaderToyUniforms().update();

        texture.bind(0);
        vaoHandler.bind();
        shader.bind();
        GL11.glDrawElements(GL11.GL_TRIANGLES, vaoHandler.indices(), GL11.GL_UNSIGNED_INT, GL11.GL_NONE);
        shader.unbind();
        vaoHandler.unbind();
        Texture.unbind(0);
    }

    protected void onResize(int width, int height) {
        shader.attributes().position().set(0, 0, vertA.set(0, 0, Z_OFFSET));
        shader.attributes().position().set(1, 0, vertB.set(width, 0, Z_OFFSET));
        shader.attributes().position().set(2, 0, vertC.set(width, height, Z_OFFSET));
        shader.attributes().position().set(3, 0, vertD.set(0, height, Z_OFFSET));
    }

    private static void initOpenGLDebug() {
        GL11.glEnable(GL43.GL_DEBUG_OUTPUT);
        GL11.glEnable(GL43.GL_DEBUG_OUTPUT_SYNCHRONOUS);
        GL43.glDebugMessageCallback(new KHRDebugCallback());
    }
}
