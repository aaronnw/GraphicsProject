import com.jogamp.opengl.GL2;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2d;

/**
 * This class creates a spark object, which is based on a triangle
 */
public class Spark extends Triangle {
    private int count = 0;
    private int duration = 8;

    Spark(float x, float y, Vector2d direction){
        //First initialize a triangle
        super(x, y, 10, direction, 120, Color.YELLOW);
        super.setRotationAmount(rand.nextDouble()*Math.PI);
    }
    //Do a special drawing for this effect, which lets the spark fade out and disappear
    @Override
    public void draw(GL2 gl){
        gl.glEnable(gl.GL_BLEND);
        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
        gl.glColor4f(color.getR() / 255, color.getG() / 255, color.getB() / 255, ((float) (duration - count))/((float) duration));
        gl.glBegin(GL2.GL_POLYGON);
        for(Point2f p:points){
            gl.glVertex2f(p.x, p.y);
        }
        gl.glVertex2f(points.get(0).getX(), points.get(0).getY());
        gl.glEnd();
        count++;
    }
    //Check for whether we should remove the spark
    public boolean isDead(){
        if(count > duration){
            return  true;
        }else{
            return false;
        }
    }
}
