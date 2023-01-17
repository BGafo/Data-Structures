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
 * SchedulePlanInputFile name is passed through the command line as args[1]
 * Read from SchedulePlanInputFile with the format:
 * 1. One line containing a course ID
 * 2. c (int): number of courses
 * 3. c lines, each with one course ID
 * 
 * Step 3:
 * SchedulePlanOutputFile name is passed through the command line as args[2]
 * Output to SchedulePlanOutputFile with the format:
 * 1. One line containing an int c, the number of semesters required to take the course
 * 2. c lines, each with up to 3 space separated course ID's
 */
public class SchedulePlan {
    public static void schedulePlan(Graph map, String scheduleInput, String outputPlan){
        HashMap<String, ArrayList<String>> neededCoursesMap = new HashMap<>(); // all the needed courses to take target
        HashMap<String, ArrayList<String>> targetMap = new HashMap<>(); // target's direct and in-direct prereqs map
        StdOut.setFile(outputPlan);
        Queue<String> takenQ = new LinkedList<>(); // initially store all taken course's direct prereqs to get all the in-direct prereqs
        Queue<String> targetQ = new LinkedList<>();  // initially store all target course's direct prereqs to get all the in-direct prereqs
        ArrayList<String> targetPrereqs; // store all the required prereqs of target course
        ArrayList<String> takenList = new ArrayList<>(); // already taken courses and their direct, in-direct prereqs
        ArrayList<String> tmp = new ArrayList<>();
        String target = StdIn.readString(); // target course
        int numOfTaken = StdIn.readInt(); // number of taken courses


        for(int d = 0; d <numOfTaken; d++){
            String takenAlready = StdIn.readString();
            takenList.addAll(prereqs(map, takenAlready));
            takenList.add(takenAlready);
        }

        //target's map includes all of the direct and in-direct prereqs of the target
        // each prereq points to a list that includes all the direct and 
        // in-direct prereqs of that course
        targetQ.add(target);
        int k = 0;
        while(!targetQ.isEmpty()){
            String y = targetQ.peek();
            targetPrereqs = new ArrayList<>();
            targetPrereqs = prereqs(map, y);
            
            for(int j = 0; j < map.get(y).size(); j++){
                String z = map.get(y).get(j);
                if(!tmp.contains(z)){
                    targetQ.add(z);
                }
                tmp.add(map.get(y).get(j));
            }
            if(!targetMap.containsKey(y) && k !=  0){
                targetMap.put(targetQ.remove(), targetPrereqs);
            } else if(k == 0){
                String doNotNeed = targetQ.remove();
                }
            k++;
        }
        ArrayList<String> keysSet = new ArrayList<>();
        keysSet.addAll(targetMap.keySet());
        for(String part : keysSet){
        boolean proceed = false;
            for(int m = 0; m < takenList.size(); m++){
                if((takenList.get(m).toString()).equals(part)){
                    proceed = true;
                }
            }
            if(proceed == false){
                neededCoursesMap.put(part, targetMap.get(part));
            }
        }
        ArrayList<String> keys = new ArrayList<>();
        keys.addAll(neededCoursesMap.keySet());
        ArrayList<String> moreTemp = new ArrayList<>();
        int numOfSemesters = 0;
        ArrayList<String> result = new ArrayList<>();
        for(int g = 0; g < neededCoursesMap.size(); g++){
            boolean addClass = false;
            String holder = "";
            for(int h = g + 1; h < neededCoursesMap.size(); h++){ 
                if (!neededCoursesMap.get(keys.get(g)).contains(keys.get(h)) && (!neededCoursesMap.get(keys.get(h)).contains(keys.get(g)))
                    && !moreTemp.contains(keys.get(g)) && !moreTemp.contains(keys.get(h))){
                    holder = holder + " " + keys.get(g);
                    moreTemp.add(keys.get(g));
                    holder = holder + " " + keys.get(h);
                    moreTemp.add(keys.get(h));
                    addClass = true;
                } else if(!moreTemp.contains(keys.get(g))){
                       continue;
                    }       
            }
            if(!moreTemp.contains(keys.get(g))){
                holder = holder + " " + keys.get(g);
                moreTemp.add(keys.get(g));
                addClass = true;
            }
            if(addClass == true){
            result.add(holder);
            holder ="";
            addClass = false;
            }
          
        }
        numOfSemesters = result.size();
        StdOut.println(numOfSemesters);
        for(String one : result){
            StdOut.println(one);
        }
        
    }     

    public static ArrayList<String> prereqs(Graph map, String target){
        Queue<String> q = new LinkedList<>();
        ArrayList<String> totalTaken = new ArrayList<>();
        String temp = target;
        q.add(temp);
        int i = 0;
        while(!q.isEmpty()){
        String y = q.peek();
        if(i > 0){
        totalTaken.add(q.remove());
        }
        if(i == 0){
            String noNeed = q.remove();
        }
        i++;
            for(int j = 0; j < map.get(y).size(); j++){
                String z = map.get(y).get(j);
                if(!q.contains(z) && !totalTaken.contains(z)){ 
                q.add(z);
                } 
            }
        }
    return totalTaken;       
    }
        
    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.SchedulePlan <adjacency list INput file> <schedule plan INput file> <schedule plan OUTput file>");
            return;
        }

       
        String inputFile = args[0];
        String schedulePlanInput = args[1];
        String output = args[2];  
        Graph graph = new Graph(inputFile);
        StdIn.setFile(schedulePlanInput);
        schedulePlan(graph, schedulePlanInput, output);

    }
}


 
