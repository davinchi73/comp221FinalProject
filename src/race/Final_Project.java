package race;

// import statements
import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Line;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.ui.Button;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

// class declaration
public class Final_Project {
    
    // declare global variables
    private static CanvasWindow canvas;
    private static Dot dot;
    private static Button startButton;
    private static Button restartButton;
    private static Button calcPath;
    private static List<Point> orderedPoints;
    private static boolean startClicked = false;

    // main
    public static void main(String[] args) {   

        // Our canvas object and setting canvas color
        canvas = new CanvasWindow("Race", 1000, 1000);
        Color skyBlue = new Color(135, 206, 235);
        canvas.setBackground(skyBlue);

        // call to runner
        runner();
    }

    /*
     * Main method to run our project
     */
    private static void runner() {

        // Initialize global arrayList of dots
        ArrayList<Point> listOfDots = new ArrayList<>();
        System.out.println(listOfDots);

        // Initializing and positioning buttons
        startButton = new Button("Start");
        startButton.setCenter(500, 700);
        restartButton = new Button("Restart");
        restartButton.setCenter(500, 700);
        calcPath = new Button("Display Path: Kruskal");
        calcPath.setCenter();

        // Lambda statements
        canvas.onClick(event -> placeDots(event.getPosition(), listOfDots));
        startButton.onClick(() -> startButton(listOfDots));
        restartButton.onClick(() -> restartButton());
    }

    /*
     * Method to handle dot placement and conditions which 
     * would prevent dot placement
     */
    private static void placeDots(Point point, ArrayList<Point> dotList) {

        if (dotList.size() < 15 && startClicked == false) {
            dot = new Dot(point.getX(), point.getY(), 10);

            canvas.add(dot);
            dotList.add(point);
    
            if (dotList.size() == 8) {
                canvas.add(startButton);
            }
            System.out.println(dotList);
        }
    }

    /*
     * Method to handle onClick lambda for start button
     */
    private static void startButton(ArrayList<Point> pointList) {
        startClicked = true;
        canvas.remove(startButton);
        canvas.add(restartButton);

        orderedPoints = Kruskal.getKruskalPath(pointList, 0, pointList.size()-1);
        Line line;

        for (int i = 0; i < orderedPoints.size() - 1; i++) {
            line = new Line(orderedPoints.get(i), orderedPoints.get(i+1));
            line.setStrokeWidth(5);
            line.setStrokeColor(Color.BLACK);
            canvas.add(line);
        }

    }


    private static void restartButton() {
        startClicked = false;
        canvas.removeAll();
        runner();
    }
}
