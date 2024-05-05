
/*************************************************************************
 *  Compilation:  javac SchubsArc.java
 *  Execution:    java SchubsArc <archive-name> <file1> <file2> ... OR java SchubsArc <archive-name> <GLOB>
 *  Run Example:  java SchubsArc ../src/SchubsArcTests ../src/SchubsArcTests/blee.txt ../src/SchubsArcTests/blue.txt
 *                  OR
 *                java SchubsArc ../src/SchubsArcTests ../src/SchubsArcTests/*.txt
 * 
 *  Description:  This program compresses a file using Huffman encoding and then archives the compressed files into a single archive file.
 * 
 *  Test Instructions: mvn test
 *  
 *  Ethan Spindler
 *  CS 375
 *  May 5 2024
 *  
 *
 *************************************************************************/
import java.io.IOException;
import java.io.File;

public class SchubsArc {
    public static void main(String[] args) throws IOException {
        File in1 = null;
        BinaryIn bin1 = null;
        BinaryOut out = null;

        char separator = (char) 255; // all ones 11111111

        if (args.length < 2) {
            System.out.println("Usage: java SchubsArc <archive-name> <file1> <file2> ...");
            throw new IllegalArgumentException("Incorrect number of arguments.");
        }

        File input = null;
        for (int i = 1; i < args.length; i++) {
            input = new File(args[i]);
            if (!input.exists()) {
                System.out.println("    File " + args[i] + " does not exist.");
                System.out.println("    Please provide a valid file name.");
                throw new IllegalArgumentException("File " + args[i] + " does not exist.");
            }

            if (input.isDirectory()) {
                System.out.println("    File " + args[i] + " is a directory.");
                System.out.println("    SchubsArc is for files only.");
                throw new IllegalArgumentException("File " + args[i] + " is a directory.");
            }
        }

        // String archiveName = args[0];
        // out = new BinaryOut(archiveName + ".zh"); // Provide the correct file path
        // and extension
        String[] filePath = args[0].split("[/\\\\]");
        String archiveName = filePath[filePath.length - 1];

        // String archivePath = "";
        // for (int i = 0; i < filePath.length - 2; i++) {
        // archivePath += filePath[i] + File.separator;
        // }

        String archivePath = args[0] + File.separator;
        archivePath += archiveName + ".zh";
        out = new BinaryOut(archivePath);
        // System.out.println(args[0]);

        try {
            for (int i = 1; i < args.length; i++) {
                String fileName = args[i] + ".hh";
                in1 = new File(fileName);
                File argFile = new File(args[i]);
                if (argFile.length() == 0) {
                    System.out.println("    File " + args[i] + " is empty.");
                } else {
                    SchubsH.compress(args[i], args[i] + ".hh");
                    System.out.println("File " + args[i] + " compressed successfully.");
                }
                if (!in1.exists() || !in1.isFile()) {
                    System.out.println("    File " + fileName + " does not exist or is not a regular file.");
                    continue;
                }

                long fileSize = in1.length();
                int fileNameSize = fileName.length();

                out.write(fileNameSize);
                out.write(separator);

                out.write(fileName);
                out.write(separator);

                out.write(fileSize);
                out.write(separator);

                bin1 = new BinaryIn(fileName);
                while (!bin1.isEmpty()) {
                    out.write(bin1.readChar());
                }
                System.out.println(fileName + " archived successfully.");
            }
        } finally {
            // SchubsH.compress(archivePath, archivePath + ".zh");
            File archiveFinal = new File(archivePath);
            if (archiveFinal.exists() || archiveFinal.isFile())
                System.out.println("Archive " + archivePath + " created successfully.");
            if (out != null)
                out.close();
        }
    }
}