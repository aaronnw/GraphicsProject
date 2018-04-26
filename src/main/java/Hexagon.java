import javax.vecmath.Vector2d;

/**
 * Created by Aaron on 3/9/2018.
 */
public class Hexagon extends Shape {

    public Hexagon(float x, float y, int size){
        this.x = x;
        this.y = y;
        this.size = size;
        this.movement = new Vector2d(0,0);
        this.color = Color.WHITE;
        rotationAmount = 0;
        sideNum = 6;
        populatePoints();
        populateVectors();
    }

    public Hexagon(float x, float y, int size, Vector2d movement, Color color){
        this.x = x;
        this.y = y;
        this.movement = movement;
        this.size = size;
        this.color = color;
        rotationAmount = 0;
        sideNum = 6;
        populatePoints();
        populateVectors();
    }

    public Hexagon makeCopy(){
        return new Hexagon(x,y,size, movement, color);
    }

}
