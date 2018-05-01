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
 * Created by Aaron on 3/9/2018.
 */
public class Controller {
    Model model;
    View view;
    int playAreaTop = 150;
    int defaultSize = 100;
    Random rand = new Random();

    public Controller(Model m, View v){
        model = m;
        view = v;
    }
    //Start the project
    public void init(int width, int height){
        GLProfile profile = GLProfile.getDefault();
        GLCapabilities capabilities = new GLCapabilities(profile);
        GLCanvas canvas = new GLCanvas(capabilities);
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
        canvas.addGLEventListener(view);
        canvas.addMouseListener(view);

        Level current = new Level(3, 10, 10, 150, 100, 100, 50);
        model.setCurrentLevel(current);
        model.setLives(current.getLives());
        addShapes(current.getNumShapes());
        setTarget();
        setContainer();

        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    public void addShapes(int numShapes){
        Level current = model.getCurrentLevel();
        int minShapeSize = current.getMinShapeSize();
        int maxShapeSize = current.getMaxShapeSize();
        int minVel = current.getMinVel();
        int maxVel = current.getMaxVel();

        Random rand = new Random();
        Color[] colors = Color.values();
        for (int i = 0; i < numShapes ; i++) {
            int size = minShapeSize + rand.nextInt(maxShapeSize-minShapeSize);
            int x = rand.nextInt(view.getWidth()-size) + size/2;
            int y = playAreaTop + rand.nextInt(view.getHeight()-size-playAreaTop) + size/2;
            int colorVal = rand.nextInt(colors.length);
            Shape shape;
            //int shapeType = 1;
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

            shape.setColor(colors[colorVal]);
            shape.setSpeed(rand.nextInt(maxVel-minVel)+minVel);

            double movementX = rand.nextDouble();
            double movementY = 1-movementX;
            Vector2d movement = new Vector2d(movementX, movementY);
            shape.setDirection(movement);

            model.addShape(shape);
        }
    }
    public void setTarget(){
        ArrayList<Shape> shapeList = model.getShapes();
        int index = rand.nextInt(shapeList.size());
        Shape target = shapeList.get(index).makeCopy();
        target.setDirection(new Vector2d(0,0));
        target.size = defaultSize;
        model.setTarget(target);
    }
    public void setContainer(){
        ArrayList<Point2f> containerPoints = new ArrayList<Point2f>();
        containerPoints.add(new Point2f(0,playAreaTop));
        containerPoints.add(new Point2f(view.getWidth(),playAreaTop));
        containerPoints.add(new Point2f(view.getWidth(),view.getHeight()));
        containerPoints.add(new Point2f(0,view.getHeight()
        ));
        Container container = new Container(containerPoints);
        model.setContainer(container);
    }
    public void processClick(Point2f p){
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
            Iterator<Shape> iter = model.getShapes().iterator();
            Shape target = model.getTarget();
            Color targetColor = target.getColor();
            boolean shapeClicked = false;
            while (iter.hasNext()) {
                Shape s = iter.next();
                //If this is the shape we clicked
                if (s.containsPoint(p)) {
                    Color clickedColor = s.getColor();
                    System.out.println("Target color:" + targetColor);
                    System.out.println("color clicked: " + clickedColor);
                    if (targetColor == clickedColor && target.getClass().equals(s.getClass())) {
                        iter.remove();
                        addShapes(1);
                        setTarget();
                        return;
                    } else {
                        shapeClicked = true;
                        model.removeLife();
                    }
                }
            }
            if (!shapeClicked) {
                Bubble bubble = new Bubble(p.getX(), p.getY(), 70, new Vector2d(0, -1), 20, Color.WHITE);
                bubble.setAlpha(.5f);
                model.addBubble(bubble);
            }
        }
    }

    public void handleCollision(Collision collision){
        Shape encroachingShape  = collision.getS1();
        Shape edgeShape = collision.getS2();

        Vector2d encroachingShapeMovement = encroachingShape.getDirection();
        Vector2d edgeShapeMovement = edgeShape.getDirection();
        Vector2d lineBetween = new Vector2d(edgeShape.getX() - encroachingShape.getX(), edgeShape.getY() - encroachingShape.getY());
        boolean edgeMovingAway = edgeShapeMovement.angle(lineBetween) > Math.PI/2;
        boolean encroachingShapeMovingAway = encroachingShapeMovement.angle(lineBetween) > Math.PI/2;
        if( edgeMovingAway && encroachingShapeMovingAway){
            return;
        }

        lineBetween.normalize();
        Vector2d newEdgeShapeMovement =lineBetween;
        lineBetween = new Vector2d(-lineBetween.getX(), -lineBetween.getY());
        Vector2d newEncroachingShapeMovement = lineBetween;

        encroachingShape.setDirection(newEncroachingShapeMovement);
        edgeShape.setDirection(newEdgeShapeMovement);
    }


    public void checkWallCollision(Shape s){
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
                s.addCrack();
                return;
            }
        }
    }


    public void checkShapeCollision(Shape s){
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

    public double distanceBetween(Shape s1, Shape s2){
        return Math.sqrt(Math.pow(s1.getX()-s2.getX(), 2) + Math.pow(s1.getY() - s2.getY(), 2));
    }
    public double distanceBetween(Point2f p1, Point2f p2){
        return Math.sqrt(Math.pow(p1.getX()-p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }
}
