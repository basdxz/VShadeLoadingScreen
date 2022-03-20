package com.basdxz.vshadeloadingscreen.scene;

import com.basdxz.vbuffers.common.ResourceHelper;
import com.basdxz.vshade.shader.ShaderProgram;
import com.basdxz.vshade.shader.ShaderSource;
import lombok.*;
import lombok.experimental.*;

@Getter
@Accessors(fluent = true, chain = true)
@SuperBuilder
public class ShaderToyShader extends ShaderProgram {
    protected final ShaderToyLayout shaderToyUniforms = ShaderToyLayout.builder().layoutProvider(this).build().init();
    protected final OrthoMVPLayout uniforms = OrthoMVPLayout.builder().layoutProvider(this).build().init();
    protected final OrthoVertexLayout attributes = OrthoVertexLayout.builder().layoutProvider(this).build().init();

    public static ShaderToyShader newHexagons() {
        return ShaderToyShader.builder()
                .source(ShaderSource.newVertex(ResourceHelper.readResourceAsString("/assets/vshadeloadingscreen/shader/hexagon.vert")))
                .source(ShaderSource.newFragment(ResourceHelper.readResourceAsString("/assets/vshadeloadingscreen/shader/hexagon.frag")))
                .name("Default Hexagons")
                .build().init();
    }
}
