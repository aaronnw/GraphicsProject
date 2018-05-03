/**
 * Contains the main method
 * Sets up the MVC structure and calls the init in controller
 */
public class Driver {
    //Set up the MVC structure and start the controller
    public static void main(String[] args) {
        //Size of window
        int width = 1000;
        int height = 1100;
        Model m = new Model();
        View v = new View(m);
        Controller c = new Controller(m, v);
        v.setController(c);
        c.init(width, height);
    }
}
