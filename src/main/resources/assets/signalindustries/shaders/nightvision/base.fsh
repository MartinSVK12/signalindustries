#version 120

uniform sampler2D colortex0;

varying vec2 texcoord;

void main(){
    vec4 color = texture2D(colortex0, texcoord).rgba;
    gl_FragColor = color;
}
