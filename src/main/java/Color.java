/**
 * Created by Aaron on 3/9/2018.
 */
public enum Color {
    RED, ORANGE, YELLOW, GREEN, BLUE, INDIGO, VIOLET, WHITE;

    public int getR(){
        switch (this){
            case RED:
            case ORANGE:
            case YELLOW:
                return 255;
            case GREEN:
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
    public int getG(){
        switch (this){
            case RED:
            case BLUE:
            case INDIGO:
            case VIOLET:
                return 0;
            case ORANGE:
                return 127;
            case YELLOW:
            case GREEN:
                return 255;
            default:
                return 255;
        }
    }
    public int getB(){
        switch (this){
            case RED:
            case ORANGE:
            case YELLOW:
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
