uniform float cX;
uniform float cY;

vec4 getColor(float iteration) {


	float alpha = 0.8;
	float beta = 0.15;
	float gamma = 0.15;

	float red   = alpha * iteration;
    float green = red;
    float blue  = red;

	red   = beta  * 1.5 * iteration;
	green = gamma * 0.5 * iteration;
	blue  = alpha * 0.5 * iteration;

	return vec4(abs(cos(red)), abs(cos(green)), abs(cos(blue)),	1);
}


void main (void)
{
    float iteration =0.0;
    float max_iteration = 256.0;

    vec2 c = gl_TexCoord[0].xy;
    vec2 z = c;

    for (float iteration = 0.0; iteration <  max_iteration; iteration += 1.0) {
    	if (dot(z, z) > 4.0) {
    	    gl_FragColor = getColor(iteration);
    		break;
    	}
    	else{
    	 gl_FragColor = vec4 (0.0, 0.0, 0.0, 1.0);
    	 }

    	z = vec2(z.x*z.x - z.y*z.y, 2.0*z.x*z.y) + c;
    }

}
