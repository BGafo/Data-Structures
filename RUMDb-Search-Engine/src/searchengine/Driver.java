package searchengine;

import java.util.ArrayList;

public class Driver {
    
    public static void main (String[] args) {

		int hashTableSize = 20;
        double threshold = 8;
        //String inputFile = "dataSample.txt";
        //String inputFile = "data.txt";
        String inputFile = "data.txt";
        String noiseWordsFile = "noisewords.txt";


        
        RUMDbSearchEngine rudb = new RUMDbSearchEngine(hashTableSize, threshold, noiseWordsFile);
		rudb.insertMoviesIntoHashTable(inputFile);
        //rudb.print();

        // String word1 = "young";
        // String word2 = "man";

        // String word1 = "tragic";
        // String word2 = "love";

        // String word1 = "daughter";
        // String word2 = "woman";


        // String word1 = "madame";
        // String word2 = "room";

        String word1 = "young";
        String word2 = "son";


        // ArrayList<MovieSearchResult> alp = rudb.createMovieSearchResult(word1, word2);
        // if ( alp != null && alp.size() > 0 ) {
        
        //         for ( MovieSearchResult s : alp ) {
        //             System.out.println(s.getTitle()); 
        //         }
        //         for ( MovieSearchResult s : alp ) {
        //             System.out.println("nana");
        //             System.out.println(s.getTitle()+"\t["+s.getMinDistance()+"]"); 
        //         }
        //     } else {
        //         StdOut.println("There are no movies with the words " + word1 + " and " + word2 + " at their description.");
        //     }
        
        

       

        ArrayList<MovieSearchResult> als = rudb.topTenSearch(word1, word2);
	
        if ( als != null && als.size() > 0 ) {
            
            StdOut.println("The shortest distance between " + word1 + " and " + word2 + " is located at:");
	
            for ( MovieSearchResult s : als ) {
                //System.out.println("nana");
                System.out.println(s.getTitle()+"\t["+s.getMinDistance()+"]"); 
            }
        } else {
            StdOut.println("There are no movies with the words " + word1 + " and " + word2 + " at their description.");
        }
	}
}
