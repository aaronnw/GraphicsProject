import com.jogamp.opengl.GL2;

import javax.vecmath.Vector2d;
import java.awt.*;

/**
 * Created by Aaron on 3/9/2018.
 */
public class Circle extends Shape {
    private int radius;

    public Circle(float x, float y, int size){
        this.x = x;
        this.y = y;
        radius = size/2;
        this.size = size;
        this.movement = new Vector2d(0,0);
    }

    public Circle(float x, float y, int size, Vector2d movement, Color color){
        this.x = x;
        this.y = y;
        this.movement = movement;
        this.size = size;
        radius = size/2;
        this.color = color;
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
    public boolean containsPoint(Point p){
        return (p.getX() > getLeftPoint() &&
                p.getX() < getRightPoint() &&
                p.getY() > getTopPoint() &&
                p.getY() < getBottomPoint());
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

    public Circle makeCopy(){
        return new Circle(x, y, size, movement, color);
    }

}
