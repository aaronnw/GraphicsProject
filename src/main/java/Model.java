import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Aaron on 3/9/2018.
 */
public class Model extends Observable{
    ArrayList<Shape> shapes;
    Shape target;
    Container container;
    Level current;
    int lives;

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
    public void setLives(int lives){
        this.lives = lives;
    }
    public int getLives(){
        return lives;
    }
    public void setCurrentLevel(Level level){
        current = level;
    }
    public Level getCurrentLevel(){
        return current;
    }


}
