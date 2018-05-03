/**
 * Simple data class to hold level information
 */
public class Level {
    private int lives;
    private int targetNumber;
    private int numShapes;
    private int maxShapeSize;
    private int minShapeSize;
    private int maxVel;
    private int minVel;

    public Level(int lives, int targetNumber, int numShapes, int maxShapeSize, int minShapeSize, int maxVel, int minVel){
        this.lives = lives;
        this.targetNumber = targetNumber;
        this.numShapes = numShapes;
        this.maxShapeSize = maxShapeSize;
        this.minShapeSize = minShapeSize;
        this.maxVel = maxVel;
        this.minVel = minVel;
    }

    /**
     * All the methods here just edit or retrieve the constants set for the level
     * @return
     */
    public int getLives() {
        return lives;
    }

    public void removeLife(){
        lives--;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getTargetNumber() {
        return targetNumber;
    }

    public void setTargetNumber(int targetNumber) {
        this.targetNumber = targetNumber;
    }

    public int getNumShapes() {
        return numShapes;
    }

    public void setNumShapes(int numShapes) {
        this.numShapes = numShapes;
    }

    public int getMaxShapeSize() {
        return maxShapeSize;
    }

    public void setMaxShapeSize(int maxShapeSize) {
        this.maxShapeSize = maxShapeSize;
    }

    public int getMinShapeSize() {
        return minShapeSize;
    }

    public void setMinShapeSize(int minShapeSize) {
        this.minShapeSize = minShapeSize;
    }

    public int getMaxVel() {
        return maxVel;
    }

    public void setMaxVel(int maxVel) {
        this.maxVel = maxVel;
    }

    public int getMinVel() {
        return minVel;
    }

    public void setMinVel(int minVel) {
        this.minVel = minVel;
    }


}
