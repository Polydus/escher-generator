varying lowp vec4 vColor;
//varying highp vec2 vTextureCoord;
//uniform sampler2D uSampler;
varying highp vec3 vLighting;

void main(void) {
    gl_FragColor = vColor;
    gl_FragColor = vec4(vColor.rgb * vLighting, vColor.a);

    //highp vec4 texelColor = texture2D(uSampler, vTextureCoord);
    //gl_FragColor = vec4(texelColor.rgb * vLighting, texelColor.a);
    //gl_FragColor = vec4(1, 0, 1, 1);
}




