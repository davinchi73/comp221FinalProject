package race;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.macalester.graphics.Point;

public class Kruskal {

    public static void main(String[] args) {
        //float[][] g = new float[][]{{1,3},{1,4},{2,5},{4,5},{2,7},{2,3},{0,2}};
        double[][] g = new double[][]{{482.0, 327.0}, {599.0, 439.0}, {320.0, 444.0}, {187.0, 356.0}, {235.0, 229.0}, {466.0, 140.0}
                , {727.0, 272.0}, {731.0, 575.0}};
        List<Point> points = new ArrayList<>();
        for (double[] index : g) {
            points.add(new Point(index[0], index[1]));
        }
        Kruskal.getKruskalPath(points, 0, g.length - 1);
        //Kruskal.Kruskal(g,0,2);
        //Kruskal.Kruskal(g,2,0);
    }

    static List<Point> getKruskalPath(List<Point> points, int start, int end) {
        double[][] g = new double[points.size()][2];
        for (int i = 0; i < g.length; i++) {
            g[i] = new double[]{points.get(i).getX(), points.get(i).getY()};
        }

        List<Integer> path = Kruskal(g, start, end);
        List<Point> pointPath = new ArrayList<>();
        for (Integer integer : path) {
            pointPath.add(points.get(integer));
        }
        return pointPath;
    }

    private static List<Integer> Kruskal(double[][] g, int start, int end) {
        /*
            Add lowest val edge
            Add next lowest val edge that adds a point
            If a point (other than start and end) has two edges connecting it,
            it no longer should be considered
            Start and end points are removed after they get one edge
        */
        double[][] edges = calcEdges(g);
        AlgoVertex[] vertices = new AlgoVertex[g.length]; // will use this to do path reconstruction
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = new AlgoVertex(i);
        }

        int connectedPoints = 0;
        while (connectedPoints < g.length - 1) {
            int[] lowestEdge = getLowestRemainingEdge(edges, vertices, start, end);
            vertices[lowestEdge[0]].addEdge(lowestEdge[1]);
            vertices[lowestEdge[1]].addEdge(lowestEdge[0]); //add the edge
            connectedPoints++;

            //checks to see if any vertexes should be removed from the options
            for (int index : lowestEdge) {
                if (index == start || index == end || vertices[index].getNumOfConnections() == 2) {
                    vertices[index].setAsVisited();
                }
            }
            System.out.println("added edge: " + Arrays.toString(lowestEdge));
            System.out.println("length: " + edges[lowestEdge[0]][lowestEdge[1]]);
        }
        return reconstructPath(vertices, start, end);
    }

    /**
     * @return the path of vertices that Kruskal's gives
     */
    private static List<Integer> reconstructPath(AlgoVertex[] vertices, int start, int end) {
        List<Integer> path = new ArrayList<>();
        path.add(start);
        int currVertex = start;
        int prevVertex = start;
        while (currVertex != end) {
            if (prevVertex == vertices[currVertex].connections.get(0)) {
                prevVertex = currVertex;
                currVertex = vertices[currVertex].connections.get(1);
            } else {
                prevVertex = currVertex;
                currVertex = vertices[currVertex].connections.get(0);
            }
            path.add(currVertex);
        }
        System.out.println(path);
        return path;
    }

    /**
     * I apologize for this method, it's so annoying, but IDK if I can simplify it much better
     * Although, I will look into ways to avoid some code duplication
     */
    private static boolean canBeAdded(AlgoVertex[] vertices, int[] edge, int start, int end) {
        if ((edge[0] == start && edge[1] == end) || (edge[1] == start && edge[0] == end)) // can't go straight from start to end
            return false;
        if (vertices[edge[0]].connections.contains(edge[1])) { // don't add edges twice
            return false;
        }
        int currVertex = start;
        int prevVertex = start;
        int edgeCount = 0;
        if (vertices[start].getNumOfConnections() == 0) {
            boolean b = edge[0] == start;
            if (!b && edge[1] != start) {//If start is open, we can always add an edge that doesn't connect to start
                return !hasCycle(vertices, edge);
            } else {
                int index = b ? 1 : 0; // my boy dex coming in clutch with the reminder about ternary operators
                if (vertices[edge[index]].getNumOfConnections() == 0)
                    return true; //if the part of the edge that isn't the start is open, we can go ahead and add the edge
                else {
                    currVertex = edge[index];
                    prevVertex = edge[index];
                    edgeCount++;
                }
            }
        }
        if (vertices[end].getNumOfConnections() == 0) {
            boolean b = edge[0] == end;
            int index = b ? 1 : 0;
            if (!b && edge[1] != end) {
                return !hasCycle(vertices, edge);
                //return true;
            } else if (vertices[edge[index]].getNumOfConnections() == 0)
                return true;
        }

        if (hasCycle(vertices,edge)){
            return false;
        }
        /*
         * follow path from start, if all vertices would be closed on the path, then this can't be added
         */

        while (edgeCount < vertices.length - 1) {
            if (edgeCount < vertices.length - 2) {
                if (currVertex == edge[0] && edge[1] == end) {
                    return false; // this means our path goes from start to the end, which we don't want
                } else if (currVertex == edge[1] && edge[0] == end) {
                    return false;
                }
            }
            if (vertices[currVertex].getNumOfConnections()==0)
                return true;
            if (prevVertex == vertices[currVertex].connections.get(0)) {
                if (vertices[currVertex].getNumOfConnections() == 1) {
                    boolean b = currVertex==edge[0];
                    int index = b ? 1:0;
                    if (b||currVertex==edge[1]){ //probably a way to make this better
                        prevVertex = currVertex;
                        currVertex = edge[index];
                        edgeCount++;
                        continue;
                    } else return true;
                }
                prevVertex = currVertex;
                currVertex = vertices[currVertex].connections.get(1);
            } else {
                prevVertex = currVertex;
                currVertex = vertices[currVertex].connections.get(0);
            }
            edgeCount++;
            if (currVertex == end && edgeCount<vertices.length-1) {
                //System.out.println("hey there buckaroo: "+Arrays.toString(edge)+" edge count: "+edgeCount);
                return false;
            }

        }
        return true;
    }

    private static boolean hasCycle(AlgoVertex[] vertices, int[] edge) {
        int currVertex;
        if (vertices[edge[0]].getNumOfConnections() == 0 || vertices[edge[1]].getNumOfConnections() == 0) {
            return false;
        } else {
            int b = vertices[edge[0]].getNumOfConnections() > 0 ? 0 : 1;
            currVertex = edge[b];
        }
        int prevVertex = currVertex;
        while (true) {
            if (vertices[currVertex].connections.get(0) == prevVertex) {
                prevVertex = currVertex;
                currVertex = vertices[currVertex].connections.get(1);
            } else {
                prevVertex = currVertex;
                currVertex = vertices[currVertex].connections.get(0);
            }
            if (currVertex == edge[1] || currVertex == edge[0]) // we have reached our edge again, there is a cycle
                return true;
            if (vertices[currVertex].getNumOfConnections() == 1) // not our edge and only one connection
                return false;
        }
        //return false;
    }

    private static int[] getLowestRemainingEdge(double[][] edges, AlgoVertex[] vertices, int start, int end) {
        double lowestEdge = Double.POSITIVE_INFINITY;
        int[] coords = new int[2];
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i].isVisited()) continue;

            for (int j = i + 1; j < edges[i].length; j++) {
                if (vertices[j].isVisited()) continue;

                if (edges[i][j] < lowestEdge) {
                    if (!canBeAdded(vertices, new int[]{i, j}, start, end)) continue;

                    lowestEdge = edges[i][j];
                    coords[0] = i;
                    coords[1] = j;
                }
            }
        }
        return coords;
    }

    private static double[][] calcEdges(double[][] g) {
        double[][] weights = new double[g.length][g.length];

        for (int i = 0; i < g.length; i++) {
            for (int j = i + 1; j < g.length; j++) {
                weights[i][j] = getDistance(g[i], g[j]);
                weights[j][i] = weights[i][j];
            }
        }

        return weights;
    }

    static double getDistance(double[] a, double[] b) {
        return Math.sqrt(Math.pow(a[0] - b[0], 2) + Math.pow(a[1] - b[1], 2));
    }
}