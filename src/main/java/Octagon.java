import javax.vecmath.Vector2d;

/**
 * Created by Aaron on 3/9/2018.
 */
public class Octagon extends Shape {

    public Octagon(float x, float y, int size){
        this.x = x;
        this.y = y;
        this.size = size;
        this.movement = new Vector2d(0,0);
        this.color = Color.WHITE;
        rotationAmount = Math.PI/8;
        sideNum = 8;
        populatePoints();
        populateVectors();
    }

    public Octagon(float x, float y, int size, Vector2d movement, Color color){
        this.x = x;
        this.y = y;
        this.movement = movement;
        this.size = size;
        this.color = color;
        rotationAmount = Math.PI/8;
        sideNum = 8;
        populatePoints();
        populateVectors();
    }

    public Octagon makeCopy(){
        return new Octagon(x,y,size, movement, color);
    }

}
