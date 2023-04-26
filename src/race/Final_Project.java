package race;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Point;

import java.awt.Color;
import java.util.ArrayList;

public class Final_Project {
    private static CanvasWindow canvas;
    private static ArrayList<Dot> listOfDots;
    public static void main(String[] args) {
        canvas = new CanvasWindow("Race", 1000, 1000);
        listOfDots = new ArrayList<>();

        Color skyBlue = new Color(135, 206, 235);
        canvas.setBackground(skyBlue);
<<<<<<< Updated upstream
        
=======

        canvas.onClick(event -> placeDots(event.getPosition()));
    }

    private static void placeDots(Point point) {
>>>>>>> Stashed changes
        
        Dot dot = new Dot(point.getX(), point.getY(), 5);

        canvas.add(dot);
        listOfDots.add(point);

    }
}
