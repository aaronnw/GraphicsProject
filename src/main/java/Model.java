import java.util.ArrayList;
import java.util.Observable;

/**
 * The model class for our MVC implementation
 * This class handles the data of our
 */
public class Model{
    ArrayList<Shape> shapes;
    ArrayList<Shape> bubbles;
    ArrayList<Spark> sparks;
    ArrayList<Explosion> explosions;
    ArrayList<Collision> collisionList;
    ArrayList<Shape> explodedShapes;
    Shape target;
    private Shape beginButton;
    Container container;
    Level current;
    int lives;
    private boolean gameStarted = false;
    private boolean newLevel = false;
    private boolean gameOver = false;
    private int levelNum;
    private int itemsClick = 0;
    private int totalScore = 0;

    public Model(){
        shapes =  new ArrayList<Shape>();
        bubbles = new ArrayList<Shape>();
        explodedShapes = new ArrayList<Shape>();
        sparks = new ArrayList<Spark>();
        explosions = new ArrayList<Explosion>();
        collisionList = new ArrayList<Collision>();

    }

    /**
     * All of these methods simply change or retrieve the data stored in the model
     */

    public void addShape(Shape s){
        shapes.add(s);
    }
    public ArrayList<Shape> getShapes(){
        return shapes;
    }
    public void setTarget(Shape s){
        target = s;
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

    public void increaseItemsClick(){
        itemsClick++;
    }

    public int getItemsClick(){
        return itemsClick;
    }

    public void resetItemsClick(){
        itemsClick = 0;
    }

    public void increaseScore(int score){
        totalScore = totalScore + score;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void resetScore(){
        totalScore = 0;
    }

    public void increaseLevelNum(){
        levelNum++;
    }
    public void resetLevelNum(){
        levelNum = 0;
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

    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }

    public ArrayList<Collision> getCollisionList() {
        return collisionList;
    }

    public void setCollisionList(ArrayList<Collision> collisionList) {
        this.collisionList = collisionList;
    }

    public ArrayList<Shape> getExplodedShapes() {
        return explodedShapes;
    }

    public void addExplodedShape(Shape s){
        explodedShapes.add(s);
        Explosion exp = new Explosion(s);
        explosions.add(exp);
    }
    public void setExplodedShapes(ArrayList<Shape> explodedShapes) {
        this.explodedShapes = explodedShapes;
    }

    public ArrayList<Explosion> getExplosions() {
        return explosions;
    }

    public void resetViewItems(){
        shapes.clear();
        bubbles.clear();
        sparks.clear();
        explosions.clear();
    }
}
