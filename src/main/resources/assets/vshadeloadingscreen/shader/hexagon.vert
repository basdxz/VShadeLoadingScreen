#version 460

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

uniform ColourUniformBlock {
    vec4 real_color_in;
};

in vec3 in_Position;

out vec2 pass_Position;

void main(void) {
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(in_Position, 1.0);
    //gl_Position.xy -= 1.0;
    //pass_Position.xy = in_Position.xy;
}