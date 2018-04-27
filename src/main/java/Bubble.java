import javax.vecmath.Vector2d;

/**
 * Created by Aaron on 3/9/2018.
 */
public class Bubble extends Circle {
    private int count = 30;
    private int test  = 30;

    public Bubble(float x, float y, int size){
        super(x, y, size);
    }

    public Bubble(float x, float y, int size, Vector2d direction, int speed, Color color){
        super(x, y, size, direction, speed, color);
    }

    public Bubble makeCopy(){
        return new Bubble(x, y, size, direction, speed, color);
    }

    @Override
    public void move(){
            if(count > test) {
                direction = new Vector2d(Math.sin(y), direction.getY());
                count = 0;
            }else{
                count++;
            }
            x = (float) (x + speed* direction.getX()/10);
            y = (float) (y + speed* direction.getY()/10);
    }

}
