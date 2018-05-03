import javax.vecmath.Vector2d;

/**
 * Bubble is based on circle, but has special movement
 */
public class Bubble extends Circle {
    private int count = 30;

    Bubble(float x, float y, int size, Vector2d direction, int speed, Color color){
        super(x, y, size, direction, speed, color);
    }

    public Bubble makeCopy(){
        return new Bubble(x, y, size, direction, speed, color);
    }

    //Update the movement based on position every 30 frames as the bubble rises
    @Override
    public void move(){
            if(count > 30) {
                direction = new Vector2d(Math.sin(y), direction.getY());
                count = 0;
            }else{
                count++;
            }
            x = (float) (x + speed* direction.getX()/10);
            y = (float) (y + speed* direction.getY()/10);
    }

}
