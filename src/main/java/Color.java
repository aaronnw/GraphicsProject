/**
 * Created by Aaron on 3/9/2018.
 */
public enum Color {
    RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE, WHITE;

    public float getR(){
        switch (this){
            case RED:
                return 255;
            case ORANGE:
                return 255;
            case YELLOW:
                return 255;
            case GREEN:
                return 0;
            case BLUE:
                return 0;
            case PURPLE:
                return 100;
            default:
                return 255;
        }
    }
    public float getG(){
        switch (this){
            case RED:
                return 0;
            case BLUE:
                return 0;
            case PURPLE:
                return 0;
            case ORANGE:
                return 127;
            case YELLOW:
                return 255;
            case GREEN:
                return 255;
            default:
                return 255;
        }
    }
    public float getB(){
        switch (this){
            case RED:
                return 0;
            case ORANGE:
                return 0;
            case YELLOW:
                return 0;
            case GREEN:
                return 0;
            case BLUE:
                return 255;
            case PURPLE:
                return 175;
            default:
                return 255;
        }
    }
    public int getA(){
        return 1;
    }


}
