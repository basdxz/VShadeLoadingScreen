package com.basdxz.vshadeloadingscreen.scene;

import com.basdxz.vbuffers.common.MemUtils;
import com.basdxz.vbuffers.texture.Texture;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.Objs;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;

public class HexagonsRender {
    protected final ShaderToyShader shader = ShaderToyShader.newHexagons();
    protected final VAOHandler vaoHandler = new VAOHandler(shader);
    protected final Obj model = Objs.create();
    protected final Texture texture;

    {
        initOpenGLDebug();

        vaoHandler.newBuffers(4, 6);

        model.addVertex(0, 0, -0.1F);
        model.addVertex(800, 0, -0.1F);
        model.addVertex(800, 600, -0.1F);
        model.addVertex(0, 600, -0.1F);
        model.addFace(0, 1, 3);
        model.addFace(1, 2, 3);

        vaoHandler.indexBuffer().buffer().asIntBuffer().put(ObjData.getFaceVertexIndices(model));

        ByteBuffer vertBuffer = vaoHandler.vertexBuffer().buffer();

        shader.shaderPeer().vertices(4);
        shader.attributes().position().buffer(vertBuffer).set(MemUtils.getByteBuffer(ObjData.getVertices(model)));

        texture = Texture.loadTexture("/example/notABaseSixtyFourString.png");

        System.out.println(Display.getWidth());
        System.out.println(Display.getHeight());
    }

    public void render() {
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

    private static void initOpenGLDebug() {
        GL11.glEnable(GL43.GL_DEBUG_OUTPUT);
        GL11.glEnable(GL43.GL_DEBUG_OUTPUT_SYNCHRONOUS);
        GL43.glDebugMessageCallback(new KHRDebugCallback());
    }
}
