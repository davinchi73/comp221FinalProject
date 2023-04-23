package race;

import java.util.*;

public class Dijkstra {

    public static void main(String[] args) {
        float[][] g = new float[][]{{1,3},{1,4},{2,5},{4,5},{2,7}};
        Dijkstra.djiskstras(g,0,3);
    }

    /**
     * I believe this is a correct implementation of dijkstras
     * However, I have not made the extra condition that every node must be visited
     * NOTE: this will always return a path with a length of one, as the quickest journey is a straight line
     */
    private static void djiskstras (float[][] g, int start, int end) {
        double[] distance = new double[g.length];
        Integer[] prev = new Integer[g.length];
        for (int i = 0; i < g.length; i++) {
            if (i==start){
                continue;
            }
            distance[i] = Double.POSITIVE_INFINITY;
            prev[i] = -1;
        }

        distance[start] = 0;
        boolean[] isVisited = new boolean[g.length];
        int numVisited = 0;
        Arrays.fill(isVisited,false);
        while (numVisited<g.length-1){
            int currVertex = returnLowestDist(distance,isVisited);
            System.out.println(currVertex);
            isVisited[currVertex] = true;
            numVisited++;
            if (currVertex == end){
                break;
            }
            for (int i = 0; i < g.length; i++) {
                if (currVertex==i||isVisited[i]){
                    continue;
                }
                double alt = distance[currVertex] + getDistance(g[i],g[currVertex]);
                if (alt<distance[i]){
                    distance[i] = alt;
                    prev[i] = currVertex;
                    System.out.println("currVertex: "+Arrays.toString(g[currVertex])+" i: "+Arrays.toString(g[i]));
                }

            }
        }
        //reconstruct path
        List<Integer> path = new ArrayList<>();
        path.add(end);
        path.add(prev[end]);
        int currVertex = prev[end];
        while(prev[currVertex]!=null){
            path.add(prev[currVertex]);
            System.out.println(prev[currVertex]);
            currVertex = prev[currVertex];
        }
        for (int i: path) {
            System.out.print(i+", ");
        }

    }

    private static int returnLowestDist(double[] distances, boolean[] isVisited){
        double thingy = Double.POSITIVE_INFINITY;
        int lowestVertex = -1;
        for (int i = 0; i < distances.length; i++) {
            if (isVisited[i]){
                continue;
            }
            if (distances[i]<thingy){
                thingy = distances[i];
                lowestVertex = i;
            }
        }
        if (lowestVertex==-1){
            for (int i = 0; i < isVisited.length; i++) {
                if (!isVisited[i]){
                    return i;
                }
            }
        }
        return lowestVertex;
    }
    private static class Vertex{
        double dist;
        final int indice;
        private Vertex(double dist, int indice){
            this.dist = dist;
            this.indice = indice;
        }
    }

    private static class VertexComp implements Comparator<Vertex>{
        @Override
        public int compare(Vertex o1, Vertex o2) {
            if (o1.dist<o2.dist){ //reversed order to give lowest val first
                return -1;
            } else if (o2.dist<o1.dist) {
                return 1;
            }
            return 0;
        }
    }
    static double getDistance(float[] a, float[] b){
        return Math.sqrt(Math.pow(a[0]-b[0],2)+Math.pow(a[1]-b[1],2));
    }

   
}