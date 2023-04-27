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
    private static ArrayList<Point> listOfDots;
    private static Button calcPath;
    private static List<Point> orderedPoints;


    public static void main(String[] args) {
        canvas = new CanvasWindow("Race", 1000, 1000);
        listOfDots = new ArrayList<>();


        Color skyBlue = new Color(135, 206, 235);
        canvas.setBackground(skyBlue);

        calcPath = new Button("Start");
        calcPath.setCenter(500, 700);
        canvas.onClick(event -> placeDots(event.getPosition()));
        calcPath.onClick(() -> startButton(listOfDots));
    }

    private static void placeDots(Point point) {
        if (listOfDots.size() < 15) {
            Dot dot = new Dot(point.getX(), point.getY(), 10);

            canvas.add(dot);
            listOfDots.add(point);
    
            if (listOfDots.size() == 8) {
                canvas.add(calcPath);
            }
            System.out.println(listOfDots);
        }


    }

    private static void startButton(ArrayList<Point> pointList) {

        orderedPoints = Kruskal.getKruskalPath(pointList, 0,pointList.size()-1);
        Line line;
        for (int i = 0; i < orderedPoints.size() - 2; i++) {
            line = new Line(orderedPoints.get(i), orderedPoints.get(i+1));
            line.setStrokeWidth(5);
            line.setStrokeColor(Color.BLACK);
            canvas.add(line);
        }

    }
}
