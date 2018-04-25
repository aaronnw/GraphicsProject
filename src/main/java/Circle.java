import javax.vecmath.Vector2d;

/**
 * Created by Aaron on 3/9/2018.
 */
public class Circle extends Shape {
    private int radius;

    public Circle(float x, float y, int size){
        this.x = x;
        this.y = y;
        this.size = size;
        this.movement = new Vector2d(0,0);
        rotationAmount = 0;
        sideNum = 32;
        populatePoints();
        populateVectors();
    }

    public Circle(float x, float y, int size, Vector2d movement, Color color){
        this.x = x;
        this.y = y;
        this.movement = movement;
        this.size = size;
        this.color = color;
        rotationAmount = 0;
        sideNum = 32;
        populatePoints();
        populateVectors();
    }

    public float getLeftPoint() {
        return x- radius;
    }

    public float getTopPoint() {
        return y- radius;
    }

    public float getRightPoint() {
        return x+ radius;
    }

    public float getBottomPoint() {
        return y+ radius;
    }

    public Circle makeCopy(){
        return new Circle(x, y, size, movement, color);
    }

}
