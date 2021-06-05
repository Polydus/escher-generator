attribute vec2 aVertexPosition;
attribute vec4 aVertexColor;

uniform mat4 uModelViewMatrix;
uniform mat4 uProjectionMatrix;
uniform highp float uTime;

varying lowp vec4 vColor;
varying highp float vTime;

void main(void) {
    gl_Position = uProjectionMatrix * uModelViewMatrix * vec4(aVertexPosition, 1.0, 1.0);
    vColor = aVertexColor;
    vTime = mod((uTime / 10000.0), 0.5);
}