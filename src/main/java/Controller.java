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
            shape.setMovement(movement);

            model.addShape(shape);
        }
    }
    public void setTarget(){
        ArrayList<Shape> shapeList = model.getShapes();
        int index = rand.nextInt(shapeList.size());
        Shape target = shapeList.get(index).makeCopy();
        target.setMovement(new Vector2d(0,0));
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
        Iterator<Shape> iter = model.getShapes().iterator();
        Shape target = model.getTarget();
        Color targetColor = target.getColor();
        while (iter.hasNext()) {
            Shape s = iter.next();
            //If this is the shape we clicked
            if (s.containsPoint(p)) {
                Color clickedColor = s.getColor();
                System.out.println("Target color:" + targetColor);
                System.out.println("color clicked: " + clickedColor);
                if(targetColor == clickedColor && target.getClass().equals(s.getClass())){
                    iter.remove();
                    addShapes(1);
                    setTarget();
                    return;
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
