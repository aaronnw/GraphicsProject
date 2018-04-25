import javax.vecmath.Vector2d;

/**
 * Created by Aaron on 3/9/2018.
 */
public class Square extends Shape {
    private int sideLength;

    public Square(float x, float y, int size){
        this.x = x;
        this.y = y;
        this.size = size;
        this.movement = new Vector2d(0,0);
        rotationAmount = .25*Math.PI;
        sideNum = 4;
        populatePoints();
        populateVectors();
    }

    public Square(float x, float y, int size, Vector2d movement, Color color){
        this.x = x;
        this.y = y;
        this.movement = movement;
        this.size = size;
        this.color = color;
        rotationAmount = .25*Math.PI;
        sideNum = 4;
        populatePoints();
        populateVectors();
    }

    public float getLeftPoint() {
        return x-sideLength/2;
    }

    public float getTopPoint() {
        return y-sideLength/2;
    }

    public float getRightPoint() {
        return x+sideLength/2;
    }

    public float getBottomPoint() {
        return y+sideLength/2;
    }

    public Square makeCopy(){
        return new Square(x,y,size, movement, color);
    }

}
