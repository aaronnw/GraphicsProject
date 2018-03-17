import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
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
    int playAreaTop = 100;

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

        addShapes();

        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    public void addShapes(){
        //Can change these later for harder levels
        int numShapes = 10;
        int maxShapeSize = 100;
        int minShapeSize = 20;
        int maxVel = 300;
        int minVel = 50;

        Random rand = new Random();
        Color[] colors = Color.values();

        for (int i = 0; i < 10 ; i++) {
            int size = minShapeSize + rand.nextInt(maxShapeSize-minShapeSize);
            int x = rand.nextInt(view.getWidth()-size) + size/2;
            int y = playAreaTop + rand.nextInt(view.getHeight()-size-playAreaTop) + size/2;
            int colorVal = rand.nextInt(colors.length);
            Shape shape;
            int shapeType = rand.nextInt(2);
            if(shapeType == 1) {
                shape = new Square(x, y, size);
            }else{
                shape = new Circle(x, y, size);
            }
            shape.setColor(colors[colorVal]);

            int movementX = minVel + rand.nextInt(maxVel-minVel);
            int movementY = minVel + rand.nextInt(maxVel-minVel);
            Vector2d movement = new Vector2d(movementX, movementY);
            shape.setMovement(movement);

            model.addShape(shape);
        }
    }
    public void processClick(Point p){
        Iterator<Shape> iter = model.getShapes().iterator();

        while (iter.hasNext()) {
            Shape s = iter.next();
            if (s.containsPoint(p))
                iter.remove();
        }
    }
}
