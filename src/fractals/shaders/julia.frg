    uniform float cX;
    uniform float cY;
    uniform float colorFrequency;
    uniform int colorDefinition;


    vec4 getColor(float iteration) {
        float v = iteration;

        float initialColorValue = 0.1*colorFrequency;

        float a = initialColorValue;
        float b = initialColorValue;
        float c = 0.13;

        float red   = a * v;
        float green = red;
        float blue  = red;

        if(colorDefinition > 0) {
            green = a  * v;
            blue  = b * v;
        }
        if(colorDefinition > 1) {
            red   = red * 2.0;
            green = b * v;
            blue  = c  * v;
        }
        if(colorDefinition > 2) {
            red   = c  * 1.5 * v;
            green = a * 0.8 * v;
            blue  = b * 0.8 * v;
        }

        return vec4(abs(cos(red)), abs(cos(green)), abs(cos(blue)),	1.0);
    }


    void main (void)
    {
      float iteration =0.0;
      float max_iteration = 256.0;


      vec2 c = gl_TexCoord[0].xy;
      vec2 z = c;

      for (float iteration = 0.0; iteration < max_iteration; iteration += 1.0) {
         if (dot(z, z) > 4.0) {
             gl_FragColor = getColor(iteration);
                break;
         }
         else{
             gl_FragColor = vec4 (0.0, 0.0, 0.0, 1.0);
         }

         z = vec2(z.x*z.x - z.y*z.y + cX, 2.0*z.x*z.y + cY);
      }
    }


