package prereqchecker;

import java.util.*;

/**
*
* @author Emrah Yilgen
*
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
 * NeedToTakeInputFile name is passed through the command line as args[1]
 * Read from NeedToTakeInputFile with the format:
 * 1. One line, containing a course ID
 * 2. c (int): Number of courses
 * 3. c lines, each with one course ID
 * 
 * Step 3:
 * NeedToTakeOutputFile name is passed through the command line as args[2]
 * Output to NeedToTakeOutputFile with the format:
 * 1. Some number of lines, each with one course ID
 */

 
public class NeedToTake {

    public static ArrayList<String> takeThese(Graph map, String targetAndTaken){
        ArrayList<String> takenAndPrereqs = new ArrayList<>(); // store all the prereqs to compare with target's prereqs
        ArrayList<String> targetPrereqs = new ArrayList<>();   // store all the prereqs to compare with taken's prereqs
        ArrayList<String> neededClasses = new ArrayList<>();
        Queue<String> takenQ = new LinkedList<>(); // initially store all taken course's direct prereqs to get all the in-direct prereqs
        Queue<String> targetQ = new LinkedList<>();  // initially store all target course's direct prereqs to get all the in-direct prereqs
        String targetCourse = StdIn.readString();
        targetQ.addAll(map.get(targetCourse));
        int numOftaken = StdIn.readInt();
        for(int i = 0; i < numOftaken; i++){
            takenQ.add(StdIn.readString());
        }
        while(!takenQ.isEmpty()){
            String y = takenQ.peek();
            takenAndPrereqs.add(takenQ.remove());
                for(int j = 0; j < map.get(y).size(); j++){
                    String z = map.get(y).get(j);
                    if(!takenQ.contains(z) && !takenAndPrereqs.contains(z)){
                    takenQ.add(z);
                    } 
                }
            } 
        while(!targetQ.isEmpty()){
            String p = targetQ.peek();
            targetPrereqs.add(targetQ.remove());
                for(int k = 0; k < map.get(p).size(); k++){
                    String m = map.get(p).get(k);
                    if(!targetQ.contains(m) && !targetPrereqs.contains(m)){
                    targetQ.add(m);
                    } 
                }
        }  
        for(String temp : targetPrereqs){
            if(!takenAndPrereqs.contains(temp)){
                neededClasses.add(temp);
            }
        }
        
        return neededClasses;
    }
    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java NeedToTake <adjacency list INput file> <need to take INput file> <need to take OUTput file>");
            return;
        }

        String inputFile = args[0];
        String needToTakeInput = args[1];
        String output = args[2];  
        Graph graph = new Graph(inputFile);
        StdIn.setFile(needToTakeInput);
        StdOut.setFile(output);
        ArrayList<String> needToTake = takeThese(graph, needToTakeInput);
        for(int i = 0; i < needToTake.size(); i++){
            StdOut.println(needToTake.get(i));
        }
    }
}
