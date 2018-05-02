import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import javax.vecmath.Point2f;
import javax.vecmath.Vector2d;
import java.util.ArrayList;
import java.util.Random;
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
    Float alpha = 1f;
    Vector2d direction;
    ArrayList<Vector2d> vectors;
    ArrayList<Point2f> points;
    ArrayList<Integer> unusuedPoints;
    double offsetVal = 0;
    ArrayList<Vector2d> crackVectors = new ArrayList<Vector2d>();
    ArrayList<ArrayList<Vector2d>> cracks = new  ArrayList<ArrayList<Vector2d>>();
    Random rand = new Random();

    public void move(){
        x = (float) (x + speed* direction.getX()/10);
        y = (float) (y + speed* direction.getY()/10);
    }
    public void setX(float x){
        this.x = x;
    }
    public void setY(float y){
        this.y = y;
    }
    public void setDirection(Vector2d v){
        direction = v;
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public Vector2d getDirection() {
        return direction;
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
        drawCracks(gl);
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

    public void addCrack(){
        if(unusuedPoints == null || unusuedPoints.size() == 0){
            unusuedPoints = new ArrayList<Integer>();
            for(int i = 0 ; i < points.size(); i ++){
                unusuedPoints.add(i);
            }
        }
        //crackVectors = new ArrayList<Vector2d>();
        int index = rand.nextInt(unusuedPoints.size());
        int start = unusuedPoints.get(index);
        Point2f startPoint = points.get(start);
        ArrayList<Vector2d> crack = createCrack(x, y, startPoint.getX(), startPoint.getY(),4);
        cracks.add(crack);
        unusuedPoints.remove(index);
    }

    public void drawCracks(GL2 gl) {
        for (ArrayList<Vector2d> crack : cracks) {
            drawCrack(gl, crack);
        }
    }

    public void drawCrack(GL2 gl, ArrayList<Vector2d> crack){
        float startX = x;
        float startY = y;
        float nextX;
        float nextY;
        gl.glColor3f(0.1f, 0.1f, 0.1f);
        gl.glLineWidth(2);
        gl.glBegin(GL.GL_LINE_STRIP);
        gl.glVertex2f(startX, startY);
        for (Vector2d v : crack) {
            nextX = startX + (float) v.getX();
            nextY = startY + (float) v.getY();
            gl.glVertex2f(nextX, nextY);
            startX = nextX;
            startY = nextY;
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

    public void setSpeed(int speed){
        this.speed = speed;
    }
    public void setRotationAmount(double newAmount) {
        this.rotationAmount = newAmount;
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
    public ArrayList<Vector2d> createCrack(float x1, float y1, float x2, float y2, int z){
        ArrayList<Vector2d> crack = new ArrayList<Vector2d>();
        crack.add(new Vector2d(x2-x1, y2-y1));
        return crack;
//        if (z < 2) {
//            crackVectors.add(new Vector2d(x2-x1, y2-y1));
//        }
//        else {
//            float mid_x = (x1 + x2)/2;
//            float mid_y = (y1 + y2)/2;
//            mid_x +=  (float) (rand.nextDouble()-.5)*10*z;
//            mid_y +=  (float) (rand.nextDouble()-.5)*10*z;
//            createCrack(x2,y2,mid_x,mid_y, z/2);
//            createCrack(mid_x,mid_y,x1,y1,z/2);
//        }
    }
    public int getCrackNum(){
        return cracks.size();
    }


    public void resetCracks(){
        cracks = new ArrayList<ArrayList<Vector2d>>();
    }

    public int getSideNum() {
        return sideNum;
    }
}
