package com.basdxz.vshadeloadingscreen.scene;


import com.basdxz.vbuffers.common.MemUtils;
import com.basdxz.vbuffers.common.ResourceHelper;
import com.basdxz.vbuffers.texture.Texture;
import com.basdxz.vshade.example.*;
import com.basdxz.vshade.shader.ShaderSource;
import de.javagl.obj.ObjData;
import de.javagl.obj.Objs;
import lombok.*;
import org.joml.Vector3f;
import org.lwjgl.opengl.*;

public class HexagonScene {
    protected final int width;
    protected final int height;

    protected Camera camera = null;
    protected CameraController cameraController = null;
    protected Render render = null;

    public HexagonScene(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void reset() {
        setupCamera();
        setupRender();
        setupGeometry();
        setupGLFlags();
    }

    protected void setupCamera() {
        camera = new Camera(width, height);
        camera.updateProjection();
        cameraController = new CameraController(camera);
    }

    protected void setupRender() {
        render = Render.builder()
                .shader(ShaderToy.builder()
                        .source(ShaderSource
                                .newVertex(ResourceHelper.readResourceAsString("/example/hexagon.vert")))
                        .source(ShaderSource
                                .newFragment(ResourceHelper.readResourceAsString("/example/hexagon.frag"))
                        ).name("Hexagons are the bestagons or something lmao")
                        .build().init())
                .camera(camera)
                .texture(Texture.loadTexture("/example/notABaseSixtyFourString.png"))
                .modelTransform(ModelTransform.builder()
                        .position(new Vector3f(0.0F, 0.0F, -0.1F))
                        .build())
                .build();

        render.init();
    }

    @SneakyThrows
    protected void setupGeometry() {
        val obj = Objs.create();

        obj.addVertex(0, 0, 0);
        obj.addVertex(800, 0, 0);
        obj.addVertex(800, 600, 0);
        obj.addVertex(0, 600, 0);
        obj.addFace(0, 1, 3);
        obj.addFace(1, 2, 3);

        render.vaoHandler().newBuffers(obj.getNumVertices(), obj.getNumFaces() * 3);
        render.vaoHandler().indexBuffer().buffer().asIntBuffer().put(ObjData.getFaceVertexIndices(obj));

        val vertBuffer = render.vaoHandler().vertexBuffer().buffer();

        render.shader().attributes().position().buffer(vertBuffer).blocks(obj.getNumVertices()).set(MemUtils.getByteBuffer(ObjData.getVertices(obj)));

        ((ShaderToy) render.shader()).shaderToyUniforms().iResolution().set(new Vector3f(800, 600, 800 / 600F));
    }

    public void setupGLFlags() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    public void update(Profiler profiler) {
        ((ShaderToy) render.shader()).shaderToyUniforms().iTime().set(profiler.runningSeconds());
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        render.doRender();
        profiler.updateMS();
    }
}
