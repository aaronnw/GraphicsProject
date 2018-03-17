import com.jogamp.opengl.GL2;
import javax.vecmath.Vector2d;
import java.awt.*;


/**
 * Created by Aaron on 3/9/2018.
 */
public abstract class Shape {
    float x;
    float y;
    Color color;
    Vector2d movement;

    public void draw(GL2 gl){

    }
    public void move(){
        x = (float) (x + movement.getX()/60);
        y = (float) (y + movement.getY()/60);
    }
    public abstract boolean containsPoint(Point p);
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
    public abstract float getLeftPoint();
    public abstract float getTopPoint();
    public abstract float getRightPoint();
    public abstract float getBottomPoint();
    public void setColor(Color color) {
        this.color = color;
    }
}
