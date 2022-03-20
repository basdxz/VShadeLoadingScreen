package com.basdxz.vshadeloadingscreen.scene;

import com.basdxz.vbuffers.common.Disposable;
import com.basdxz.vshade.shader.ShaderPeerProvider;
import lombok.*;
import lombok.experimental.*;
import org.lwjgl.opengl.*;

import java.util.function.BiConsumer;

@Getter
@Accessors(fluent = true, chain = true)
public class VAOHandler implements Disposable {
    protected final int stride;
    protected final BiConsumer<Integer, Integer> vboFormat;

    protected final int vao;
    protected int vertices;
    protected int indices;
    protected GraphicsMappedBuffer vertexBuffer;
    protected GraphicsMappedBuffer indexBuffer;

    public VAOHandler(ShaderPeerProvider peerProvider) {
        val shaderPeer = peerProvider.shaderPeer();
        this.stride = shaderPeer.vertexStride();
        this.vboFormat = shaderPeer::formatVBO;
        vao = GL45.glCreateVertexArrays();
        shaderPeer.formatVAO(vao);
    }

    public void newBuffers(int vertices, int indices) {
        deleteBuffers();

        this.vertices = vertices;
        this.indices = indices;

        vertexBuffer = GraphicsMappedBuffer.newArrayBuffer(vertices * stride);
        indexBuffer = GraphicsMappedBuffer.newElementArrayBuffer(indices * 3 * 4);

        GL45.glVertexArrayVertexBuffer(vao, 0, vertexBuffer.bufferID(), 0, stride);
        GL45.glVertexArrayElementBuffer(vao, indexBuffer.bufferID());

        vboFormat.accept(vao, vertexBuffer.bufferID());
    }

    public void bind() {
        GL30.glBindVertexArray(vao);
        indexBuffer.bind();
    }

    public void unbind() {
        indexBuffer.unbind();
        GL30.glBindVertexArray(GL11.GL_NONE);
    }

    @Override
    public void dispose() {
        deleteBuffers();
        GL30.glDeleteVertexArrays(vao);
    }

    public void deleteBuffers() {
        if (vertexBuffer != null)
            vertexBuffer.dispose();
        if (indexBuffer != null)
            indexBuffer.dispose();
    }
}

