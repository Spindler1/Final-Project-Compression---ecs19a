
/*************************************************************************
 *  Compilation:  javac Deschubs.java
 *  Execution:    java Deschubs <filename>.<extension>
 *  Run Example:  java Deschubs ../src/DeschubsTests/testFile.txt.hh
 * 
 *  Description:  This program decompresses .hh|.ll|.zh files that have been compressed into their original files
 * 
 *  Test Instructions: mvn test
 *          - Make sure to delete or move the files created during testing before testing again
 *                      - This is because we cannot rewrite the original text files or overwrite it
 *          - The files to delete:
 *                      - src/DeschubsTests/DeschubsTars.txt
 *                      - src/DeschubsTests/DeschubsTars1.txt
 *                      - src/DeschubsTests/DeschubsH.txt
 *                      - src/DeschubsTests/DeschubsTars.txt.hh
 *                      - src/DeschubsTests/DeschubsTars1.txt.hh
 *                      - src/DeschubsTests/testFile.txt
 *  
 *  Ethan Spindler
 *  CS 375
 *  May 5 2024
 *  
 *
 *************************************************************************/
import java.io.*;
import java.util.*;

public class Deschubs {
    private static final int R = 256; // number of input chars
    private static final int L = 4096; // number of codewords = 2^W
    private static final int W = 500; // codeword width

    // Huffman trie node
    private static class Node implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        // is the node a leaf node?
        private boolean isLeaf() {
            assert (left == null && right == null) || (left != null && right != null);
            return (left == null && right == null);
        }

        // compare, based on frequency
        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }

    public static void decompress(String inputFile, String outputFile) {
        try {
            // Check if the file is Huffman compressed (.hh)
            if (inputFile.endsWith(".hh")) {
                // create input stream for reading compressed file
                FileInputStream fis = new FileInputStream(inputFile);
                ObjectInputStream ois = new ObjectInputStream(fis);

                // read in Huffman trie from input stream
                Node root = readTrie(ois);

                // number of bytes to write
                int length = ois.readInt();

                // create output stream for writing uncompressed file
                FileOutputStream fos = new FileOutputStream(outputFile); // remove ".hh" or ".ll" extension
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                // decode using the Huffman trie and write to uncompressed file
                for (int i = 0; i < length; i++) {
                    Node x = root;
                    while (!x.isLeaf()) {
                        boolean bit = ois.readBoolean();
                        if (bit)
                            x = x.right;
                        else
                            x = x.left;
                    }
                    bos.write(x.ch);
                }
                bos.flush();
                bos.close();
                ois.close();
            }
            // Check if the file is LZW compressed (.ll)
            else if (inputFile.endsWith(".ll")) {
                try (DataInputStream inputStream = new DataInputStream(new FileInputStream(inputFile));
                        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

                    String compressedData = inputStream.readUTF();
                    String[] codes = compressedData.split(" ");

                    HashMap<Integer, String> st = new HashMap<>();
                    for (int i = 0; i < R; i++) {
                        st.put(i, "" + (char) i);
                    }
                    int code = R + 1; // R is codeword for EOF

                    StringBuilder outputData = new StringBuilder();
                    String current = "" + (char) Integer.parseInt(codes[0]);
                    outputData.append(current);

                    for (int i = 1; i < codes.length; i++) {
                        int nextCode = Integer.parseInt(codes[i]);
                        String entry;
                        if (st.containsKey(nextCode)) {
                            entry = st.get(nextCode);
                        } else if (nextCode == code) {
                            entry = current + current.charAt(0);
                        } else {
                            throw new IllegalStateException("Invalid compressed file");
                        }

                        outputData.append(entry);
                        if (code < L) {
                            st.put(code++, current + entry.charAt(0));
                        }
                        current = entry;
                    }

                    writer.write(outputData.toString());
                }
            }
            // Unsupported file format
            else {
                System.out.println("Unsupported file format: " + inputFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void expand(String filename) throws IOException {
        // check if the file that is soon to be expanded into already exists
        File file = new File(filename.substring(0, filename.length() - 3));
        System.out.println(filename.substring(0, filename.length() - 3));
        // if (file.exists()) {
        // System.out.println("File " + filename.substring(0, filename.length() - 3) + "
        // already exists.");
        // throw new IOException("File already exists.");
        // }
        try {
            System.out.println("New file being created");
            // create input stream for reading compressed file
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);

            // read in Huffman trie from input stream
            Node root = readTrie(ois);

            // number of bytes to write
            int length = ois.readInt();

            // create output stream for writing uncompressed file
            FileOutputStream fos = new FileOutputStream(filename.substring(0, filename.length() - 3)); // remove ".hh"
                                                                                                       // extension
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            // decode using the Huffman trie and write to uncompressed file
            for (int i = 0; i < length; i++) {
                Node x = root;
                while (!x.isLeaf()) {
                    boolean bit = ois.readBoolean();
                    if (bit)
                        x = x.right;
                    else
                        x = x.left;
                }
                bos.write(x.ch);
            }
            bos.flush();
            bos.close();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Node readTrie(ObjectInputStream ois) throws IOException {
        boolean isLeaf = ois.readBoolean();
        if (isLeaf) {
            return new Node(ois.readChar(), -1, null, null);
        } else {
            return new Node('\0', -1, readTrie(ois), readTrie(ois));
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Deschubs <filename>.<extension>");
            throw new IllegalArgumentException("Incorrect number of arguments.");
        }
        String filename = args[0];
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        if (extension.equals("hh")) {
            expand(filename);
        } else if (extension.equals("zh")) {
            BinaryIn in = null;
            BinaryOut out = null;

            char sep = (char) 255; // all ones 11111111

            try {
                in = new BinaryIn(args[0]);

                while (!in.isEmpty()) {
                    int filenamesize = in.readInt();
                    sep = in.readChar();
                    String filenameUntar = "";
                    for (int i = 0; i < filenamesize; i++)
                        filenameUntar += in.readChar();

                    sep = in.readChar();
                    long filesize = in.readLong();
                    sep = in.readChar();
                    System.out.println("Extracting file: " + filenameUntar + " (" + filesize + ").");
                    out = new BinaryOut(filenameUntar);
                    for (int i = 0; i < filesize; i++)
                        out.write(in.readChar());

                    System.out.println(filenameUntar + " extracted successfully.");
                    if (out != null) {
                        out.close();
                        System.out.println("File " + filenameUntar + " closed successfully.");
                    }
                    expand(filenameUntar);
                }

            } finally {
                if (out != null)
                    out.close();
            }
        } else if (extension.equals("ll")) {
            decompress(filename, filename + ".ll");
        }

        else {
            System.out.println("Invalid arguments. Only .hh and .zh and .ll files are supported.");
        }
    }
}
