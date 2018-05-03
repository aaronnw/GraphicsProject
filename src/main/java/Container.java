import com.jogamp.opengl.GL2;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2d;
import java.util.ArrayList;
import java.util.TreeMap;


/**
 * Sets up the container for our shapes to bounce in
 * Could be configured to a different set of points
 */
public class Container {
    private Color color;
    private ArrayList<Vector2d> vectors;
    private ArrayList<Point2f> points;
    private Point2f center;
    private double offsetVal = 0;

    /**
     * To make a container just give it a set of points
     * @param points
     */
    Container(ArrayList<Point2f> points){
        this.color = Color.WHITE;
        //The points are first sorted counter-clockwise then the shape is made
        this.points = sortPoints(points);
        populateVectors();
        calculateCenter();
    }
    //Sort the points counter-clockwise based on their angle to the vector (1,0)
    private ArrayList<Point2f> sortPoints(ArrayList<Point2f> pointList){
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
    //Make vectors for the edges
    private void populateVectors(){
        vectors = new ArrayList<Vector2d>();
        //Progress around
        for(int i = 0; i < points.size()-1; i++){
            Vector2d v = new Vector2d(points.get(i+1).getX() - points.get(i).getX(),points.get(i+1).getY() - points.get(i).getY());
            vectors.add(v);
        }
        vectors.add(new Vector2d(points.get(0).getX() - points.get(points.size()-1).getX(),points.get(0).getY() - points.get(points.size()-1).getY()));
    }
    //If we wanted to draw the container, do it like any other point-defined shape
    public void draw(GL2 gl){
        gl.glColor4f(color.getR() / 255, color.getG() / 255, color.getB() / 255, color.getA());
        gl.glBegin(GL2.GL_LINE_STRIP);
        for(Point2f p:points){
            gl.glVertex2f(p.x, p.y);
        }
        gl.glVertex2f(points.get(0).getX(), points.get(0).getY());
        gl.glEnd();
    }
    //The important part -- does this container contain a given point
    boolean containsPoint(Point2f p){
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
    //Find out which edge of the container has been violated
    Vector2d violatingEdge(Point2f p){
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
    //Figure out the center, determined by the average of the point values
    private  void calculateCenter(){
        float sumX = 0;
        float sumY = 0;
        for(Point2f p:points){
            sumX += p.getX();
            sumY += p.getY();
        }
       center = new Point2f(sumX/points.size(), sumY/points.size());
    }
    Point2f getCenter(){
        return center;
    }
    public Color getColor(){
        return color;
    }
}
