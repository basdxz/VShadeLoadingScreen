package com.basdxz.vshadeloadingscreen.scene;

import com.basdxz.vshade.layout.VertexLayout;
import com.basdxz.vshade.variable.linked.floats.GLFloatVec3;
import lombok.*;
import lombok.experimental.*;

@Getter
@Accessors(fluent = true, chain = true)
@SuperBuilder
public class OrthoVertexLayout extends VertexLayout {
    protected final GLFloatVec3 position = GLFloatVec3.builder().variableLayout(this).name("in_Position").build().init();
}
