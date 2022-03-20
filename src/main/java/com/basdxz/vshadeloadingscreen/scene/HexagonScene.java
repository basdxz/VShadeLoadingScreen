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

public class HexagonScene extends SimpleScene {
    public HexagonScene(int width, int height) {
        super(width, height);
    }

    protected void setupRender() {
        render = Render.builder()
                //.shader(BasicShader.builder()
                //        .source(ShaderSource
                //                .newVertex(ResourceHelper.readResourceAsString("/zerohero/shaders/basic.vert")))
                //        .source(ShaderSource
                //                .newFragment(ResourceHelper.readResourceAsString("/zerohero/shaders/basic.frag"))
                //        ).name("Some Default Shader")
                //        .build().init())
                .shader(ShaderToy.builder()
                        .source(ShaderSource
                                .newVertex(ResourceHelper.readResourceAsString("/example/hexagon.vert")))
                        .source(ShaderSource
                                .newFragment(ResourceHelper.readResourceAsString("/example/hexagon.frag"))
                        ).name("Hexagons are the bestagons or something lmao")
                        .build().init())
                .camera(camera)
                //.texture(Texture.loadTexture("/zerohero/textures/antique_ceramic_vase_01_diff_4k.png"))
                .texture(Texture.loadTexture("/example/notABaseSixtyFourString.png"))
                //.modelTransform(ModelTransform.builder()
                //        .position(new Vector3f(0.0F, -0.3F, -1.0F))
                //        .scale(new Vector3f(0.2F))
                //        .rotation(new Quaternionf(Math.PI / 16, Math.PI / 8, 0.0F, 1.0F))
                //        .build())
                .modelTransform(ModelTransform.builder()
                        .position(new Vector3f(0.0F, 0.0F, -0.1F))
                        .build())
                .build();

        render.init();
    }

    //@Override
    //@SneakyThrows
    //protected void setupGeometry() {
    //    val obj = ObjUtils.convertToRenderable(ObjReader.read(
    //            ResourceHelper.readResourceAsInputStream("/zerohero/models/antique_ceramic_vase_01_4k.obj")));
//
    //    render.vaoHandler().newBuffers(obj.getNumVertices(), obj.getNumFaces() * 3);
    //    render.vaoHandler().indexBuffer().buffer().asIntBuffer().put(ObjData.getFaceVertexIndices(obj));
//
    //    val vertBuffer = render.vaoHandler().vertexBuffer().buffer();
//
    //    render.shader().attributes().position().buffer(vertBuffer)
    //            .put(MemUtils.getByteBuffer(ObjData.getVertices(obj)));
    //    render.shader().attributes().texture().buffer(render.vaoHandler().vertexBuffer().buffer())
    //            .put(MemUtils.getByteBuffer(ObjData.getTexCoords(obj, 2, true)));
    //}

    @Override
    @SneakyThrows
    protected void setupGeometry() {
        val obj = Objs.create();

        obj.addVertex(0, 0, 0);
        obj.addVertex(800, 0, 0);
        obj.addVertex(800, 600, 0);
        obj.addVertex(0, 600, 0);
        obj.addFace(0, 1, 3);
        obj.addFace(1, 2, 3);

        System.out.println("Created Obj " + obj);

        render.vaoHandler().newBuffers(obj.getNumVertices(), obj.getNumFaces() * 3);
        render.vaoHandler().indexBuffer().buffer().asIntBuffer().put(ObjData.getFaceVertexIndices(obj));

        val vertBuffer = render.vaoHandler().vertexBuffer().buffer();

        render.shader().attributes().position().buffer(vertBuffer).blocks(obj.getNumVertices())
                .set(MemUtils.getByteBuffer(ObjData.getVertices(obj)));
        //render.shader().attributes().texture().buffer(render.vaoHandler().vertexBuffer().buffer())
        //        .put(MemUtils.getByteBuffer(ObjData.getTexCoords(obj, 2, true)));


        ((ShaderToy) render.shader()).shaderToyUniforms().iResolution().set(new Vector3f(800, 600, 800 / 600F));
    }

    @Override
    public void update(Profiler profiler) {
        ((ShaderToy) render.shader()).shaderToyUniforms().iTime().set(profiler.runningSeconds());
        super.update(profiler);
    }
}
