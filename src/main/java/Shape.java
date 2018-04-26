import com.jogamp.opengl.GL2;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2d;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;


/**
 * Created by Aaron on 3/9/2018.
 */
public abstract class Shape{
    float x;
    float y;
    int size;
    int sideNum;
    int speed;
    double rotationAmount;
    Color color;
    Vector2d movement;
    ArrayList<Vector2d> vectors;
    ArrayList<Point2f> points;
    double offsetVal = 0;

    public void move(){
        x = (float) (x + speed*movement.getX()/10);
        y = (float) (y + speed*movement.getY()/10);
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
    public void populatePoints(){
        points = new ArrayList<Point2f>();
        for(int i = 0; i < sideNum; i++){
            double theta = i*2*Math.PI/sideNum - rotationAmount;
            Point2f nextPoint = new Point2f(
                    (float)(x + ((size/2)*Math.cos(theta))),
                    (float)(y - ((size/2)*Math.sin(theta))));
            points.add(nextPoint);
        }
    }
    public void populateVectors(){
        vectors = new ArrayList<Vector2d>();
        //Progress around
        for(int i = 0; i < points.size()-1; i++){
            Vector2d v = new Vector2d(points.get(i+1).getX() - points.get(i).getX(),points.get(i+1).getY() - points.get(i).getY());
            vectors.add(v);
        }
        vectors.add(new Vector2d(points.get(0).getX() - points.get(points.size()-1).getX(),points.get(0).getY() - points.get(points.size()-1).getY()));
    }
    public void update(GL2 gl){
        populatePoints();
        populateVectors();
        draw(gl);
    }
    public void draw(GL2 gl){
        gl.glColor4f(color.getR() / 255, color.getG() / 255, color.getB() / 255, color.getA());
        gl.glBegin(GL2.GL_LINE_STRIP);
        for(Point2f p:points){
            gl.glVertex2f(p.x, p.y);
        }
        gl.glVertex2f(points.get(0).getX(), points.get(0).getY());
        gl.glEnd();
    }

    public boolean containsPoint(Point2f p){
        float beginX = points.get(0).getX();
        float beginY = points.get(0).getY();
        float endX;
        float endY;
        for(Vector2d v: vectors){
            endX = beginX + (float) v.x;
            endY = beginY + (float) v.y;
            if(((endX-beginX) * (p.y - beginY) - (endY-beginY)*(p.x - beginX)) > offsetVal){
                return false;
            }
            beginX = endX;
            beginY = endY;
        }
        return true;
    }
    public Vector2d violatingOutsideEdge(Point2f p){
        float beginX = points.get(0).getX();
        float beginY = points.get(0).getY();
        float endX;
        float endY;
        for(Vector2d v: vectors){
            endX = beginX + (float) v.x;
            endY = beginY + (float) v.y;
            if(((endX-beginX) * (p.y - beginY) - (endY-beginY)*(p.x - beginX)) > offsetVal){
                return v;
            }
            beginX = endX;
            beginY = endY;
        }
        return null;
    }
    public Vector2d violatingInsideEdge(Point2f p){
        Point2f closestPoint = points.get(0);
        double distanceToClosest = Double.MAX_VALUE;
        Point2f nextClosestPoint = points.get(1);
        double distanceToNextClosest = Double.MAX_VALUE;
        int closestPointIndex;
        for(Point2f vertex:points){
            double distance = distanceBetween(vertex, p);
            if(distance < distanceToClosest){
                nextClosestPoint = closestPoint;
                distanceToNextClosest = distanceBetween(p, nextClosestPoint);
                closestPoint = vertex;
                distanceToClosest = distanceBetween(p, closestPoint);
            }else if(distance < distanceToNextClosest){
                nextClosestPoint = vertex;
                distanceToNextClosest = distanceBetween(p, nextClosestPoint);
            }
        }
        ArrayList<Point2f> closestPoints = new ArrayList<Point2f>();
        closestPoints.add(closestPoint);
        closestPoints.add(nextClosestPoint);
        closestPoints = sortPoints(closestPoints);
        closestPointIndex =  points.indexOf(closestPoints.get(0));
        return vectors.get(closestPointIndex);
    }
    public ArrayList<Point2f> getPoints(){
        return  points;
    }
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

    public void setSpeed(int speed){
        this.speed = speed;
    }
    public double getRotationAmount() {
        return rotationAmount;
    }
    public ArrayList<Point2f> sortPoints(ArrayList<Point2f> pointList){
        if(pointList.size() < 2){
            return pointList;
        }
        TreeMap rankedPoints = new TreeMap();
        double highestPoint = Double.MIN_VALUE;
        Point2f top = new Point2f();
        for(Point2f p:pointList){
            if(p.getY() >highestPoint) {
                highestPoint = p.getY();
                top = p;
            }
        }
        Vector2d difference;
        Vector2d reference = new Vector2d(1,0);
        for(Point2f p:pointList){
            difference = new Vector2d(p.getX()- top.getX(), p.getY()-top.getY());
            double angle = reference.angle(difference);
            rankedPoints.put(angle, p);
        }
        return new ArrayList<Point2f>(rankedPoints.values());
    }
    public double distanceBetween(Point2f p1, Point2f p2){
        return Math.sqrt(Math.pow(p1.getX()-p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }
}
