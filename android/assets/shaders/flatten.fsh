varying vec4 vColor;
varying vec2 vTexCoord;

uniform sampler2D u_texture;

void main() {
	
	vec4 color = texture2D(u_texture, vTexCoord) * vColor;

	if (color.r > 0.0) {
		gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
	} else {
		gl_FragColor = vec4(0.0, 0.0, 1.0, 1.0);
	}
}