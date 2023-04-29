package race;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
// import edu.macalester.graphics.Point;

public class NearestNeighbour {


    public static void main(String[] args) {
        
        List<Point> points = Arrays.asList(new Point(1, 0), new Point(2, 1), new Point(3, 2), new Point(4, 2), new Point(5, 2) );

        Point source = points.get(0);
        Point target = points.get(points.size() - 1);
        
        System.out.println(getNearestNeighbourPath(points, source, target));
    }


    /*
     * Given a list of points, a source, and a target, return the path in a list 
     * that visits all points using the nearest neighbour algorithm in which a 
     * greedy choice is made at each step until all points are visited.
     * 
     * The path starts at the source and ends at the target.
     */
    public static List<Point> getNearestNeighbourPath(List<Point> points, Point source, Point target) {
        List<Point> path = new ArrayList<>();

        Point currentPoint = source;

        points.remove(source);
        points.remove(target);
    
        path.add(currentPoint);

        while (points.size() > 0) {
            Point nearestNeighbour = getNearestNeighbour(points, currentPoint);
            path.add(nearestNeighbour);
            points.remove(nearestNeighbour);
            currentPoint = nearestNeighbour;
        }

        path.add(target);
        
        return path;
    }

    /*
     * Given a list of points and a current point, return the nearest neighbour
     */
    private static Point getNearestNeighbour(List<Point> points, Point currentPoint) {
        Point nearestNeighbour = null;
        
        for (Point point : points) {
            if (point != currentPoint) {
                if (nearestNeighbour == null) {
                    nearestNeighbour = point;
                } else {
                    if (currentPoint.distance(point) < currentPoint.distance(nearestNeighbour)) {
                        nearestNeighbour = point;
                    }
                }
            }
        }
        return nearestNeighbour;
    }
    


}