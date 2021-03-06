import com.jogamp.opengl.*;

import javax.vecmath.Point2d;
import javax.vecmath.Point2f;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import com.jogamp.opengl.util.awt.TextRenderer;


/**
 * The view for MVC
 * Handles all of the opengl drawing, event handling, etc.
 */
public class View implements GLEventListener, MouseListener{
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
    private void setPixelProjection(GL2 gl, GLAutoDrawable drawable){
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, drawable.getSurfaceWidth(), drawable.getSurfaceHeight(),0, 0,1);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    /***
     * Draw methods
     * Each of these uses the GL2 object and calls the appropriate draw methods on each object in the model
     */
    ////////////////////////////////////////////////////////////////////////////////////////////////////
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
        target.setX(w - 130);
        target.setY(target.getSize()/2 + 20);
        target.update(gl);
    }
    private void drawLives(GL2 gl){
        gl.glColor3f(Color.RED.getR(), Color.RED.getG(), Color.RED.getB());
        gl.glLineWidth(5);
        float startX = 40;
        float startY = playAreaTop - 75;
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

    /**
        Level Handling
        Checks where we are at in the game and draws the appropriate things
     */
    private void processLevel(GL2 gl, GLAutoDrawable drawable){
        // view that shows when the game begins
        if(!model.isGameStarted()){
            drawStartMenu(gl, drawable);
        }
        // view that shows for every new level
        else if(model.isNewLevel()){
            drawNewLevelMenu(gl, drawable);
        }
        else if(model.isGameOver()){
            drawGameOverMenu(gl, drawable);
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
            drawScore(gl, drawable);
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    /***
     * Level methods
     * Each of these uses the GL2 object and draws a simple menu with an improvised "button"
     */
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    public void drawStartMenu(GL2 gl, GLAutoDrawable drawable){
        Font gameStartFont = new Font("Verdana", Font.PLAIN, 38);
        Font buttonFont = new Font("Verdana", Font.BOLD, 30);

        renderer = new TextRenderer(gameStartFont);
        renderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
        renderer.setColor(1.0f, 1.0f, 0, 1.0f);
        renderer.draw("Welcome to Click-Tap-Match!", w/2 - 240, h/2 + 150);
        renderer.endRendering();

        Square beginButton = new Square(w/2,h/2 + 20, 220);
        model.setBeginButton(beginButton);
        beginButton.update(gl);

        renderer = new TextRenderer(buttonFont);
        renderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
        renderer.setColor(0.0f, 0.0f, 0.0f, 1.0f);
        renderer.draw("Begin", w/2-43, h/2-20);
        renderer.endRendering();
    }

    public void drawNewLevelMenu(GL2 gl, GLAutoDrawable drawable){
        Font newLevelFont = new Font("Verdana", Font.PLAIN, 38);
        Font buttonFont = new Font("Verdana", Font.PLAIN, 30);

        renderer = new TextRenderer(newLevelFont);
        renderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
        renderer.setColor(1.0f, 1.0f, 0, 1.0f);
        renderer.draw("Congratulations! You have completed level " + model.getLevelNum(), w/2 - 400, h/2 + 150);
        renderer.endRendering();

        model.getBeginButton().update(gl);

        renderer = new TextRenderer(buttonFont);
        renderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
        renderer.setColor(0.0f, 0.0f, 0.0f, 1.0f);
        renderer.draw("Continue", w/2-70, h/2 - 30);
        renderer.endRendering();
    }

    public void drawGameOverMenu(GL2 gl, GLAutoDrawable drawable){
        Font gameOverFont = new Font("Verdana", Font.PLAIN, 38);
        Font buttonFont = new Font("Verdana", Font.PLAIN, 30);
        Font finalScoreFont = new Font("Verdana", Font.PLAIN, 25);

        renderer = new TextRenderer(gameOverFont);
        renderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
        renderer.setColor(1.0f, 1.0f, 0, 1.0f);
        renderer.draw("GAME OVER", w/2 - 110, h/2 + 150);
        renderer.endRendering();

        model.getBeginButton().update(gl);

        renderer = new TextRenderer(buttonFont);
        renderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
        renderer.setColor(0.0f, 0.0f, 0.0f, 1.0f);
        renderer.draw("Restart", w/2-53, h/2-20);
        renderer.endRendering();

        renderer = new TextRenderer(finalScoreFont);
        renderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
        renderer.setColor(1.0f, 1.0f, 0, 1.0f);
        renderer.draw("Final Score: " + model.getTotalScore(), w/2 - 85, h/2 + 90);
        renderer.endRendering();
    }

    public void drawScore(GL2 gl, GLAutoDrawable drawable){
        Font scoreFont = new Font("Verdana", Font.PLAIN, 38);

        renderer = new TextRenderer(scoreFont);
        renderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
        renderer.setColor(1.0f, 1.0f, 0, 1.0f);
        renderer.draw("Score: " + model.getTotalScore(), w/2-100, h/2+450);
        renderer.endRendering();

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

    //Processes when a user clicks in the area
    public void mousePressed(MouseEvent e) {
        controller.processClick(new Point2f(e.getX(), e.getY()));
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}
