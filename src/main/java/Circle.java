import javax.vecmath.Vector2d;

/**
 * This class extends shape, but provides some variables specific to this shape type
 */
public class Circle extends Shape {

    public Circle(float x, float y, int size){
        this.x = x;
        this.y = y;
        this.size = size;
        this.direction = new Vector2d(0,0);
        this.color = Color.WHITE;
        rotationAmount = 0;
        sideNum = 32;
        populatePoints();
        populateVectors();
    }

    public Circle(float x, float y, int size, Vector2d direction, int speed, Color color){
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = speed;
        this.size = size;
        this.color = color;
        rotationAmount = 0;
        sideNum = 32;
        populatePoints();
        populateVectors();
    }

    public Circle makeCopy(){
        return new Circle(x, y, size, direction, speed, color);
    }

}
