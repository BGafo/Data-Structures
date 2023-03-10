package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Emrah Yilgen
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                System.exit(1);
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }
    
    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /**
     * Reads a given text file character by character, and returns an arraylist
     * of CharFreq objects with frequency > 0, sorted by frequency
     * 
     * @param filename The text file to read from
     * @return Arraylist of CharFreq objects, sorted by frequency
     */
    public static ArrayList<CharFreq> makeSortedList(String filename) {
        StdIn.setFile(filename);
        ArrayList<CharFreq> chars = new ArrayList<CharFreq>();
        int arr[] = new int[128];
        int count = 0;
        double freq = 0.0;
        double nextFreq = 0.0;
        int totalChars = 0;
        while(StdIn.hasNextChar()){
            char chr = StdIn.readChar();
            arr[chr]++;
            totalChars++;
        }
        for(int i = 0; i<arr.length; i++){
            if(arr[i] != 0){
                count++;
            }
        }
        int k = 0;
        for(int i = 0; i<arr.length; i++){
            if(arr[i] != 0){
                freq = (double) arr[i] / totalChars;
                nextFreq = (double) arr[i+1] / totalChars;
            }
            if(count > 1 && arr[i] != 0) {
                chars.add(k, new CharFreq((char) i, freq));
                k++;
            
            
            }
            else if(i == 127 && arr[i] != 0 && count == 1){
                chars.add(k, new CharFreq((char) i, freq));    
                chars.add(k+1, new CharFreq((char) 0, nextFreq)); 
                k += 2;
            }
            else if(count == 1 && arr[i] != 0){
                chars.add(k, new CharFreq((char) i, freq));
                chars.add(k+1, new CharFreq((char) (i+1), nextFreq));
                k += 2;
            }
        }
        for(int j = 0; j<chars.size(); j++){
            
            Collections.sort(chars);
            System.out.println(chars);
        }

        return chars;
    }

    /**
     * Uses a given sorted arraylist of CharFreq objects to build a huffman coding tree
     * 
     * @param sortedList The arraylist of CharFreq objects to build the tree from
     * @return A TreeNode representing the root of the huffman coding tree
     */
    public static TreeNode makeTree(ArrayList<CharFreq> sortedList) {
        Queue<TreeNode> source = new Queue<TreeNode>();
        Queue<TreeNode> target = new Queue<TreeNode>();
        
        
        // enqueue sortedList items into source queue
        for(int i = 0; i < sortedList.size(); i++){
            TreeNode list = new TreeNode();
            list.setData(sortedList.get(i));
            source.enqueue(list);
        }

        
        //TreeNode parent = null; // parent node of children   
        while( (!source.isEmpty()) || (target.size() > 1 ))
        {
            ArrayList<TreeNode> tree = new ArrayList<>();
            TreeNode parent = new TreeNode();
            double parentSum = 0.0; // sum of children of parent
            TreeNode node1;
            TreeNode node2;
            for(int k = 0; k < 2; k++){
                if(target.isEmpty() || (!source.isEmpty() && (source.peek().getData().getProbOccurrence() <= target.peek().getData().getProbOccurrence())) ){
                node1 = source.dequeue();
                tree.add(k, node1);
                } else if((source.isEmpty() && !target.isEmpty())|| (source.peek().getData().getProbOccurrence() > target.peek().getData().getProbOccurrence()) ){ 
                node2 = target.dequeue();
                tree.add(k, node2);
                }
            }
            TreeNode nodeA = tree.get(0);
            TreeNode nodeB = tree.get(1);
            parent.setLeft(nodeA);
            parent.setRight(nodeB);
            parentSum = nodeA.getData().getProbOccurrence() + nodeB.getData().getProbOccurrence();
            CharFreq parentData = new CharFreq(null, parentSum);
            parent.setData(parentData);
            target.enqueue(parent);

        }
        return target.peek();
}

    

  
   

    /**
     * Uses a given huffman coding tree to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null
     * 
     * @param root The root of the given huffman coding tree
     * @return Array of strings containing only 1's and 0's representing character encodings
     */
    public static String[] makeEncodings(TreeNode root) {
        String[] encodingBits = new String[128];
        encode(root, encodingBits, "");

        return encodingBits; 
    }

    // helper methods
    private static void encode(TreeNode target, String[] a, String code){
         char leaf = '\0';
        if(target == null) return;  
        encode(target.getLeft(), a, code + "0");  
        if(target.getData().getCharacter() != null){
            leaf = target.getData().getCharacter();
            a[(int) leaf] = code;
         }
        encode(target.getRight(), a, code + "1");       
    }

    /**
     * Using a given string array of encodings, a given text file, and a file name to encode into,
     * this method makes use of the writeBitString method to write the final encoding of 1's and
     * 0's to the encoded file.
     * 
     * @param encodings The array containing binary string encodings for each ASCII character
     * @param textFile The text file which is to be encoded
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public static void encodeFromArray(String[] encodings, String textFile, String encodedFile) {
        StdIn.setFile(textFile);
        String resultBits = "";
        char indivChars = '\0';
        while(StdIn.hasNextChar()){
            indivChars = StdIn.readChar();
            resultBits = resultBits + encodings[(int) indivChars];
        }
        writeBitString(encodedFile, resultBits);
    }
    
    /**
     * Using a given encoded file name and a huffman coding tree, this method makes use of the 
     * readBitString method to convert the file into a bit string, then decodes the bit string
     * using the tree, and writes it to a file.
     * 
     * @param encodedFile The file which contains the encoded text we want to decode
     * @param root The root of your Huffman Coding tree
     * @param decodedFile The file which you want to decode into
     */
    public static void decode(String encodedFile, TreeNode root, String decodedFile) {
       StdOut.setFile(decodedFile);
        String encodedBits = readBitString(encodedFile);
        String decodes = "";
        char eachBit = '\0';
        TreeNode ptr = root;
        TreeNode ptr2 = root;
        for(int i = 0; i < encodedBits.length(); i++){
            eachBit =  encodedBits.charAt(i);
            if(eachBit == '0' && ptr.getLeft() != null){
                ptr = ptr.getLeft();
            }
            if(eachBit == '1' && ptr.getRight() != null){
                ptr = ptr.getRight();
            }
            if(ptr.getData().getCharacter() == null){
                continue;
            }
            if(ptr.getData().getCharacter() != null){
                decodes = decodes + ptr.getData().getCharacter();
                ptr = ptr2;
            }     
        } 
        StdOut.print(decodes);  
     }
}
