import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Aaron on 3/9/2018.
 */
public class Model extends Observable{
    ArrayList<Shape> shapes;
    ArrayList<Shape> bubbles;
    Shape target;
    Container container;
    Level current;
    int lives;
    public boolean beginClick = false;
    public boolean newLevel = false;
    public int levelNumb;

    public Model(){
        shapes =  new ArrayList<Shape>();
        bubbles = new ArrayList<Shape>();
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
    public void addBubble(Shape s){
        bubbles.add(s);
    }
    public ArrayList<Shape> getBubbles(){
        return  bubbles;
    }



}
