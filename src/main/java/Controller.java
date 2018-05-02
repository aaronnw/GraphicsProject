import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import javax.swing.*;
import javax.vecmath.Point2f;
import javax.vecmath.Vector2d;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * The controller for MVC
 * Handles the logic of the program, collisions, model changes, etc.
 */
public class Controller {
    private Model model;
    private View view;
    private int playAreaTop = 150;
    private int defaultSize = 100;
    private Random rand = new Random();

    Controller(Model m, View v){
        model = m;
        view = v;
    }
    //Start the project
    void init(int width, int height){
        //Set up the OpenGL default settings
        GLProfile profile = GLProfile.getDefault();
        GLCapabilities capabilities = new GLCapabilities(profile);
        GLCanvas canvas = new GLCanvas(capabilities);
        //Create the frame and set the sizes
        JFrame frame = new JFrame("Shape Game");
        canvas.setPreferredSize(new Dimension(width, height));
        view.setWidth(width);
        view.setHeight(height);
        view.setPlayAreaTop(playAreaTop);
        frame.setBounds(0, 0, width, height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.getContentPane().add(canvas);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        //Add listeners to the canvas
        canvas.addGLEventListener(view);
        canvas.addMouseListener(view);
        //Create a level to start
        Level current = new Level(3, 10, 10, 150, 100, 100, 50);
        model.setCurrentLevel(current);
        model.setLives(current.getLives());
        //Add the shapes to the play area
        addShapes(current.getNumShapes());
        //Set the target to match
        setTarget();
        //Set the square container that holds the game, for edge collision
        setContainer();

        //Animate at 60 fps
        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    //Add a given number of shapes to the play area
    private void addShapes(int numShapes){
        //Get the shape parameters from the current level
        Level current = model.getCurrentLevel();
        int minShapeSize = current.getMinShapeSize();
        int maxShapeSize = current.getMaxShapeSize();
        int minVel = current.getMinVel();
        int maxVel = current.getMaxVel();
        //Get the list of all the possible colors
        Color[] colors = Color.values();
        //Add each shape
        for (int i = 0; i < numShapes ; i++) {
            int size = minShapeSize + rand.nextInt(maxShapeSize-minShapeSize);
            int x = rand.nextInt(view.getWidth()-size) + size/2;
            int y = playAreaTop + rand.nextInt(view.getHeight()-size-playAreaTop) + size/2;
            int colorVal = rand.nextInt(colors.length);
            Shape shape;
            //Pick a random type of shape to create
            int shapeType = rand.nextInt(4);
            switch (shapeType){
                case 0:
                    shape = new Triangle(x, y, size);
                    break;
                case 1:

                    shape = new Square(x, y, size);
                    break;
                case 2:
                    shape = new Hexagon(x, y, size);
                    break;
                case 3:
                    shape = new Octagon(x, y, size);
                    break;
                case 4:
                    shape = new Circle(x, y, size);
                    break;
                default:
                    shape = new Circle(x, y, size);
                    break;
            }
            //Set the color and velocity within the level parameters
            shape.setColor(colors[colorVal]);
            shape.setSpeed(rand.nextInt(maxVel-minVel)+minVel);
            double movementX = rand.nextDouble();
            double movementY = 1-movementX;
            Vector2d movement = new Vector2d(movementX, movementY);
            shape.setDirection(movement);
            //Add the shape to the model
            model.addShape(shape);
        }
    }
    //Set the target the player should match
    private void setTarget(){
        ArrayList<Shape> shapeList = model.getShapes();
        //Copy a random shape
        int index = rand.nextInt(shapeList.size());
        Shape target = shapeList.get(index).makeCopy();
        //Reset the cracks for matches to avoid unfair deaths
        for(Shape s:shapeList){
            if(isMatch(target, s)){
                s.resetCracks();
            }
        }
        //Set the copied target settings
        target.setDirection(new Vector2d(0,0));
        target.size = defaultSize;
        model.setTarget(target);
    }
    //Set the container for the game to take place in
    private void setContainer(){
        //Define a container solely by the points around its edge
        ArrayList<Point2f> containerPoints = new ArrayList<Point2f>();
        containerPoints.add(new Point2f(0,playAreaTop));
        containerPoints.add(new Point2f(view.getWidth(),playAreaTop));
        containerPoints.add(new Point2f(view.getWidth(),view.getHeight()));
        containerPoints.add(new Point2f(0,view.getHeight()));
        Container container = new Container(containerPoints);
        model.setContainer(container);
    }
    //Process any click by the user
    void processClick(Point2f p){
        // will be accessed only when the game starts
        if(!model.isGameStarted()){
            // if user pressed within the beginButton area
            if(model.getBeginButton().containsPoint(p)){
                model.setGameStarted(true);
            }

        }
        // will be accessed when a new level is reached
        else if(model.isNewLevel()){

        }
        else {
            Shape target = model.getTarget();
            boolean shapeClicked = false;
            //Iterate over all the shapes
            Iterator<Shape> iter = model.getShapes().iterator();
            while (iter.hasNext()) {
                Shape s = iter.next();
                //If this is the shape we clicked
                if (s.containsPoint(p)) {
                    //If it matches, remove it, add a new shape, and set a new target
                    if (isMatch(s, target)) {
                        iter.remove();
                        addShapes(1);
                        setTarget();
                        return;
                    } else {
                        //If it's the wrong shape, remove a life
                        shapeClicked = true;
                        model.removeLife();
                    }
                }
            }
            //If we missed all the shapes, add a bubble effect
            if (!shapeClicked) {
                Bubble bubble = new Bubble(p.getX(), p.getY(), 70, new Vector2d(0, -1), 20, Color.WHITE);
                bubble.setAlpha(.5f);
                model.addBubble(bubble);
            }
        }
    }
    //Handle a collision object between two shapes
    void handleCollision(Collision collision){
        Shape encroachingShape  = collision.getS1();
        Shape edgeShape = collision.getS2();

        Vector2d encroachingShapeMovement = encroachingShape.getDirection();
        Vector2d edgeShapeMovement = edgeShape.getDirection();
        //Draw the line between the two shapes
        Vector2d lineBetween = new Vector2d(edgeShape.getX() - encroachingShape.getX(), edgeShape.getY() - encroachingShape.getY());
        //If they're already moving away, let them so we don't get stuck
        boolean edgeMovingAway = edgeShapeMovement.angle(lineBetween) > Math.PI/2;
        boolean encroachingShapeMovingAway = encroachingShapeMovement.angle(lineBetween) > Math.PI/2;
        if( edgeMovingAway && encroachingShapeMovingAway){
            return;
        }
        //Bounce the shapes off each other along the line between them
        lineBetween.normalize();
        Vector2d newEdgeShapeMovement =lineBetween;
        lineBetween = new Vector2d(-lineBetween.getX(), -lineBetween.getY());
        Vector2d newEncroachingShapeMovement = lineBetween;

        encroachingShape.setDirection(newEncroachingShapeMovement);
        edgeShape.setDirection(newEdgeShapeMovement);
    }

    //Handle a collision between a shape and the container
    void checkWallCollision(Shape s){
        Container container = model.getContainer();
        //If the shape already escaped somehow send it straight to the center
        if (!container.containsPoint(new Point2f(s.getX(), s.getY()))) {
            Vector2d toCenter = new Vector2d(container.getCenter().getX() - s.getX(), container.getCenter().getY() - s.getY());
            toCenter.normalize();
            s.setDirection(toCenter);
            return;
        }
        for (Point2f p : s.getPoints()) {
            if (!container.containsPoint(p)) {
                //Find the vector from the point to the center
                Vector2d toCenter = new Vector2d(container.getCenter().getX() - p.getX(), container.getCenter().getY() - p.getY());
                //If the shape is already headed back to the center don't flip the direction
                if (s.getDirection().angle(toCenter) < Math.PI / 2) {
                    return;
                }
                //Find the edge the shape hit
                Vector2d edge = container.violatingEdge(p);
                //Get the normal vector to the edge
                Vector2d normal = new Vector2d(edge.getY(), -edge.getX());
                normal.normalize();
                Vector2d v = new Vector2d(s.getDirection().getX(), s.getDirection().getY());
                normal.scale(v.dot(normal) * 2);
                //The actual reflection vector
                Vector2d newMovement = new Vector2d(v.getX() - normal.getX(), v.getY() - normal.getY());
                s.setDirection(newMovement);
                //Add the shape to be exploded if it has too many cracks
                if(s.getCrackNum()+1 >= s.getPoints().size()){
                    model.addExplodedShape(s);
                }else {
                    //Add another crack
                    s.addCrack();
                }
                return;
            }
        }
    }
    //Check if a shape s has collided with any  other shapes
    void checkShapeCollision(Shape s){
        for(Shape each:model.getShapes()){
            if(!s.equals(each) && distanceBetween(s, each) < s.getSize() + each.getSize()){
                for(Point2f p:s.getPoints()) {
                    if (each.containsPoint(p)) {
                        //One of the points of shape s has crossed an edge of shape each
                        Collision collision = new Collision(s, each, p);
                        model.getCollisionList().add(collision);
                        break;
                    }
                }
            }
        }
    }
    //Check if the shape is the only one to match the target
    private boolean isOnlyMatch(Shape s){
        Shape target = model.getTarget();
        for(Shape others:model.getShapes()){
            if(others.equals(s)) {
                continue;
            }
            if(isMatch(others, target)){
                return false;
            }
        }
        return true;
    }
    //Remove exploded shapes from the normal shape play list
    void removeExplodedShapes(){
        ArrayList<Shape> explodedShapes = model.getExplodedShapes();
        ArrayList<Shape> normalShapes = model.getShapes();
        for(Shape s:explodedShapes){
            //If it's the only one we need to set a new target, and the player loses a life
            if(isOnlyMatch(s)){
                model.setLives(model.getLives()-1);
                normalShapes.remove(s);
                setTarget();
            }else {
                //Just remove it otherwise
                normalShapes.remove(s);
            }
        }
        //Wipe the exploded shape list when we're done
        model.setExplodedShapes(new ArrayList<Shape>());
        //Add as many new shapes as we need to keep at the level-determined amount
        int difference = model.getCurrentLevel().getNumShapes() - model.getShapes().size();
        if(difference != 0){
            addShapes(difference);
        }
    }

    //Check if two shapes match (have the same type and color)
    private boolean isMatch(Shape s1, Shape s2){
        return s1.getColor() == s2.getColor() && s1.getClass().equals(s2.getClass());
    }
    //Calculate the distance between two shapes
    private double distanceBetween(Shape s1, Shape s2){
        return Math.sqrt(Math.pow(s1.getX()-s2.getX(), 2) + Math.pow(s1.getY() - s2.getY(), 2));
    }
    //Calculate the distance between two points
    public double distanceBetween(Point2f p1, Point2f p2){
        return Math.sqrt(Math.pow(p1.getX()-p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }
}
