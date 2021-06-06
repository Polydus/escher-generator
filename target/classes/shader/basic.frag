varying lowp vec4 vColor;
varying highp float vTime;

void main(void) {
    gl_FragColor = vColor;
    //gl_FragColor.rgb = vec3(vColor.x + vTime, vColor.y + vTime, vColor.z + vTime);
}




