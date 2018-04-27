import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import javax.vecmath.Point2d;
import javax.vecmath.Point2f;
import javax.vecmath.Vector2d;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

import static com.jogamp.opengl.GL.GL_BLEND;


/**
 * Created by Aaron on 3/9/2018.
 */
public abstract class Shape{
    float x;
    float y;
    int size;
    int sideNum;
    double rotationAmount;
    Color color;
    Float alpha = 1f;
    Vector2d movement;
    ArrayList<Vector2d> vectors;
    ArrayList<Point2f> points;
    ArrayList<Point2d> crackPoints = new ArrayList<>();

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
        gl.glEnable(gl.GL_BLEND);
        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
        gl.glColor4f(color.getR() / 255, color.getG() / 255, color.getB() / 255, alpha);
        gl.glBegin(GL2.GL_POLYGON);
        for(Point2f p:points){
            gl.glVertex2f(p.x, p.y);
        }
        gl.glVertex2f(points.get(0).getX(), points.get(0).getY());
        gl.glEnd();
    }

    public void drawOutline(GL2 gl){
        gl.glColor3f(color.getR() / 255, color.getG() / 255, color.getB() / 255);
        gl.glBegin(GL2.GL_LINE_LOOP);
        for(Point2f p:points){
            gl.glVertex2f(p.x, p.y);
        }
        gl.glVertex2f(points.get(0).getX(), points.get(0).getY());
        gl.glEnd();
    }

    public void setCracks(){
        Random rand = new Random();
        int start = rand.nextInt(points.size());
        Effects effects = new Effects();
        Point2f startPoint = points.get(start);
        //crackPoints = effects.createCracks(startPoint.getX(), startPoint.getY(), x, y, 75);
        crackPoints = effects.createCracks(100,100, x, y, 130);
    }

    public void drawCracks(GL2 gl) {
        gl.glColor3f(1, 1, 1);
        gl.glLineWidth(1);
        gl.glBegin(GL.GL_LINE_STRIP);
        for (Point2d p : crackPoints) {
            gl.glVertex2d(p.x, p.y);
        }
        gl.glEnd();
    }

    public void drawHighlight(GL2 gl){
        Point2f p = points.get(0);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2f(p.x-5, p.y-10);
        gl.glVertex2f(p.x -20, p.y-25);
        gl.glVertex2f(p.x -25, p.y-20);
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
            if(((endX-beginX) * (p.y - beginY) - (endY-beginY)*(p.x - beginX)) > 0){
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
            if(((endX-beginX) * (p.y - beginY) - (endY-beginY)*(p.x - beginX)) > 0){
                return v;
            }
            beginX = endX;
            beginY = endY;
        }
        return null;
    }
    public Vector2d violatingInsideEdge(Point2f p){
        float beginX = points.get(0).getX();
        float beginY = points.get(0).getY();
        float endX;
        float endY;
        for(Vector2d v: vectors){
            endX = beginX + (float) v.x;
            endY = beginY + (float) v.y;
            if(((endX-beginX) * (p.y - beginY) - (endY-beginY)*(p.x - beginX)) < 0){
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
    public Color getColor(){
        return color;
    }

    public void setAlpha(Float alpha) {
        this.alpha = alpha;
    }

    public Float getAlpha() {
        return alpha;
    }

    public int getSize(){
        return size;
    }
    public abstract Shape makeCopy();
}
