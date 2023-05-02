package race;

// import statements
import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Line;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.ui.Button;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsObject;

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
    private static GraphicsText timeCalc = new GraphicsText("Time to run at 5PPS: ");
    private static GraphicsText startText = new GraphicsText("Click on the screen to add dots (range = 8 - 15 dots)");
    private static GraphicsText start = new GraphicsText("Start");
    private static GraphicsText end = new GraphicsText("End");
    private static ArrayList<Point> orderedPoints;
    private static ArrayList<Point> orderedPointsNN;
    private static List<Point> orderedPointsAnimation;
    private static List<Point> orderedPointsAnimationNN;
    private static boolean startClicked = false;
    private static boolean kruskalAdded = false;
    private static boolean NNAdded = false;
    private static boolean emojiOnScreen = false;
    private static double timeKruskal;
    private static double timeNN;
    private static GraphicsGroup emoji;

    private static GraphicsGroup kruskalGraphicsGroup;
    private static GraphicsGroup NNGraphicsGroup;

    // main
    public static void main(String[] args) {   

        // Our canvas object and setting canvas color
        canvas = new CanvasWindow("Race", 1000, 1000);
        Color skyBlue = new Color(135, 206, 235);
        canvas.setBackground(skyBlue);
        timeCalc.setCenter(100, 100);
        startText.setCenter(500, 200);
        emoji = FunnyFace.createSmileyFace(25);

        // call to runner
        runner();
    }

    /*
     * Main method to run our project
     */
    private static void runner() {

        // Initialize global arrayList of dots
        ArrayList<Point> listOfDots = new ArrayList<>();
        // System.out.println(listOfDots);

        // Initializing and positioning buttons / texts
        startButton = new Button("Start");
        startButton.setCenter(500, 100);
        restartButton = new Button("Restart");
        restartButton.setCenter(500, 100);
        calcPath = new Button("Display Path: (Modified) Kruskal");
        calcPath.setCenter(700, 100);
        calcPathNN = new Button("Display Path: Nearest Neighbors");
        calcPathNN.setCenter(700, 175);
        timeCalc.setText("Time to run at 5PPS: ");
        canvas.add(startText);

        timeKruskal = 0.0;
        timeNN = 0.0;

        // Initializing ordered points
        orderedPoints = new ArrayList<>();
        orderedPointsNN = new ArrayList<>();
        orderedPointsAnimation = new ArrayList<>();
        orderedPointsAnimationNN = new ArrayList<>();

        // Initializing graphicsgroups
        kruskalGraphicsGroup = new GraphicsGroup();
        NNGraphicsGroup = new GraphicsGroup();

        // Lambda statements
        canvas.onClick(event -> placeDots(event.getPosition(), listOfDots));
        startButton.onClick(() -> startButton(listOfDots, orderedPoints, orderedPointsNN));
        restartButton.onClick(() -> restartButton());
        calcPath.onClick(() -> calcPath(listOfDots, true));
        calcPathNN.onClick(() -> calcPath(listOfDots, false));
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
        }
    }

    /*
     * Method to handle onClick lambda for start button
     */
    private static void startButton(ArrayList<Point> pointList, List<Point> orderedPoints, List<Point> orderedPointsNN) {
        startClicked = true;
        canvas.remove(startButton);
        canvas.remove(startText);
        canvas.add(restartButton);
        canvas.add(calcPath);
        canvas.add(calcPathNN);
        canvas.add(timeCalc);
        orderedPoints = Kruskal.getKruskalPath(pointList, 0, pointList.size()-1);
        orderedPointsNN = NearestNeighbour.getNearestNeighbourPath(pointList, pointList.get(0), pointList.get(pointList.size() - 1));
        orderedPointsAnimation = orderedPoints;
        orderedPointsAnimationNN = orderedPointsNN;

        Line line;
        Line lineNN;

        for (int i = 0; i < orderedPoints.size() - 1; i++) {

            if (i == 0) {
                start.setCenter(orderedPoints.get(i).getX() + 5, orderedPoints.get(i).getY() - 10);
                canvas.add(start);
            }

            if (i == orderedPoints.size() - 2) {
                end.setCenter(orderedPoints.get(i+1).getX() + 5, orderedPoints.get(i+1).getY() - 10);
                canvas.add(end);
            }

            line = new Line(orderedPoints.get(i).getX() + 5, orderedPoints.get(i).getY() + 5, 
            orderedPoints.get(i+1).getX() + 5, orderedPoints.get(i+1).getY() + 5);

            lineNN = new Line(orderedPointsNN.get(i).getX() + 5, orderedPointsNN.get(i).getY() + 5, 
            orderedPointsNN.get(i+1).getX() + 5, orderedPointsNN.get(i+1).getY() + 5);


            line.setStrokeWidth(5);
            line.setStrokeColor(Color.blue);
            kruskalGraphicsGroup.add(line);

            lineNN.setStrokeWidth(5);
            lineNN.setStrokeColor(Color.red);
            NNGraphicsGroup.add(lineNN);


            timeKruskal += Math.hypot((orderedPoints.get(i).getX() + 5) - (orderedPoints.get(i+1).getX() + 5),
            (orderedPoints.get(i).getY() + 5) - (orderedPoints.get(i+1).getY() + 5)) * 5;

            timeNN += Math.hypot((orderedPointsNN.get(i).getX() + 5) - (orderedPointsNN.get(i+1).getX() + 5),
            (orderedPointsNN.get(i).getY() + 5) - (orderedPointsNN.get(i+1).getY() + 5)) * 5;

        }
        
    }

    /*
     * Method to handle onClick lambda for restart button
     */
    private static void restartButton() {
        startClicked = false;
        kruskalAdded = false;
        NNAdded = false;
        emojiOnScreen = false;
        canvas.removeAll();
        kruskalGraphicsGroup.removeAll();
        NNGraphicsGroup.removeAll();
        runner();
    }

    /*
     * Method to handle onClick for lambda for calcPath
     */
    private static void calcPath(ArrayList<Point> pointList, boolean kruskalYes) {

        if (kruskalAdded == true) {
            canvas.remove(kruskalGraphicsGroup);
            kruskalAdded = false;
        } 
        
        else if (NNAdded == true) {
            canvas.remove(NNGraphicsGroup);
            NNAdded = false;
        }
        
        timeCalc.setText("Time to run at 5PPS: ");

        if (kruskalYes == true) {

            timeCalc.setText(timeCalc.getText() + Double.toString(timeKruskal) + " seconds");
            canvas.add(timeCalc);
            kruskalAdded = true;
            canvas.add(kruskalGraphicsGroup);
            runOnThePath(emoji, orderedPointsAnimation);


        } else {

            timeCalc.setText(timeCalc.getText() + Double.toString(timeNN) + " seconds");
            canvas.add(timeCalc);
            NNAdded = true;
            canvas.add(NNGraphicsGroup);
            runOnThePath(emoji, orderedPointsAnimationNN);
        }

        
    }


    /**
     * This method will add a little face that will follow along the path shown on the canvas
     */
    private static void runOnThePath(GraphicsGroup face, List<Point> pointPath) {
        //make sure the emoji is not already on screen
        if (emojiOnScreen) {
            emojiOnScreen = false;
            canvas.remove(face);
        }

        //place the emoji on the screen at the starting point
        face.setCenter(pointPath.get(0));
        canvas.add(face);
        emojiOnScreen = true;

        //for every point in the path
        for (int i = 1; i < pointPath.size(); i++) {
            //grab the distance the face is from the x and y of the next point
            double xDistance = face.getX() - pointPath.get(i).getX();
            double yDistance = face.getY() - pointPath.get(i).getY();

            //these are multipliers so the emoji travels the x and y plane at the same speed
            //based on ratios
            double xMultiplier = 1;
            double yMultiplier = 1;

            //these will give us our ratios to multiply the multipliers
            if (Math.abs(xDistance) > Math.abs(yDistance)) {

                yMultiplier = Math.abs(yDistance) / Math.abs(xDistance);

            } else if (Math.abs(yDistance) > Math.abs(xDistance)) {

                xMultiplier = Math.abs(xDistance) / Math.abs(yDistance);

            } else {
                continue;
            }

            //this will tell us which direction we need to go
            if (xDistance > 0 ) {
                xMultiplier = xMultiplier * -1;
            }
            if (yDistance > 0) {
                yMultiplier = yMultiplier * -1;
            }

            //the the face is not around the previous point
            while (face.getCenter() != pointPath.get(i)) {
                //animate the face towards the next point
                face.setX(face.getX() + xMultiplier);
                face.setY(face.getY() + yMultiplier);
                canvas.draw();

                //the if statements will determine if the face is near the points
                if (xDistance > 0 && yDistance > 0) {
                    if (face.getX() <= pointPath.get(i).getX() || face.getY() <= pointPath.get(i).getY()) {
                        break;
                    }
                }
                if (xDistance < 0 && yDistance > 0) {
                    if (face.getX() >= pointPath.get(i).getX() || face.getY() <= pointPath.get(i).getY()) {
                        break;
                    }
                }
                if (xDistance > 0 && yDistance < 0) {
                    if (face.getX() <= pointPath.get(i).getX() || face.getY() >= pointPath.get(i).getY()) {
                        break;
                    }
                }
                if (xDistance < 0 && yDistance < 0) {
                    if (face.getX() >= pointPath.get(i).getX() || face.getY() >= pointPath.get(i).getY()) {
                        break;
                    }
                }
            }

        }

        
    }
}
