package com.basdxz.vshadeloadingscreen.scene;

import com.basdxz.vshade.variable.linked.floats.GLFloatVec2;
import lombok.*;
import lombok.experimental.*;

@Getter
@Accessors(fluent = true, chain = true)
@SuperBuilder
public class SpriteVertexLayout extends OrthoVertexLayout {
    protected final GLFloatVec2 texture = GLFloatVec2.builder().variableLayout(this).name("in_TextureCoord").build().init();
}
