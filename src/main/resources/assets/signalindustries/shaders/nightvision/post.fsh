#version 120

uniform sampler2D colortex0;

uniform float intensity;

varying vec2 texcoord;

vec3 toGrayscale(vec3 color)
{
	color.r = color.r * 0.5 + color.g * 0.5 + color.b * 0.5;
	color.g = color.r * 0.5 + color.g * 0.5 + color.b * 0.5;
	color.b = color.r * 0.5 + color.g * 0.5 + color.b * 0.5;

	return color;
}

void main()
{
	vec3 color = texture2D(colortex0, texcoord).rgb;
	vec3 gs = toGrayscale(color);

	color = mix(gs, gs, intensity);

	gl_FragColor = vec4(color, 1.0);
}