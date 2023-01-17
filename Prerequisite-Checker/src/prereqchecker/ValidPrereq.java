package prereqchecker;

import java.util.*;

 /**
 *
 * @author Emrah Yilgen
 *
 * Steps to implement this class main method:
 * 
 * Step 1:
 * AdjListInputFile name is passed through the command line as args[0]
 * Read from AdjListInputFile with the format:
 * 1. a (int): number of courses in the graph
 * 2. a lines, each with 1 course ID
 * 3. b (int): number of edges in the graph
 * 4. b lines, each with a source ID
 * 
 * Step 2:
 * ValidPreReqInputFile name is passed through the command line as args[1]
 * Read from ValidPreReqInputFile with the format:
 * 1. 1 line containing the proposed advanced course
 * 2. 1 line containing the proposed prereq to the advanced course
 * 
 * Step 3:
 * ValidPreReqOutputFile name is passed through the command line as args[2]
 * Output to ValidPreReqOutputFile with the format:
 * 1. 1 line, containing either the word "YES" or "NO"
 */

public class ValidPrereq {

    public static String valid(Graph map, String class1, String class2){
        Queue<String> q = new LinkedList<>();
        String currPreq = class2;
        
        if(map.get(currPreq).size() == 0){
            return "YES";
        } else{
        do{
            for(int i = 0; i < map.get(currPreq).size(); i++){
                String x = map.get(currPreq).get(i);
                if(!x.equals(class1) && !q.contains(x)){
                q.add(x);
                } else if(x.equals(class1)){
                    return "NO";
                }
            }
                String y = q.remove();
                for(int j = 0; j < map.get(y).size(); j++){
                    String z = map.get(y).get(j);
                    if(!z.equals(class1) && !q.contains(z)){
                    q.add(z);
                    } else if(z.equals(class1)){
                        return "NO";
                    }
                }
                 currPreq = q.peek();
        }
        while(!q.isEmpty());
    }
     return "YES";
     }
    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.ValidPrereq <adjacency list INput file> <valid prereq INput file> <valid prereq OUTput file>");
            return;
        }
        String inputFile = args[0];
        String validInput = args[1];
        String outputFile = args[2];  
        Graph graph = new Graph(inputFile);
        StdIn.setFile(validInput);
        StdOut.setFile(outputFile);
        String classOne = StdIn.readString();
        String classTwo = StdIn.readString();
        String res = valid(graph, classOne, classTwo);
        if(res.equals("NO")){
            StdOut.println("NO");
        } else 
            StdOut.println("YES"); 
    }
}