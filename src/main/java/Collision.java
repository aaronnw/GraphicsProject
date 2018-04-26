import javax.vecmath.Point2f;

public class Collision {
    Shape s1;
    Shape s2;
    Point2f intersection;

    public Collision(Shape s1, Shape s2, Point2f intersection){
        this.s1 = s1;
        this.s2 = s2;
        this.intersection = intersection;
    }
    public Shape getS1(){
        return s1;
    }
    public Shape getS2(){
        return s2;
    }
    public Point2f getP(){
        return intersection;
    }
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
