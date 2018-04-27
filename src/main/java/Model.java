import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Aaron on 3/9/2018.
 */
public class Model extends Observable{
    ArrayList<Shape> shapes;
    Shape target;
    Container container;
    public boolean beginClick = false;
    public boolean newLevel = false;
    public int levelNumb;

    public Model(){
        shapes = new ArrayList();
    }
    public void addShape(Shape s){
        shapes.add(s);
    }
    public ArrayList<Shape> getShapes(){
        return shapes;
    }
    public void setTarget(Shape s){
        target = s;
        setChanged();
        notifyObservers("TARGET_CHANGED");
    }
    public Shape getTarget(){
        return target;
    }
    public void setContainer(Container s){
        container = s;
    }
    public Container getContainer(){
        return container;
    }


}
