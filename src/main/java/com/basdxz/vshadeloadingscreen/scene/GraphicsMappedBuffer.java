package com.basdxz.vshadeloadingscreen.scene;

import com.basdxz.vbuffers.common.Disposable;
import com.basdxz.vbuffers.common.MemUtils;
import lombok.*;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;

public class GraphicsMappedBuffer implements Disposable {
    protected static final int FLAGS = GL30.GL_MAP_WRITE_BIT | GL44.GL_MAP_PERSISTENT_BIT | GL44.GL_MAP_COHERENT_BIT;

    protected final Type type;
    protected final int bufferID;
    protected final ByteBuffer buffer;

    public GraphicsMappedBuffer(@NonNull Type type, int byteSize) {
        if (byteSize <= 0)
            throw new IllegalArgumentException("byteSize <= 0");
        this.type = type;
        bufferID = GL45.glCreateBuffers();
        val tempBuffer = MemUtils.newByteBuffer(byteSize);
        GL45.glNamedBufferStorage(bufferID, tempBuffer, FLAGS);
        buffer = GL45.glMapNamedBufferRange(bufferID, 0, byteSize, FLAGS, tempBuffer);
    }

    public static GraphicsMappedBuffer newArrayBuffer(int byteSize) {
        return new GraphicsMappedBuffer(Type.ARRAY, byteSize);
    }

    public static GraphicsMappedBuffer newElementArrayBuffer(int byteSize) {
        return new GraphicsMappedBuffer(Type.ELEMENT_ARRAY, byteSize);
    }

    public void bind() {
        GL15.glBindBuffer(type.glEnum(), bufferID);
    }

    public void unbind() {
        GL15.glBindBuffer(type.glEnum(), GL11.GL_NONE);
    }

    @Override
    public void dispose() {
        GL15.glDeleteBuffers(bufferID);
    }

    public Type type() {
        return this.type;
    }

    public int bufferID() {
        return this.bufferID;
    }

    public ByteBuffer buffer() {
        return this.buffer;
    }

    @AllArgsConstructor
    public enum Type {
        ARRAY(GL15.GL_ARRAY_BUFFER), ELEMENT_ARRAY(GL15.GL_ELEMENT_ARRAY_BUFFER);

        private final int glEnum;

        public int glEnum() {
            return this.glEnum;
        }
    }
}
