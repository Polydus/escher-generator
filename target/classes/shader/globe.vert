attribute vec3 aVertexPosition;
attribute vec3 aVertexNormal;
attribute vec4 aVertexColor;
//attribute vec2 aTextureCoord;

uniform mat4 uProjectionMatrix;
uniform mat4 uModelViewMatrix;
uniform mat4 uNormalMatrix;

//uniform float uTime;

varying lowp vec4 vColor;
//varying highp vec2 vTextureCoord;
varying highp vec3 vLighting;

//varying float vTime;


void main(void) {
    gl_Position = uProjectionMatrix * uModelViewMatrix * vec4(aVertexPosition, 1.0);

    // Apply lighting effect
    highp vec3 ambientLight = vec3(0.5);
    highp vec3 directionalLightColor = vec3(1);
    highp vec3 directionalVector = normalize(vec3(1.0, 0.0, 0.0));
    highp vec4 transformedNormal = uNormalMatrix * vec4(aVertexNormal, 1.0);
    highp float directional = max(dot(transformedNormal.xyz, directionalVector), 0.0);

    vLighting = ambientLight + (directionalLightColor * directional);
    vColor = aVertexColor;
    //vTime = uTime;
    //vTextureCoord = aTextureCoord;
}