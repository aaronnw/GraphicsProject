import com.jogamp.opengl.GL2;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2d;
import java.util.ArrayList;
import java.util.TreeMap;


/**
 * Created by Aaron on 3/9/2018.
 */
public class Container {
    Color color;
    ArrayList<Vector2d> vectors;
    ArrayList<Point2f> points;
    Point2f center;
    double offsetVal = 0;

    public Container(ArrayList<Point2f> points){
        this.color = Color.WHITE;
        this.points = sortPoints(points);
        populateVectors();
        calculateCenter();
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
    public void populateVectors(){
        vectors = new ArrayList<Vector2d>();
        //Progress around
        for(int i = 0; i < points.size()-1; i++){
            Vector2d v = new Vector2d(points.get(i+1).getX() - points.get(i).getX(),points.get(i+1).getY() - points.get(i).getY());
            vectors.add(v);
        }
        vectors.add(new Vector2d(points.get(0).getX() - points.get(points.size()-1).getX(),points.get(0).getY() - points.get(points.size()-1).getY()));
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
    public Vector2d violatingEdge(Point2f p){
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
    public ArrayList<Point2f> getPoints(){
        return  points;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public void calculateCenter(){
        float sumX = 0;
        float sumY = 0;
        for(Point2f p:points){
            sumX += p.getX();
            sumY += p.getY();
        }
       center = new Point2f(sumX/points.size(), sumY/points.size());
    }
    public Point2f getCenter(){
        return center;
    }
    public Color getColor(){
        return color;
    }
}
