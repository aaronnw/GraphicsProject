import com.jogamp.opengl.GL2;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2d;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Aaron on 3/9/2018.
 */
public class Square extends Shape {
    private int sideLength;

    public Square(float x, float y, int size){
        this.x = x;
        this.y = y;
        sideLength = size;
        this.size = size;
        this.movement = new Vector2d(0,0);
        startX = x+size/2;
        startY = y+size/2;
        populateVectors();
        populatePoints();
    }

    public Square(float x, float y, int size, Vector2d movement, Color color){
        this.x = x;
        this.y = y;
        this.movement = movement;
        this.size = size;
        sideLength = size;
        this.color = color;
        startX = x+size/2;
        startY = y+size/2;
        populateVectors();
        populatePoints();
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
