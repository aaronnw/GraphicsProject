import javax.vecmath.Vector2d;

/**
 * Created by Aaron on 3/9/2018.
 */
public class Triangle extends Shape {

    public Triangle(float x, float y, int size){
        this.x = x;
        this.y = y;
        this.size = size;
        this.movement = new Vector2d(0,0);
        this.color = Color.WHITE;
        rotationAmount = -Math.PI/2;
        sideNum = 3;
        populatePoints();
        populateVectors();
    }

    public Triangle(float x, float y, int size, Vector2d movement, Color color){
        this.x = x;
        this.y = y;
        this.movement = movement;
        this.size = size;
        this.color = color;
        rotationAmount = -Math.PI/2;
        sideNum = 3;
        populatePoints();
        populateVectors();
    }

    public Triangle makeCopy(){
        return new Triangle(x,y,size, movement, color);
    }

}
