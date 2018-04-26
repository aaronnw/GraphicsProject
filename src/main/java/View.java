import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2d;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;


/**
 * Created by Aaron on 3/9/2018.
 */
public class View implements GLEventListener, MouseListener, Observer {
    private int	w;
    private int	h;
    private int counter = 0;
    private Controller controller;
    private Model model;
    private int playAreaTop;
    public View(){

    }
    public void setController(Controller c){
        controller = c;
    }
    public void setModel(Model m){
        model = m;
    }

    public void init(GLAutoDrawable drawable) {
        w = drawable.getSurfaceWidth();
        h = drawable.getSurfaceHeight();
    }

    public void dispose(GLAutoDrawable drawable) {

    }

    public void display(GLAutoDrawable drawable) {
        update();
        render(drawable);
    }

    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int w, int h) {
        this.w = w;
        this.h = h;

    }
    public void update(){
        counter ++;
    }
    private  void render(GLAutoDrawable drawable){
        GL2 gl = drawable.getGL().getGL2();
       gl.glClear(GL.GL_COLOR_BUFFER_BIT);		// Clear the buffer
        setPixelProjection(gl, drawable);
        drawTargetArea(gl);
        drawTarget(gl);
        drawShapes(gl);
    }
    private void	setPixelProjection(GL2 gl, GLAutoDrawable drawable)
    {
        GLU glu = new GLU();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, drawable.getSurfaceWidth(), drawable.getSurfaceHeight(),0, 0,1);
    }
    private void drawTargetArea(GL2 gl){
        gl.glColor3f(Color.WHITE.getR(), Color.WHITE.getG(), Color.WHITE.getB());
        gl.glLineWidth(5);
        gl.glBegin(GL.GL_LINES);
        gl.glVertex2i(0, playAreaTop);
        gl.glVertex2i(w, playAreaTop);
        gl.glEnd();
    }
    private void drawTarget(GL2 gl){
        Shape target = model.getTarget();
        target.setX(w - 200);
        target.setY(target.getSize()/2 + 10);
        target.update(gl);
    }
    private void drawShapes(GL2 gl){
        for (Shape s:model.getShapes()){
            s.update(gl);
        }
        for (Shape s: model.getShapes()) {
            checkCollisions(s);
        }
        for (Shape s:model.getShapes()){
            s.move();
        }
    }
    private void checkCollisions(Shape s){
        Container container = model.getContainer();
        for(Point2f p:s.getPoints()) {
            if (!container.containsPoint(p)) {
                //Find the vector from the point to the center
                Vector2d toCenter = new Vector2d(container.getCenter().getX() - p.getX(), container.getCenter().getY() - p.getY());
                //If the shape is already headed back to the center don't flip the direction
                if(s.getMovement().angle(toCenter) < Math.PI/2){
                    return;
                }
                //Find the edge the shape hit
                Vector2d edge = container.violatingEdge(p);
                //Get the normal vector to the edge
                Vector2d normal = new Vector2d(edge.getY(), -edge.getX());
                normal.normalize();
                Vector2d v = new Vector2d(s.getMovement().getX(), s.getMovement().getY());
                normal.scale(v.dot(normal) * 2);
                //The actual reflection vector
                Vector2d newMovement = new Vector2d(v.getX() - normal.getX(), v.getY() - normal.getY());
                s.setMovement(newMovement);
                return;
            }
        }
        for(Shape each:model.getShapes()){
            if(!s.equals(each) && controller.distanceBetween(s, each) < s.getSize() + each.getSize()){
                for(Point2f p:s.getPoints()) {
                    if (each.containsPoint(p)) {
                        //One of the points of shape s has crossed an edge of shape each
                        Collision = new Collision(s, each, p);
                        //Find the edge the shape hit
                        Vector2d edge = each.violatingInsideEdge(p);
                        handleCollision(s, each, edge, p);
                        break;
                    }
                }
            }
        }
    }

    private void handleCollision(Shape encroachingShape, Shape edgeShape, Vector2d edge, Point2f p){
        Vector2d encroachingShapeMovement = encroachingShape.getMovement();
        Vector2d edgeShapeMovement = edgeShape.getMovement();
        Vector2d lineBetween = new Vector2d(edgeShape.getX() - encroachingShape.getX(), edgeShape.getY() - encroachingShape.getY());
        boolean edgeMovingAway = edgeShapeMovement.angle(lineBetween) > Math.PI/2;
        boolean encroachingShapeMovingAway = encroachingShapeMovement.angle(lineBetween) > Math.PI/2;
        if( edgeMovingAway && encroachingShapeMovingAway){
            return;
        }

        //Get the normal vector to the edge
        Vector2d rightNormal = new Vector2d(edge.getY(), -edge.getX());
        rightNormal.normalize();
        rightNormal.scale(encroachingShapeMovement.dot(rightNormal) * 2);
        //The actual reflection vector
        Vector2d newEncroachingShapeMovement = new Vector2d(encroachingShapeMovement.getX() - rightNormal.getX(), encroachingShapeMovement.getY() - rightNormal.getY());


        //Get the normal vector to the edge
        rightNormal = new Vector2d(edge.getY(), -edge.getX());
        rightNormal.normalize();
        rightNormal.scale(edgeShapeMovement.dot(rightNormal) * 2);
        Vector2d newEdgeShapeMovement = new Vector2d(edgeShapeMovement.getX() - rightNormal.getX(), edgeShapeMovement.getY() - rightNormal.getY());

        encroachingShape.setMovement(newEncroachingShapeMovement);
        edgeShape.setMovement(newEdgeShapeMovement);
    }

    public int getWidth(){
        return w;
    }
    public int getHeight(){
        return h;
    }
    public void setWidth(int w){
        this.w = w;
    }
    public void setHeight(int h){
        this.h = h;
    }
    public void setPlayAreaTop(int top){
        playAreaTop = top;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        controller.processClick(new Point2f(e.getX(), e.getY()));
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void update(Observable o, Object arg) {
    }
}
