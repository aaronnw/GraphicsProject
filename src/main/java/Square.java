import com.jogamp.opengl.GL2;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2d;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

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
        populateVectors();
    }

    public Square(float x, float y, int size, Vector2d movement, Color color){
        this.x = x;
        this.y = y;
        this.movement = movement;
        this.size = size;
        sideLength = size;
        this.color = color;
        populateVectors();
    }

    public void populateVectors(){
        vectors = new ArrayList<Vector2d>();
        Vector2d temp = new Vector2d(0, - size);
        vectors.add(temp);
        temp = new Vector2d(-size, 0);
        vectors.add(temp);
        temp = new Vector2d(0, size);
        vectors.add(temp);
        temp = new Vector2d(size, 0);
        vectors.add(temp);
    }
    public void draw(GL2 gl){
        points = new ArrayList<Point2f>();
        float originX = x;
        float originY = y;
        float halfSide = size/2;
        gl.glColor4f(color.getR() / 255, color.getG() / 255, color.getB() / 255, color.getA());
        gl.glBegin(GL2.GL_POLYGON);
        //Get the first point
        x = x+halfSide;
        y = y+halfSide;
        gl.glVertex2f(x, y);
        points.add(new Point2f(x, y));
        //Progress around
        for(Vector2d v:vectors){
            x = (float) v.getX() + x;
            y = (float) v.getY() + y;
            gl.glVertex2f(x, y);
            points.add(new Point2f(x, y));
        }
        gl.glEnd();
        x = originX;
        y = originY;
    }
    public boolean containsPoint(Point2f p){
        float startX = x+ size/2;
        float startY = y + size/2;
        for(Vector2d v: vectors){
            float nextX = startX + (float) v.x;
            float nextY = startY + (float) v.y;
            if(((nextX-startX) * (p.y - startY) - (nextY-startY)*(p.x - startX)) > 0){
                return false;
            }
            startX = nextX;
            startY = nextY;
        }
        return true;
    }
    public Vector2d violatingEdge(Point2f p){
        float startX = x+ size/2;
        float startY = y + size/2;
        for(Vector2d v: vectors){
            float nextX = startX + (float) v.x;
            float nextY = startY + (float) v.y;
            if(((nextX-startX) * (p.y - startY) - (nextY-startY)*(p.x - startX)) > 0){
                return v;
            }
            startX = nextX;
            startY = nextY;
        }
        return null;
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
