package race;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Line;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.ui.Button;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

public class Final_Project {

    private static CanvasWindow canvas;
    private static Dot dot;
    private static Button calcPath;
    private static Button restartButton;
    private static List<Point> orderedPoints;


    public static void main(String[] args) {   

        // Our canvas object and setting canvas color
        canvas = new CanvasWindow("Race", 1000, 1000);
        Color skyBlue = new Color(135, 206, 235);
        canvas.setBackground(skyBlue);

        runner();
    }

    private static void runner() {

        // Initialize global arrayList of dots
        ArrayList<Point> listOfDots = new ArrayList<>();

        // Initializing and positioning buttons
        calcPath = new Button("Start");
        calcPath.setCenter(500, 700);
        restartButton = new Button("Restart");
        restartButton.setCenter(500, 700);

        // Lambda statements
        canvas.onClick(event -> placeDots(event.getPosition(), listOfDots));
        calcPath.onClick(() -> startButton(listOfDots));
        restartButton.onClick(() -> restartButton());
    }

    private static void placeDots(Point point, ArrayList<Point> dotList) {

        if (dotList.size() < 15) {
            dot = new Dot(point.getX(), point.getY(), 10);

            canvas.add(dot);
            dotList.add(point);
    
            if (dotList.size() == 8) {
                canvas.add(calcPath);
            }
            System.out.println(dotList);
        }
    }


    private static void startButton(ArrayList<Point> pointList) {
        canvas.remove(calcPath);
        canvas.add(restartButton);

        orderedPoints = Kruskal.getKruskalPath(pointList, 0,pointList.size()-1);
        Line line;

        for (int i = 0; i < orderedPoints.size() - 2; i++) {
            line = new Line(orderedPoints.get(i), orderedPoints.get(i+1));
            line.setStrokeWidth(5);
            line.setStrokeColor(Color.BLACK);
            canvas.add(line);
        }

    }


    private static void restartButton() {
        canvas.removeAll();
        // listOfDots.clear();
        runner();
    }
}
