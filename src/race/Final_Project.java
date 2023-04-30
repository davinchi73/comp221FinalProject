package race;

// import statements
import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Line;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.ui.Button;
import edu.macalester.graphics.GraphicsGroup;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

// class declaration
public class Final_Project {
    
    // declare global variables
    private static CanvasWindow canvas;
    private static Dot dot;
    // private static Dot runner;
    private static Button startButton;
    private static Button restartButton;
    private static Button calcPath;
    private static Button calcPathNN;
    private static GraphicsText timeCalc = new GraphicsText("Time to run at 5MPH: ");
    private static List<Point> orderedPoints;
    private static List<Point> orderedPointsNN;
    private static boolean startClicked = false;
    private static double timeKruskal;

    private static GraphicsGroup kruskalGraphicsGroup;
    private static GraphicsGroup NNGraphicsGroup;

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

        // Initializing and positioning buttons / texts
        startButton = new Button("Start");
        startButton.setCenter(500, 100);
        restartButton = new Button("Restart");
        restartButton.setCenter(500, 100);
        calcPath = new Button("Display Path: Kruskal");
        calcPath.setCenter(700, 100);
        calcPathNN = new Button("Display Path: Nearest Neighbors");
        calcPathNN.setCenter(700, 175);
        timeCalc.setCenter(100, 100);
        timeKruskal = 0.0;

        // Initializing graphicsgroups
        kruskalGraphicsGroup = new GraphicsGroup();
        NNGraphicsGroup = new GraphicsGroup();

        // Lambda statements
        canvas.onClick(event -> placeDots(event.getPosition(), listOfDots));
        startButton.onClick(() -> startButton());
        restartButton.onClick(() -> restartButton());
        calcPath.onClick(() -> calcPath(listOfDots, true));
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
    private static void startButton() {
        startClicked = true;
        canvas.remove(startButton);
        canvas.add(restartButton);
        canvas.add(calcPath);
        canvas.add(calcPathNN);
        canvas.add(timeCalc);
    }

    /*
     * Method to handle onClick lambda for restart button
     */
    private static void restartButton() {
        startClicked = false;
        canvas.removeAll();
        kruskalGraphicsGroup.removeAll();
        NNGraphicsGroup.removeAll();
        runner();
    }

    /*
     * Method to handle onClick for lambda for calcPath
     */
    private static void calcPath(ArrayList<Point> pointList, boolean kruskalYes) {

        orderedPoints = Kruskal.getKruskalPath(pointList, 0, pointList.size()-1);
        // orderedPointsNN = NearestNeighbour.getNearestNeighbourPath(pointList, pointList.get(0), pointList.get(pointList.size() - 1));

        Line line;

        for (int i = 0; i < orderedPoints.size() - 1; i++) {

            line = new Line(orderedPoints.get(i).getX() + 5, orderedPoints.get(i).getY() + 5, 
            orderedPoints.get(i+1).getX() + 5, orderedPoints.get(i+1).getY() + 5);

            line.setStrokeWidth(5);
            line.setStrokeColor(Color.gray);
            kruskalGraphicsGroup.add(line);

            timeKruskal += Math.hypot((orderedPoints.get(i).getX() + 5) - (orderedPoints.get(i+1).getX() + 5),
            (orderedPoints.get(i).getY() + 5) - (orderedPoints.get(i+1).getY() + 5)) * 5;
        }

        if (kruskalYes == true) {
            timeCalc.setText(timeCalc.getText() + Double.toString(timeKruskal) + " seconds");
            canvas.add(timeCalc);

            canvas.add(kruskalGraphicsGroup);
        } else {
            timeCalc.setText(timeCalc.getText() + Double.toString(timeKruskal) + " seconds");
            canvas.add(timeCalc);

            canvas.add(kruskalGraphicsGroup);
        }
    }
}
