package com.basdxz.vshadeloadingscreen.scene;

import de.matthiasmann.twl.utils.PNGDecoder;
import lombok.*;
import org.lwjgl.opengl.*;

import java.io.InputStream;
import java.nio.ByteBuffer;

@RequiredArgsConstructor
public class Texture {
    protected static final int BIT_PER_FRAG = 4;
    protected final int texID;

    @SneakyThrows
    public static Texture loadTexture(InputStream inputStream) {
        // Get and decode texture from memory
        val decoder = new PNGDecoder(inputStream);
        val buffer = ByteBuffer.allocateDirect(BIT_PER_FRAG * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buffer, decoder.getWidth() * BIT_PER_FRAG, PNGDecoder.Format.RGBA);
        buffer.flip();

        // Allocate new texture id
        val id = GL45.glCreateTextures(GL11.GL_TEXTURE_2D);

        // Set scaling flags
        GL45.glTextureParameteri(id, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL45.glTextureParameteri(id, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        // Set format and upload texture
        GL45.glTextureStorage2D(id, 4, GL11.GL_RGBA8, decoder.getWidth(), decoder.getHeight());
        GL45.glTextureSubImage2D(id, 0, 0, 0, decoder.getWidth(), decoder.getHeight(), GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        // Gen mipmaps
        GL45.glGenerateTextureMipmap(id);

        return new Texture(id);
    }

    public void bind(int texUnit) {
        GL45.glBindTextureUnit(texUnit, texID);
    }

    public void unbind(int texUnit) {
        GL45.glBindTextureUnit(texUnit, 0);
    }
}
