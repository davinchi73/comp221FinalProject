package race;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Kruskal {

    public static void main(String[] args) {
        float[][] g = new float[][]{{1,3},{1,4},{2,5},{4,5},{2,7}};
        Kruskal.Kruskal(g,0,2);
    }
    private static void Kruskal(float[][] g, int start, int end){
        /**
         * Add lowest val edge
         * Add next lowest val edge that adds a point
         * If a point (other than start and end) has two edges connecting it,
         * it no longer should be considered
         * Start and end points are removed after they get one edge
         */
        double[][] edges = calcEdges(g);
        boolean[] remainingPoints = new boolean[g.length];
        Arrays.fill(remainingPoints,true);
        Vertex[] vertices = new Vertex[g.length]; // will use this to do path reconstruction
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = new Vertex(i);
        }

        int connectedPoints = 0;
        while (connectedPoints<g.length-1){
            int[] lowestEdge = getLowestRemainingEdge(edges,remainingPoints,vertices,start,end);
            vertices[lowestEdge[0]].addEdge(lowestEdge[1]);
            vertices[lowestEdge[1]].addEdge(lowestEdge[0]); //add the edge
            connectedPoints++;
            if (lowestEdge[0]==start||lowestEdge[0]==end){
                remainingPoints[lowestEdge[0]]=false;
            } else if (vertices[lowestEdge[0]].getNumOfConnections()==2){
                remainingPoints[lowestEdge[0]]=false;
            }
            if (lowestEdge[1]==start||lowestEdge[1]==end){
                remainingPoints[lowestEdge[1]]=false;
            } else if (vertices[lowestEdge[1]].getNumOfConnections()==2){
                remainingPoints[lowestEdge[1]]=false;
            }
            System.out.println("added edge: " + Arrays.toString(lowestEdge));
            System.out.println("length: "+ edges[lowestEdge[0]][lowestEdge[1]]);
        }
        reconstructPath(vertices,start,end);
    }

    private static List<Integer> reconstructPath(Vertex[] vertices, int start, int end){
        System.out.print("path : " + start);
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
            System.out.print(", "+currVertex);
        }
        return path;
    }
    private static boolean canBeAdded(Vertex[] vertices, int[] edge, int start, int end){
        System.out.println("edge that we are checking: " +Arrays.toString(edge));
        if ((edge[0]==start&&edge[1]==end)||(edge[1]==start&&edge[0]==end))
            return false;
        if (vertices[start].getNumOfConnections()==0) {
            if (edge[0]!=start&&edge[1]!=start)
                return true;
        }
        if (vertices[end].getNumOfConnections()==0) {
            if (edge[0]!=end&&edge[1]!=end)
                return true;
        }
        /**
         * follow path from start, if all vertices would be closed on the path, then this can't be added
         */
        int currVertex = start;
        int prevVertex = start;
        while (true){
            if (currVertex == edge[0]){
                if (edge[1]==end){
                    return false; // this means our path goes from start to the end without any conflict
                }
            }
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
            System.out.println("going from: " +prevVertex+" to: "+currVertex);

            if (currVertex == end){
                return false;
            }
        }
    }



    private static int[] getLowestRemainingEdge(double[][] edges, boolean[] remainingPoints, Vertex[] vertices, int start, int end){
        double lowestEdge = Double.POSITIVE_INFINITY;
        int[] coords = new int[2];
        for (int i = 0; i < remainingPoints.length; i++) {
            if (!remainingPoints[i]) continue;

            for (int j = i+1; j < edges[i].length; j++) {
                if (!remainingPoints[j]) continue;

                if (edges[i][j]<lowestEdge){
                    if (!canBeAdded(vertices,new int[]{i,j},start,end)) continue;

                    lowestEdge = edges[i][j];
                    coords[0] = i;
                    coords[1] = j;
                }
            }
        }
        System.out.println("lowest edge: "+ Arrays.toString(coords));
        return coords;
    }

    private static double[][] calcEdges(float[][] g){
        double[][] weights = new double[g.length][g.length];

        for (int i = 0; i < g.length; i++) {
            for (int j = i+1; j < g.length; j++) {
                weights[i][j] = Algorithms.getDistance(g[i],g[j]);
                weights[j][i] = weights[i][j];
            }
        }

        return weights;
    }

    private static class Vertex{
        final int index;
        List<Integer> connections;
        private Vertex(int index){
            this.index = index;
            connections = new ArrayList<>();
        }

        private void addEdge(int vertex){
            connections.add(vertex);
        }

        int getNumOfConnections(){
            return connections.size();
        }
    }
}
