import com.jogamp.opengl.GL2;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2d;
import java.util.ArrayList;

/**
 * Extends the shape class to override the points and drawing, creating a short explosion
 */
public class Explosion extends Shape {

    private float count = 0;
    private float duration = 12;

    Explosion(Shape s){
        this.x = s.getX();
        this.y = s.getY();
        this.size = s.getSize();
        this.direction = new Vector2d(0,0);
        this.color = Color.RED;
        rotationAmount = 0;
        sideNum = 32;
        populatePoints();
        populateVectors();
    }

    /**
     * Draw like a normal shape, but with random colors
     * @param gl
     */
    @Override
    public void draw(GL2 gl){
        int originalSize = size;
        gl.glEnable(gl.GL_BLEND);
        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
        size =  (int) (((float) size)*(count/duration)) ;
        populatePoints();
        populateVectors();
        int colorIndex = rand.nextInt(3);
        Color explosionColor= this.color;
        switch (colorIndex){
            case 0:
                explosionColor = Color.RED;
                break;
            case 1:
                explosionColor = Color.YELLOW;
                break;
            case 2:
                explosionColor = Color.ORANGE;
                break;
        }
        gl.glColor4f(explosionColor.getR() / 255, explosionColor.getG() / 255, explosionColor.getB() / 255, .8f);
        gl.glBegin(GL2.GL_POLYGON);
        for(Point2f p:points){
            gl.glVertex2f(p.x, p.y);
        }
        gl.glVertex2f(points.get(0).getX(), points.get(0).getY());
        gl.glEnd();
        count++;
        size = originalSize;
    }

    //Add a randomness to the points of the shape, to provide a jagged effect
    @Override
    public void populatePoints(){
        points = new ArrayList<Point2f>();
        for(int i = 0; i < sideNum; i++){
            double randomness = rand.nextDouble()+.2;
            double theta = i*2*Math.PI/sideNum - rotationAmount;
            Point2f nextPoint = new Point2f(
                    (float)(x + ((size/2)*Math.cos(theta)*randomness)),
                    (float)(y - ((size/2)*Math.sin(theta)*randomness)));
            points.add(nextPoint);
        }
    }

    public Circle makeCopy(){
        return new Circle(x, y, size, direction, speed, color);
    }
    //Check if we should remove the explosion yet
    boolean isFinished(){
        if(count > duration){
            return  true;
        }else{
            return false;
        }
    }
}
