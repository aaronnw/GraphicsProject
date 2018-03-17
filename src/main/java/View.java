import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * Created by Aaron on 3/9/2018.
 */
public class View implements GLEventListener, MouseListener {
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
    private void drawShapes(GL2 gl){
        for (Shape s: model.getShapes()) {
            s.draw(gl);
            //Added some basic movement
            if(s.getTopPoint() < playAreaTop || s.getBottomPoint() > h){
                s.getMovement().setY(-s.getMovement().getY());
            }
            if(s.getLeftPoint() < 0 || s.getRightPoint() > w){
                s.getMovement().setX(-s.getMovement().getX());
            }
            s.move();
        }
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
        controller.processClick(new Point(e.getX(), e.getY()));
    }

    public void mousePressed(MouseEvent e) {

        controller.processClick(new Point(e.getX(), e.getY()));
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}
