import javax.vecmath.Point2f;

/**
 * Collision simply stores two shapes and an intersection
 */
public class Collision {
    private Shape s1;
    private Shape s2;
    private Point2f intersection;

    Collision(Shape s1, Shape s2, Point2f intersection){
        this.s1 = s1;
        this.s2 = s2;
        this.intersection = intersection;
    }
    Shape getS1(){
        return s1;
    }
    Shape getS2(){
        return s2;
    }
    Point2f getP(){
        return intersection;
    }
    //Provide a comparator so that we can see if two collisions are between the same shapes
    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (!Collision.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Collision other = (Collision) obj;
        boolean s1Shared = s1.equals(other.getS1()) || s1.equals(other.getS2());
        boolean s2Shared = s2.equals(other.getS1()) || s2.equals(other.getS2());
        if(s1Shared && s2Shared){
            return true;
        }else{
            return false;
        }

    }
}
