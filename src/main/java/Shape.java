import com.jogamp.opengl.GL2;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2d;
import java.awt.*;
import java.util.ArrayList;


/**
 * Created by Aaron on 3/9/2018.
 */
public abstract class Shape{
    float x;
    float y;
    int size;
    Color color;
    Vector2d movement;
    ArrayList<Vector2d> vectors;
    ArrayList<Point2f> points;

    public void move(){
        x = (float) (x + movement.getX()/60);
        y = (float) (y + movement.getY()/60);
    }
    public abstract boolean containsPoint(Point2f p);
    //Like contains point, but returns the edge that a point violates
    public abstract Vector2d violatingEdge(Point2f p);
    public void setX(float x){
        this.x = x;
    }
    public void setY(float y){
        this.y = y;
    }
    public void setMovement(Vector2d v){
        movement = v;
    }
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Vector2d getMovement() {
        return movement;
    }
    public abstract void draw(GL2 gl);
    public abstract float getLeftPoint();
    public abstract float getTopPoint();
    public abstract float getRightPoint();
    public abstract float getBottomPoint();
    public void setColor(Color color) {
        this.color = color;
    }
    public Color getColor(){
        return color;
    }
    public int getSize(){
        return size;
    }
    public abstract Shape makeCopy();
}
