import java.util.ArrayList;

/**
 * Created by Aaron on 3/9/2018.
 */
public class Model {
    ArrayList<Shape> shapes;

    public Model(){
        shapes = new ArrayList();
    }
    public void addShape(Shape s){
        shapes.add(s);
    }
    public ArrayList<Shape> getShapes(){
        return shapes;
    }
}
