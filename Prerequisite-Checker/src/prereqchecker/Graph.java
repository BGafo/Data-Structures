package prereqchecker;

import java.util.*;

/**
 * This class contains methods which perform various operations on a graph
 * to create a graph of given university classes
 * 
 * @author Emrah Yilgen
 * 
 */

public class Graph {
    private HashMap<String, ArrayList<String>> graph;

    

    public Graph(String inputFile) {
        if (inputFile == null) throw new IllegalArgumentException("argument is null");
            StdIn.setFile(inputFile);
            int vertices = StdIn.readInt();
            if (vertices < 0) throw new IllegalArgumentException("number of vertices in a Graph must be non-negative");
            graph = new HashMap<String, ArrayList<String>>(vertices);
               for (int v = 0; v < vertices; v++) {
                    String classId = StdIn.readString();
                   graph.put(classId, new ArrayList<String>());
               }
               int edgesCount = StdIn.readInt();
               StdIn.readLine();
               for (int e = 0; e < edgesCount; e++) {
                String edge = StdIn.readLine();
                String[] splitStr = edge.split(" ");
                if(graph.get(splitStr[0]) != null)
                    graph.get(splitStr[0]).add(splitStr[1]);
           } 
    } 
    

    public Graph(String inputFile, String outputFile) {
        StdOut.setFile(outputFile);
        
        if (inputFile == null) throw new IllegalArgumentException("argument is null");
            StdIn.setFile(inputFile);
            int vertices = StdIn.readInt();
            if (vertices < 0) throw new IllegalArgumentException("number of vertices in a Graph must be non-negative");
            graph = new HashMap<String, ArrayList<String>>(vertices);
               for (int v = 0; v < vertices; v++) {
                    String classId = StdIn.readString();
                   graph.put(classId, new ArrayList<String>());
               }
               int edgesCount = StdIn.readInt();
               StdIn.readLine();
               for (int e = 0; e < edgesCount; e++) {
                String edge = StdIn.readLine();
                String[] splitStr = edge.split(" ");
                if(graph.get(splitStr[0]) != null)
                    graph.get(splitStr[0]).add(splitStr[1]);
           }  
           ArrayList <String> keys = new ArrayList<>();
           for(int i = 0; i < 1; i++){
               keys.addAll(graph.keySet());
           }
           for(int i = 0; i < keys.size(); i++){
                String key = keys.get(i);
                StdOut.print(keys.get(i) + " ");
                int x = graph.get(keys.get(i)).size();
                ArrayList<String> value = graph.get(key);
                    for(int j = 0; j < x; j++){
                        StdOut.print(value.get(j) + " ");     
                    }
                if(i < keys.size() - 1)
                StdOut.println();
           }   
           
    }


    public ArrayList<String> get(String string) {
        return graph.get(string);
    }


    public  ArrayList<String> getKeySet() {
        ArrayList<String> allKeySet = new ArrayList<>();
        for(int i = 0; i < 1; i++){
            allKeySet.addAll(graph.keySet());
        }
        return allKeySet;
    }
}
    
    

 
