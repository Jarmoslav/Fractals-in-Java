package fractals;

/**
 * @source https://code.google.com/p/slickshader/ and Jeremy Klix
 *
 */

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.*;

/**
 * @author simonjare
 *
 */
public class Main extends BasicGame{

    private Shader mandelbrot;
    private Shader julia;
    private  Boolean stateJulia= true;

    private float[] quad1;
    private float[] quad2;

    private float cX, mx;
    private float cY, my;

    private float zoom = 1.0f;
    private float dx, dy = 0.0f;
    private float colorFrequency = 0.5f;

    private int colorDefinition =1;


    public Main(String title){
        super(title);
    }


    @Override
    public void init(GameContainer gc) throws SlickException{
        mandelbrot = Shader.makeShader("/Users/simonjare/Desktop/Java/kl/src/fractals/shaders/fractal.vrt", "/Users/simonjare/Desktop/Java/kl/src/fractals/shaders/mandelbrot.frg");
        julia = Shader.makeShader("/Users/simonjare/Desktop/Java/kl/src/fractals/shaders/fractal.vrt", "/Users/simonjare/Desktop/Java/kl/src/fractals/shaders/julia.frg");

        quad1 = new float[]{0, 0, 0,
                0, 2, 0,
                3, 2, 0,
                3, 0, 0};
        quad2 = new float[]{0, 0, 0,
                0, 1, 0,
                1, 1, 0,
                1, 0, 0};

    }


    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException{

        mx = gameContainer.getInput().getMouseX();
        my = gameContainer.getInput().getMouseY();

        cX=(mx/400.0f)-2.0f;
        cY=(my/400.0f)-1.0f;

        if(gameContainer.getInput().isKeyDown(Input.KEY_LEFT)){
            dx -= (0.0005f*delta)/zoom;
        }else if(gameContainer.getInput().isKeyDown(Input.KEY_RIGHT)){
            dx += (0.0005f*delta)/zoom;
        }
        if(gameContainer.getInput().isKeyDown(Input.KEY_UP)){
            dy += (0.0005f*delta)/zoom;
        }else if(gameContainer.getInput().isKeyDown(Input.KEY_DOWN)){
            dy -= (0.0005f*delta)/zoom;
        }

        if(gameContainer.getInput().isKeyDown(Input.KEY_Z)){
            zoom = zoom*1.01f;
        }else if(gameContainer.getInput().isKeyDown(Input.KEY_X)){
            zoom = zoom/1.01f;
            if(zoom<1.00f){
                zoom = 1.00f;
            }
        }

        //change to Mandelbrot or Julia set
        if(gameContainer.getInput().isKeyDown(Input.KEY_J)){
            stateJulia=true;

        }else if(gameContainer.getInput().isKeyDown(Input.KEY_M)){
            stateJulia=false;
        }

        //change the threshold
        if(gameContainer.getInput().isKeyDown(Input.KEY_1)){
            colorDefinition=1;
        }
        if(gameContainer.getInput().isKeyDown(Input.KEY_2)){
            colorDefinition=2;
        }
        if(gameContainer.getInput().isKeyDown(Input.KEY_3)){
            colorDefinition=3;
        }

        //adjust the color frequency
        if(gameContainer.getInput().isKeyDown(Input.KEY_P)){
            colorFrequency+=0.1f;
        }
        if(gameContainer.getInput().isKeyDown(Input.KEY_O)){
            colorFrequency-=0.1f;
        }
    }



    public void render(GameContainer gc, Graphics g)
            throws SlickException{
        g.scale(400, 400);

        if(stateJulia==true){
            julia.startShader();
            julia.setUniformFloatVariable("cX", cX/zoom + dx);
            julia.setUniformFloatVariable("cY", cY/zoom + dy);
            julia.setUniformFloatVariable("colorFrequency", colorFrequency);
            julia.setUniformIntVariable("colorDefinition", colorDefinition);


            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f((-2.0f/zoom + dx), 1.0f/zoom + dy);
            GL11.glVertex3f(quad2[0],  quad2[1],  quad2[2]);

            GL11.glTexCoord2f(-2.0f/zoom + dx, -1.0f/zoom + dy);
            GL11.glVertex3f(quad1[3],  quad1[4],  quad1[5]);

            GL11.glTexCoord2f(1.0f/zoom + dx, -1.0f/zoom + dy);
            GL11.glVertex3f(quad1[6],  quad1[7],  quad1[8]);

            GL11.glTexCoord2f(1.0f/zoom + dx, 1.0f/zoom + dy);
            GL11.glVertex3f(quad1[9], quad1[10], quad1[11]);
            GL11.glEnd();
        }else {
            mandelbrot.startShader();
            mandelbrot.setUniformFloatVariable("colorFrequency", colorFrequency);
            mandelbrot.setUniformIntVariable("colorDefinition", colorDefinition);

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(-2.0f/zoom + dx, 1.0f/zoom + dy);
            GL11.glVertex3f(quad1[0], quad1[1], quad1[2]);

            GL11.glTexCoord2f(-2.0f/zoom + dx, -1.0f/zoom + dy);
            GL11.glVertex3f(quad1[3],  quad1[4],  quad1[5]);

            GL11.glTexCoord2f(1.0f/zoom + dx, -1.0f/zoom + dy);
            GL11.glVertex3f(quad1[6],  quad1[7],  quad1[8]);

            GL11.glTexCoord2f(1.0f/zoom + dx, 1.0f/zoom + dy);
            GL11.glVertex3f(quad1[9], quad1[10], quad1[11]);
            GL11.glEnd();

        }

        g.scale(1.0f/400.0f, 1.0f/400.0f);

        Shader.forceFixedShader();

        g.setColor(Color.black);
        g.drawString("Press m to get Mandelbrot", 9, 25);
        g.drawString("Press j to get Julia set", 9, 45);
        g.drawString("Zoom with: z and x", 9, 65);
        g.drawString("cX: "+(cX/zoom + dx), 9, 85);
        g.drawString("yX: "+(cY/zoom + dy), 9, 105);
        g.drawString("Change color schema by pressing: 1,2,3", 9, 125);
        g.drawString("Current Schema: "+colorDefinition, 9, 145);
        g.drawString("Adjust colors with P or O: ", 9, 165);

    }


    public static void main(String[] args){
        Main m = new Main("Fratals in Java");

        try{
            AppGameContainer agc = new AppGameContainer(m, 1200, 1200, false);
            agc.start();
        }catch(SlickException e){
            e.printStackTrace();
        }
    }
}




