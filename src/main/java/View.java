import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;

import javax.vecmath.Point2d;
import javax.vecmath.Point2f;
import javax.vecmath.Vector2d;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import com.jogamp.opengl.util.awt.TextRenderer;


/**
 * Created by Aaron on 3/9/2018.
 */
public class View implements GLEventListener, MouseListener, Observer {
    private int	w;
    private int	h;
    private Controller controller;
    private Model model;
    private int playAreaTop;
    private TextRenderer renderer;

    View(Model m){
        this.model = m;
    }
    void setController(Controller c){
        controller = c;
    }

    public void init(GLAutoDrawable drawable) {
        w = drawable.getSurfaceWidth();
        h = drawable.getSurfaceHeight();
    }

    public void dispose(GLAutoDrawable drawable) {

    }

    public void display(GLAutoDrawable drawable) {
        render(drawable);
    }

    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int w, int h) {
        this.w = w;
        this.h = h;

    }

    private  void render(GLAutoDrawable drawable){
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);		// Clear the buffer
        setPixelProjection(gl, drawable);
        processLevel(gl, drawable);
    }
    private void	setPixelProjection(GL2 gl, GLAutoDrawable drawable)
    {
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
    private void drawLives(GL2 gl){
        gl.glColor3f(Color.RED.getR(), Color.RED.getG(), Color.RED.getB());
        gl.glLineWidth(5);
        float startX = 40;
        float startY = playAreaTop - 50;
        for(int i=0; i < model.getLives(); i++) {
            drawHeart(gl, startX, startY);
            startX += 70;
        }
    }
    private void drawHeart(GL2 gl,float  x, float y){
        float circleRad = 12;
        gl.glBegin(GL2.GL_POLYGON);
        drawArc(gl, x + circleRad, y, circleRad);
        drawArc(gl, x - circleRad, y, circleRad);
        gl.glVertex2f(x, y+22);
        gl.glVertex2f(x+circleRad*2, y);
        gl.glEnd();
    }
    private void drawArc(GL2 gl,float  x, float y, float circleRad){
        int numSides = 16;
        for(int i = 0; i < numSides;  i++){
            double theta = i*Math.PI/numSides;
            gl.glVertex2d(x + Math.cos(theta)*circleRad, y-Math.sin(theta)*circleRad);
        }
    }
    private void drawShapes(GL2 gl){
        model.setCollisionList(new ArrayList<Collision>());
        for (Shape s:model.getShapes()){
            s.update(gl);
        }
        for (Shape s: model.getShapes()) {
            controller.checkShapeCollision(s);
        }

        ArrayList<Collision> trimmedCollisions = new ArrayList<Collision>();
        boolean add;
        for (Collision c:model.getCollisionList()){
            add = true;
            for(Collision other: trimmedCollisions){
                if(c.equals(other)){
                    add = false;
                }
            }
            if(add){
                trimmedCollisions.add(c);
            }
        }
        for(Collision c: trimmedCollisions){
            controller.handleCollision(c);
            controller.createSpark(c.getS1(), c.getS2(), c.getP());
        }
        for (Shape s: model.getShapes()) {
            controller.checkWallCollision(s);
        }
        for (Shape s:model.getShapes()){
            s.move();
        }
    }
    private void drawBubbles(GL2 gl){
        Iterator<Shape> iter = model.getBubbles().iterator();
        while (iter.hasNext()) {
            Shape s = iter.next();
            s.update(gl);
            gl.glLineWidth(1);
            s.drawOutline(gl);
            s.drawHighlight(gl);
            s.move();
            if(s.getY() < -s.getSize()){
                iter.remove();
            }
        }
    }
    private void drawSparks(GL2 gl){
        Iterator<Spark> iter = model.getSparks().iterator();
        while (iter.hasNext()) {
            Spark s = iter.next();
            s.update(gl);
            s.move();
            if(s.isDead()){
                iter.remove();
            }
        }
    }
    private void drawExplosions(GL2 gl){
        Iterator<Explosion> iter = model.getExplosions().iterator();
        while (iter.hasNext()) {
            Explosion exp = iter.next();
            exp.update(gl);
            exp.move();
            if(exp.isFinished()){
                iter.remove();
            }
        }
    }


    private ArrayList<Point2d> generateLightning(double x1,double y1, double x2,double y2,int z){
        ArrayList<Point2d> lightning = new ArrayList<Point2d>();
        if (z < 3) {
            lightning.add(new Point2d(x1,y1));
            lightning.add(new Point2d(x2,y2));
        }
        else {
            double mid_x = (x2+x1)/2;
            double mid_y = (y2+y1)/2;
            mid_x += (Math.random()-.5)*z;
            mid_y += (Math.random()-.5)*z;
            generateLightning(x1,y1,mid_x,mid_y,z/2);
            generateLightning(x2,y2,mid_x,mid_y,z/2);
        }
        return lightning;
    }

    /**
        Created by Taylor Humphrey
     */
    private void processLevel(GL2 gl, GLAutoDrawable drawable){
        // view that shows when the game begins
        if(!model.isGameStarted()){
            renderer = new TextRenderer(new Font("Verdana", Font.PLAIN, 38));
            renderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
            renderer.setColor(1.0f, 1.0f, 0, 1.0f);
            renderer.draw("Welcome to Click-Tap-Match!", w/2 - 240, h/2 + 150);
            renderer.endRendering();

            Square beginButton = new Square(w/2,h/2, 150);
            model.setBeginButton(beginButton);
            beginButton.update(gl);

            renderer = new TextRenderer(new Font("Verdana", Font.PLAIN, 30));
            renderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
            renderer.setColor(0.0f, 0.0f, 0.0f, 1.0f);
            renderer.draw("Begin", w/2-43, h/2-5);
            renderer.endRendering();
        }
        // view that shows for every new level
        else if(model.isNewLevel()){
            renderer = new TextRenderer(new Font("Verdana", Font.PLAIN, 38));
            renderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
            renderer.setColor(1.0f, 1.0f, 0, 1.0f);
            renderer.draw("Congratulation! You have completed level: " + model.getLevelNum(), w/2 - 240, h/2 + 150);
            renderer.endRendering();

            model.getBeginButton().update(gl);

            renderer = new TextRenderer(new Font("Verdana", Font.PLAIN, 20));
            renderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
            renderer.setColor(0.0f, 0.0f, 0.0f, 1.0f);
            renderer.draw("Continue", w/2-43, h/2-5);
            renderer.endRendering();
        }
        // draws every level
        else{
            controller.removeExplodedShapes();
            drawTargetArea(gl);
            drawTarget(gl);
            drawLives(gl);
            drawShapes(gl);
            drawBubbles(gl);
            drawSparks(gl);
            drawExplosions(gl);
        }

    }

    int getWidth(){
        return w;
    }
    int getHeight(){
        return h;
    }
    void setWidth(int w){
        this.w = w;
    }
    void setHeight(int h){
        this.h = h;
    }
    void setPlayAreaTop(int top){
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
