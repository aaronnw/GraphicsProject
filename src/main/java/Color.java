/**
 * Created by Aaron on 3/9/2018.
 */
public enum Color {
    RED, ORANGE, YELLOW, GREEN, BLUE, INDIGO, VIOLET, WHITE;

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
            case INDIGO:
                return 75;
            case VIOLET:
                return 148;
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
            case INDIGO:
                return 0;
            case VIOLET:
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
            case INDIGO:
                return 130;
            case VIOLET:
                return 211;
            default:
                return 255;
        }
    }
    public int getA(){
        return 1;
    }


}
