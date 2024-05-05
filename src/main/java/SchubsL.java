
/*************************************************************************
 *  Compilation:  javac SchubsL.java
 *  Execution:    java SchubsL file1.txt file2.txt file3.txt ... OR java SchubsL <GLOB>
 *  Run Example:  java SchubsL ../src/SchubsH/test1.txt
 * 
 *  Description:  This program compresses a given file using LZW encoding.
 * 
 *  Design: LZW (Lempel-Ziv-Welch) encoding is a dictionary-based compression algorithm commonly
 *          used for lossless data compression. Unlike Huffman encoding, which operates on individual characters,
 *          LZW encoding works on sequences of characters, allowing it to capture repetitive patterns more effectively.
 *          The algorithm starts with an initial dictionary containing all possible single-character sequences. 
 *          It then iterates through the input data, building the dictionary dynamically as it encounters new sequences.
 *          When a new sequence is encountered that is not already in the dictionary, it is added to the dictionary along 
 *          with a code representing it. The code for the new sequence is typically the next available code in the dictionary.
 *          During encoding, the algorithm replaces sequences of characters with their corresponding codes from the dictionary,
 *          thereby reducing the size of the data. As the encoding progresses, the dictionary grows to include longer and more 
 *          complex sequences, enabling efficient representation of repetitive patterns.
 *          One of the key features of LZW encoding is its adaptability to the input data. Unlike fixed-length codes used in some other 
 *          compression techniques, LZW adapts its dictionary dynamically based on the patterns present in the input data, 
 *          leading to efficient compression for a wide range of data types.
 * 
 *  Trade Off: One notable trade-off is the overhead associated with maintaining and transmitting the dictionary alongside the compressed data. 
 *          While the dictionary enables efficient compression by representing repetitive sequences with shorter codes, 
 *          it adds overhead to the compressed data, particularly for smaller input sizes where the benefits of compression may 
 *          be outweighed by the dictionary size.
 *          Another trade-off lies in the complexity of the encoding and decoding processes. While encoding data using LZW is relatively 
 *          straightforward, decoding requires building and maintaining the dictionary in sync with the encoding process. This can introduce 
 *          computational overhead, especially for large datasets or in scenarios where memory usage is a concern.
 * 
 *  Test Instructions: mvn test
 *  
 *  Ethan Spindler
 *  CS 375
 *  May 5 2024
 *  
 *
 *************************************************************************/
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.HashMap;

public class SchubsL {
    private static final int R = 256; // number of input chars
    private static final int L = 4096; // number of codewords = 2^W
    private static final int W = 500; // codeword width

    public static void compress(String inputFile, String outputFile) throws FileNotFoundException {
        File file = new File(inputFile);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + inputFile);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
                DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(outputFile))) {

            int character;
            StringBuilder input = new StringBuilder();
            while ((character = reader.read()) != -1) {
                input.append((char) character);
            }

            if (input.length() == 0) {
                // If the input file is empty, write an empty string to the output file
                outputStream.writeUTF("");
                return;
            }

            HashMap<String, Integer> st = new HashMap<>();
            for (int i = 0; i < R; i++) {
                st.put("" + (char) i, i);
            }
            int code = R + 1; // R is codeword for EOF

            StringBuilder compressedData = new StringBuilder();
            String current = "";
            for (int i = 0; i < input.length(); i++) {
                char nextChar = input.charAt(i);
                String combined = current + nextChar;
                if (st.containsKey(combined)) {
                    current = combined;
                } else {
                    compressedData.append(st.get(current)).append(" ");
                    if (code < L) {
                        st.put(combined, code++);
                    }
                    current = "" + nextChar;
                }
            }
            if (!current.equals("")) {
                compressedData.append(st.get(current));
            }

            outputStream.writeUTF(compressedData.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("    Usage: java SchubsL file1 file2 file3 ... OR java SchubsL <GLOB>");
            System.out.println("    file1: file to be compressed");
            System.out.println("    file2: compressed file");
            System.out.println("    file3: file to be compressed ...");
            System.out.println("    Incorrect number of arguments.");
            throw new IllegalArgumentException("Invalid argument");
        }
        for (String filename : args) {
            String outputFileName = filename + ".ll";
            try {
                compress(filename, outputFileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
