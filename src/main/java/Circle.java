import com.jogamp.opengl.GL2;

import javax.vecmath.Vector2d;

/**
 * Created by Aaron on 3/9/2018.
 */
public class Circle extends Shape {
    private int radius;
    private Color color = Color.WHITE;

    public Circle(int x, int y, int size){
        this.x = x;
        this.y = y;
        radius = size/2;
        this.movement = new Vector2d(0,0);
    }

    public Circle(int x, int y, int size, Vector2d movement){
        this.x = x;
        this.y = y;
        this.movement = movement;
        radius = size/2;
    }

    public void draw(GL2 gl){
        int circleSides = 32;
        gl.glColor4f(color.getR(), color.getG(), color.getB(), color.getA());
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        for(int i = 0; i <= circleSides; i++){
            gl.glVertex2f(
                (float)(x + (radius*Math.cos(i*2*Math.PI/circleSides))),
                (float)(y + (radius*Math.sin(i*2*Math.PI/circleSides)))

            );
        }
        gl.glEnd();
    }

    public float getLeftPoint() {
        return x- radius;
    }

    public float getTopPoint() {
        return y- radius;
    }

    public float getRightPoint() {
        return x+ radius;
    }

    public float getBottomPoint() {
        return y+ radius;
    }

    public void setColor(Color color){
        this.color = color;
    }

}
