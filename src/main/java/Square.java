import javax.vecmath.Vector2d;

/**
 * Created by Aaron on 3/9/2018.
 */
public class Square extends Shape {

    public Square(float x, float y, int size){
        this.x = x;
        this.y = y;
        this.size = size;
        this.direction = new Vector2d(0,0);
        this.color = Color.WHITE;
        rotationAmount = .25*Math.PI;
        sideNum = 4;
        populatePoints();
        populateVectors();
    }

    public Square(float x, float y, int size, Vector2d direction, int speed, Color color){
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = speed;
        this.size = size;
        this.color = color;
        rotationAmount = .25*Math.PI;
        sideNum = 4;
        populatePoints();
        populateVectors();
    }

    public Square makeCopy(){
        return new Square(x,y,size, direction, speed, color);
    }

}
