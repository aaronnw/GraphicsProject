import com.jogamp.opengl.GL2;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2d;

public class Spark extends Triangle {
    int count = 1;
    int duration = 3;

    public Spark(float x, float y, Vector2d direction){
        super(x, y, 7, direction, 200, Color.YELLOW);
        super.setRotationAmount(rand.nextDouble()*Math.PI);
    }
    @Override
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
        count++;
    }
    public boolean isDead(){
        if(count > duration){
            return  true;
        }else{
            return false;
        }
    }
}
