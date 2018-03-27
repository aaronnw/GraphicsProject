import com.jogamp.opengl.GL2;

import javax.vecmath.Vector2d;
import java.awt.*;

/**
 * Created by Aaron on 3/9/2018.
 */
public class Square extends Shape {
    private int sideLength;

    public Square(float x, float y, int size){
        this.x = x;
        this.y = y;
        sideLength = size;
        this.size = size;
        this.movement = new Vector2d(0,0);
    }

    public Square(float x, float y, int size, Vector2d movement, Color color){
        this.x = x;
        this.y = y;
        this.movement = movement;
        this.size = size;
        sideLength = size;
        this.color = color;
    }

    public void draw(GL2 gl){
        int halfSide = sideLength/2;
        gl.glColor4f(color.getR(), color.getG(), color.getB(), color.getA());
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2f(x +halfSide, y+halfSide);
        gl.glVertex2f(x + halfSide, y-halfSide);
        gl.glVertex2f(x - halfSide, y-halfSide);
        gl.glVertex2f(x - halfSide, y+halfSide );
        gl.glEnd();
    }
    public boolean containsPoint(Point p){
        return (p.getX() > getLeftPoint() &&
                p.getX() < getRightPoint() &&
                p.getY() > getTopPoint() &&
                p.getY() < getBottomPoint());
    }

    public float getLeftPoint() {
        return x-sideLength/2;
    }

    public float getTopPoint() {
        return y-sideLength/2;
    }

    public float getRightPoint() {
        return x+sideLength/2;
    }

    public float getBottomPoint() {
        return y+sideLength/2;
    }

    public Square makeCopy(){
        return new Square(x,y,size, movement, color);
    }

}
