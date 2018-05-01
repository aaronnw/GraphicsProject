import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Aaron on 3/9/2018.
 */
public class Model extends Observable{
    ArrayList<Shape> shapes;
    ArrayList<Shape> bubbles;
    ArrayList<Spark> sparks;
    ArrayList<Collision> collisionList;
    Shape target;
    private Shape beginButton;
    Container container;
    Level current;
    int lives;
    private boolean gameStarted = false;
    private boolean newLevel = false;
    private boolean gameOver = false;
    private int levelNum;

    public Model(){
        shapes =  new ArrayList<Shape>();
        bubbles = new ArrayList<Shape>();
        sparks = new ArrayList<Spark>();
        collisionList = new ArrayList<Collision>();
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
    public void addSpark(Spark s){
        sparks.add(s);
    }
    public void removeLife(){
        if(lives > 1){
            lives = lives -1;
        }else{
            lives = 0;
            gameOver = true;
        }
    }
    public ArrayList<Shape> getBubbles(){
        return  bubbles;
    }
    public ArrayList<Spark> getSparks(){
        return  sparks;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public boolean isNewLevel() {
        return newLevel;
    }

    public void setNewLevel(boolean newLevel) {
        this.newLevel = newLevel;
    }

    public int getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }

    public Shape getBeginButton() {
        return beginButton;
    }

    public void setBeginButton(Shape beginButton) {
        this.beginButton = beginButton;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public ArrayList<Collision> getCollisionList() {
        return collisionList;
    }

    public void setCollisionList(ArrayList<Collision> collisionList) {
        this.collisionList = collisionList;
    }
}
