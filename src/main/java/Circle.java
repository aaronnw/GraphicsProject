import com.jogamp.opengl.GL2;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2d;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

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
        populateVectors();
    }

    public Circle(float x, float y, int size, Vector2d movement, Color color){
        this.x = x;
        this.y = y;
        this.movement = movement;
        this.size = size;
        radius = size/2;
        this.color = color;
        populateVectors();
    }
    public void populateVectors(){
        int circleSides = 32;
        vectors = new ArrayList<Vector2d>();
        float lastX = x + radius;
        float lastY = y;
        for(int i = 1; i <= circleSides; i++){
            Point nextPoint = new Point(
                    (int)(x + (radius*Math.cos(i*2*Math.PI/circleSides))),
                    (int)(y - (radius*Math.sin(i*2*Math.PI/circleSides))));
            Vector2d temp = new Vector2d(nextPoint.x - lastX, nextPoint.y - lastY);
            vectors.add(temp);
            lastX = nextPoint.x;
            lastY = nextPoint.y;
        }
    }
    public void draw(GL2 gl){
        points = new ArrayList<Point2f>();
        float originX = x;
        float originY = y;
        gl.glColor4f(color.getR() / 255, color.getG() / 255, color.getB() / 255, color.getA());
        x = x+radius;
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2f(x, y);
        points.add(new Point2f(x, y));
        for(Vector2d v:vectors){
            x = (float) v.x + x;
            y = (float) v.y + y;
            gl.glVertex2f(x, y);
            points.add(new Point2f(x, y));
        }
        gl.glEnd();
        x = originX;
        y = originY;
    }
    public boolean containsPoint(Point2f p){
        float startX = x + radius;
        float startY = y;
        for(Vector2d v: vectors){
            float nextX = startX + (float) v.getX();
            float nextY = startY + (float) v.getY();
            if(((nextX-startX) * (p.y - startY) - (nextY-startY)*(p.x - startX)) > 0){
                return false;
            }
            startX = nextX;
            startY = nextY;
        }
        return true;
    }
    public Vector2d violatingEdge(Point2f p) {
        float startX = x + radius;
        float startY = y;
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

    public Circle makeCopy(){
        return new Circle(x, y, size, movement, color);
    }

}
