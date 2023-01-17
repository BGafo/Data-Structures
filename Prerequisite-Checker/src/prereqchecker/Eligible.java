package prereqchecker;

import java.util.*;

/**
 * 
 * @author Emrah Yilgen
 *
 * Steps:
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
 * EligibleInputFile name is passed through the command line as args[1]
 * Read from EligibleInputFile with the format:
 * 1. c (int): Number of courses
 * 2. c lines, each with 1 course ID
 * 
 * Step 3:
 * EligibleOutputFile name is passed through the command line as args[2]
 * Output to EligibleOutputFile with the format:
 * 1. Some number of lines, each with one course ID
 */
public class Eligible {

    public static ArrayList<String> allTaken(Graph map, String inputFile){
        Queue<String> q = new LinkedList<>();
        ArrayList<String> totalTaken = new ArrayList<>();
        StdIn.setFile(inputFile);
        int numOfTaken = StdIn.readInt();
        for(int i = 0; i < numOfTaken; i++){
            String temp = StdIn.readString();
            q.add(temp);
        }
        while(!q.isEmpty()){
        String y = q.peek();
        totalTaken.add(q.remove());
            for(int j = 0; j < map.get(y).size(); j++){
                String z = map.get(y).get(j);
                if(!q.contains(z) && !totalTaken.contains(z)){
                q.add(z);
                } 
            }
        }
    return totalTaken;       
    }

    public static ArrayList<String> eligible(Graph map, ArrayList<String> taken){
        ArrayList<String> eligibleKeys = new ArrayList<>();
        ArrayList<String> keysSet = new ArrayList<>();
        keysSet.addAll(map.getKeySet());
        
        for(int i = 0; i < keysSet.size(); i++){
            String key = keysSet.get(i);
            if(taken.contains(key)){
                continue;
            } else{
                ArrayList<String> temp = new ArrayList<>();
                temp.addAll(map.get(key));
                boolean proceed = false;
                    for(String find : temp){
                        if(!taken.contains(find)){
                            proceed = false;
                            break;
                        } else{
                            proceed = true;
                          }
                    } 
                if(proceed == true){
                    eligibleKeys.add(key);
                }
              }
        }
    return eligibleKeys; 
    }
    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.Eligible <adjacency list INput file> <eligible INput file> <eligible OUTput file>");
            return;
        }

        String inputFile = args[0];
        String eligibleInput = args[1];
        String output = args[2];  
        Graph graph = new Graph(inputFile);
        StdIn.setFile(inputFile);
        StdIn.setFile(eligibleInput);
        StdOut.setFile(output);
        ArrayList<String> alreadyTaken = allTaken(graph, eligibleInput);
        ArrayList<String> takeNext = eligible(graph, alreadyTaken);
        for(int i = 0; i < takeNext.size(); i++){
            StdOut.println(takeNext.get(i));
        }
    }
}

