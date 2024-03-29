package com.basdxz.vshadeloadingscreen.scene;

import com.basdxz.vshade.layout.UniformLayout;
import com.basdxz.vshade.variable.linked.floats.GLFloatMat4;
import lombok.experimental.*;
import org.joml.Matrix4f;

@SuperBuilder
public class OrthoMVPLayout extends UniformLayout {
    protected final static float Z_NEAR = 0.1F;
    protected final static float Z_FAR = 100F;

    protected final DisplayResizeHandler displayResizeHandler = new DisplayResizeHandler(this::onResize);

    protected final GLFloatMat4 modelMatrix = GLFloatMat4.builder().variableLayout(this).name("modelMatrix").build().init();
    protected final GLFloatMat4 viewMatrix = GLFloatMat4.builder().variableLayout(this).name("viewMatrix").build().init();
    protected final GLFloatMat4 projectionMatrix = GLFloatMat4.builder().variableLayout(this).name("projectionMatrix").build().init();

    protected final Matrix4f model = new Matrix4f();
    protected final Matrix4f view = new Matrix4f();
    protected final Matrix4f projection = new Matrix4f();

    public void update() {
        displayResizeHandler.checkForResize();
        modelMatrix.set(model);
        viewMatrix.set(view);
        projectionMatrix.set(projection);
    }

    protected void onResize(int width, int height) {
        projection.identity().orthoSymmetric(width, height, Z_NEAR, Z_FAR);
    }
}
