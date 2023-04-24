package race;

import java.util.ArrayList;
import java.util.List;

public class AlgoVertex {
    private final int index;
    List<Integer> connections;
    private boolean visited;
    AlgoVertex(int index){
        this.index = index;
        connections = new ArrayList<>();
        visited = false;
    }

    void addEdge(int vertex){
        connections.add(vertex);
    }

    int getNumOfConnections(){
        return connections.size();
    }

    void setAsVisited(){
        visited = true;
    }

    boolean isVisited(){
        return visited;
    }
}
