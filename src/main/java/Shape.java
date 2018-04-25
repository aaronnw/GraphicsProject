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
    int sideNum;
    float startX;
    float startY;
    Color color;
    Vector2d movement;
    ArrayList<Vector2d> vectors;
    ArrayList<Point2f> points;

    public void move(){
        x = (float) (x + movement.getX()/60);
        y = (float) (y + movement.getY()/60);
    }
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
    public void populateVectors(){
        vectors = new ArrayList<Vector2d>();
        float lastX = startX;
        float lastY = startY;
        Vector2d rotationVector = new Vector2d(startY - y, startX - x);
        double rotationAngle = rotationVector.angle(new Vector2d(1,0));
        for(int i = 1; i <= sideNum; i++){
            Point2f nextPoint = new Point2f(
                    (float)(x + ((size/2)*Math.cos(i*2*Math.PI/sideNum))),
                    (float)(y - ((size/2)*Math.sin(i*2*Math.PI/sideNum))));
            Vector2d temp = new Vector2d(nextPoint.x - lastX, nextPoint.y - lastY);
            vectors.add(temp);
            lastX = nextPoint.x;
            lastY = nextPoint.y;
        }
    }
    public void populatePoints(){
        points = new ArrayList<Point2f>();
        points.add(new Point2f(startX, startY));
        //Progress around
        for(Vector2d v:vectors){
            float nextX = (float) v.getX() + x;
            float nextY = (float) v.getY() + y;
            points.add(new Point2f(nextX, nextY));
        }
    }
    public void draw(GL2 gl){
        gl.glColor4f(color.getR() / 255, color.getG() / 255, color.getB() / 255, color.getA());
        gl.glBegin(GL2.GL_LINE_STRIP);
        gl.glVertex2f(startX, startY);
        for(Point2f p:points){
            gl.glVertex2f(p.x, p.y);
        }
        gl.glEnd();
    }
    public boolean containsPoint(Point2f p){
        float beginX = startX;
        float beginY = startY;
        float endX;
        float endY;
        for(Vector2d v: vectors){
            endX = beginX + (float) v.x;
            endY = beginY + (float) v.y;
            if(((endX-beginX) * (p.y - beginY) - (endY-beginY)*(p.x - beginX)) > 0){
                return false;
            }
            beginX = endX;
            beginY = endY;
        }
        return true;
    }
    public Vector2d violatingEdge(Point2f p){
        float beginX = startX;
        float beginY = startY;
        float endX;
        float endY;
        for(Vector2d v: vectors){
            endX = beginX + (float) v.x;
            endY = beginY + (float) v.y;
            if(((endX-beginX) * (p.y - beginY) - (endY-beginY)*(p.x - beginX)) > 0){
                return v;
            }
            beginX = endX;
            beginY = endY;
        }
        return null;
    }
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
