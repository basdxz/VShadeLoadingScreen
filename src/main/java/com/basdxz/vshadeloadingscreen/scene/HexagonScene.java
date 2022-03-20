package com.basdxz.vshadeloadingscreen.scene;


import com.basdxz.vshade.example.Profiler;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.Objs;
import lombok.*;
import org.joml.Vector3f;
import org.lwjgl.opengl.*;

public class HexagonScene {
    protected final int width = 800;
    protected final int height = 600;

    protected final ShaderToyShader shader = ShaderToyShader.newHexagons();
    protected final VAOHandler vaoHandler = new VAOHandler(shader);
    protected final Obj model = Objs.create();
    //protected final Texture texture;

    public void reset() {
        setupCamera();
        setupRender();
        setupGeometry();
        setupGLFlags();
    }

    protected void setupCamera() {
        //camera = new Camera(width, height);
        //camera.updateProjection();
    }

    protected void setupRender() {
    }

    @SneakyThrows
    protected void setupGeometry() {
        initOpenGLDebug();

        model.addVertex(0, 0, -0.1F);
        model.addVertex(800, 0, -0.1F);
        model.addVertex(800, 600, -0.1F);
        model.addVertex(0, 600, -0.1F);
        model.addFace(0, 1, 3);
        model.addFace(1, 2, 3);

        vaoHandler.newBuffers(model.getNumVertices(), model.getNumFaces() * 3);
        vaoHandler.indexBuffer().buffer().asIntBuffer().put(ObjData.getFaceVertexIndices(model));

        val vertBuffer = vaoHandler.vertexBuffer().buffer();

        shader.attributes().position().buffer(vertBuffer).blocks(model.getNumVertices()).set(ObjData.getVertices(model));

        shader.shaderToyUniforms().iResolution().set(new Vector3f(800, 600, 800 / 600F));
    }

    public void setupGLFlags() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    public void update(Profiler profiler) {
        //((ShaderToy) render.shader()).shaderToyUniforms().iTime().set(profiler.runningSeconds());
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        //render.doRender();
        profiler.updateMS();
    }

    private static void initOpenGLDebug() {
        GL11.glEnable(GL43.GL_DEBUG_OUTPUT);
        GL11.glEnable(GL43.GL_DEBUG_OUTPUT_SYNCHRONOUS);
        GL43.glDebugMessageCallback(new KHRDebugCallback());
    }
}
