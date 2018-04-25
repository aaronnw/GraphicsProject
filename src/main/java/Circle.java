import com.jogamp.opengl.GL2;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2d;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

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
        startX = x+size/2;
        startY = y;
        sideNum = 32;
        populateVectors();
        populatePoints();
    }

    public Circle(float x, float y, int size, Vector2d movement, Color color){
        this.x = x;
        this.y = y;
        this.movement = movement;
        this.size = size;
        this.color = color;
        startX = x+size/2;
        startY = y;
        sideNum = 32;
        populateVectors();
        populatePoints();
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
