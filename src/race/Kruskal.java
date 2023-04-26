package race;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Kruskal {

    public static void main(String[] args) {
        float[][] g = new float[][]{{1,3},{1,4},{2,5},{4,5},{2,7},{2,3},{0,2}};
        Kruskal.Kruskal(g,0,2);
        Kruskal.Kruskal(g,2,0);
    }
    private static void Kruskal(float[][] g, int start, int end){
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
        while (connectedPoints<g.length-1){
            int[] lowestEdge = getLowestRemainingEdge(edges,vertices,start,end);
            vertices[lowestEdge[0]].addEdge(lowestEdge[1]);
            vertices[lowestEdge[1]].addEdge(lowestEdge[0]); //add the edge
            connectedPoints++;

            //checks to see if any vertexes should be removed from the options
            for (int index : lowestEdge) {
                if (index==start||index==end||vertices[index].getNumOfConnections()==2){
                    vertices[index].setAsVisited();
                }
            }
            System.out.println("added edge: " + Arrays.toString(lowestEdge));
            System.out.println("length: "+ edges[lowestEdge[0]][lowestEdge[1]]);
        }
        reconstructPath(vertices,start,end);
    }

    /**
     * @return the path of vertices that Kruskal's gives
     */
    private static List<Integer> reconstructPath(AlgoVertex[] vertices, int start, int end){
        List<Integer> path = new ArrayList<>();
        path.add(start);
        int currVertex = start;
        int prevVertex = start;
        while (currVertex!=end){
            if (prevVertex==vertices[currVertex].connections.get(0)) {
                prevVertex = currVertex;
                currVertex = vertices[currVertex].connections.get(1);
            }
            else {
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
    private static boolean canBeAdded(AlgoVertex[] vertices, int[] edge, int start, int end){
        if ((edge[0]==start&&edge[1]==end)||(edge[1]==start&&edge[0]==end)) // can't go straight from start to end
            return false;
        int currVertex = start;
        int prevVertex = start;
        if (vertices[start].getNumOfConnections()==0) {
            boolean b = edge[0] == start;
            if (!b && edge[1] != start) //If start is open, we can always add an edge that doesn't connect to start
                return true;
            else {
                int index = b ? 1 : 0; // my boy dex coming in clutch with the reminder about ternary operators
                if (vertices[edge[index]].getNumOfConnections() == 0) return true; //if the part of the edge that isn't the start is open, we can go ahead and add the edge
                else {
                    currVertex = edge[index];
                    prevVertex = edge[index];
                }
            }
        }
        if (vertices[end].getNumOfConnections()==0) {
            boolean b = edge[0] == end;
            int index = b ? 1:0;
            if (!b&&edge[1]!=end)
                return true;
            else if (vertices[edge[index]].getNumOfConnections()==0)
                return true;
        }
        /*
         * follow path from start, if all vertices would be closed on the path, then this can't be added
         */
        while (true){
            if (currVertex == edge[0] && edge[1]==end) return false; // this means our path goes from start to the end, which we don't want
            else if (currVertex == edge[1] && edge[0]==end)  return false;

            if (prevVertex == vertices[currVertex].connections.get(0)){
                if (vertices[currVertex].getNumOfConnections()==1){
                    return true;
                }
                prevVertex = currVertex;
                currVertex = vertices[currVertex].connections.get(1);
            } else {
                prevVertex = currVertex;
                currVertex = vertices[currVertex].connections.get(0);
            }

            if (currVertex == end){
                return false;
            }
        }
    }

    private static int[] getLowestRemainingEdge(double[][] edges, AlgoVertex[] vertices, int start, int end){
        double lowestEdge = Double.POSITIVE_INFINITY;
        int[] coords = new int[2];
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i].isVisited()) continue;

            for (int j = i+1; j < edges[i].length; j++) {
                if (vertices[j].isVisited()) continue;

                if (edges[i][j]<lowestEdge){
                    if (!canBeAdded(vertices,new int[]{i,j},start,end)) continue;

                    lowestEdge = edges[i][j];
                    coords[0] = i;
                    coords[1] = j;
                }
            }
        }
        return coords;
    }

    private static double[][] calcEdges(float[][] g){
        double[][] weights = new double[g.length][g.length];

        for (int i = 0; i < g.length; i++) {
            for (int j = i+1; j < g.length; j++) {
                weights[i][j] = Dijkstra.getDistance(g[i],g[j]);
                weights[j][i] = weights[i][j];
            }
        }

        return weights;
    }
}
