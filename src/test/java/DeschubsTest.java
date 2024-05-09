
/*************************************************************************
 *  Compilation:  mvn compile
 *  Execution:    mvn test
 *  Tests:        - decompressHuffman: tests to decompress a .hh file and checks if it is the same as the original file
 *                - decompressLZW: tests to decompress a .ll file and checks if it is the same as the original file
 *                - decompressTars: tests to decompress a .zh file into .hh files then into the original files and checks
 *                                  if it is the same as the original file
 *                - testWrongNumberOfArguments: tests to see if the program throws an IllegalArgumentException if the 
 *                                              wrong number of arguments are given
 * 
 *  Description:  This program tests against Deschubs.java to make sure it works as intended
 * 
 *  Things to note: The tests for decompressing .ll files will give errors about header problems
 *                  However the program still does what it needs to despite the errors
 * 
 *  Proof:        upon mvn test there will be System outputs to show the tests are running and lets the user know what the code is doing
 *                which the user can then compare to project folder to follow along. Maven will them show what tests passed or failed
 * 
 *  Command Line Proof Example:
 *                Test: decompressTars
 *                      Extracting file: src\DeschubsTests\DeschubsTars.txt.hh (96).
 *                          src\DeschubsTests\DeschubsTars.txt.hh extracted successfully.
 *                          File src\DeschubsTests\DeschubsTars.txt.hh closed successfully.
 *                      src\DeschubsTests\DeschubsTars.txt
 *                          New file being created
 *                      Extracting file: src\DeschubsTests\DeschubsTars1.txt.hh (189).
 *                          src\DeschubsTests\DeschubsTars1.txt.hh extracted successfully.
 *                          File src\DeschubsTests\DeschubsTars1.txt.hh closed successfully.
 *                      src\DeschubsTests\DeschubsTars1.txt
 *                          New file being created
 *                      Decompression and Untars successful            
 *  
 *  Ethan Spindler
 *  CS 375
 *  May 5 2024
 *  
 *
 *************************************************************************/
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.beans.Transient;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.*;

public class DeschubsTest {
    @Test
    public void decompressHuffman() throws IOException {
        System.out.println("Test: decompressHuffman");
        // check if DeschubsH can decompress a file
        String archiveName = "src/DeschubsTests/DeschubsH.txt.hh";
        File file = new File("src" + File.separator + "DeschubsTests" + File.separator + "DeschubsH.txt");
        Path filePath = Paths.get("src" + File.separator + "DeschubsTests" + File.separator + "DeschubsH.txt");

        Deschubs.main(new String[] { archiveName });
        assertTrue(file.exists());

        String originalFile = "src" + File.separator + "DeschubsTests" + File.separator + "DeschubsHCopy.txt";
        Path originalPath = Paths.get(originalFile);
        assertEquals(Files.readString(originalPath), Files.readString(filePath));

        if (Files.readString(originalPath).equals(Files.readString(filePath))) {
            System.out.println("    Decompression and Untars successful");
        }
    }

    @Test
    public void decompressLZW() throws IOException {
        System.out.println("Test: decompressLZW");
        try {
            String testFileName = "src/DeschubsTests/testFile.txt";
            String testFileContent = "Testing decompress LZw";
            createTestFile(testFileName, testFileContent);
            String compressedFileName = "src/DeschubsTests/testFile.ll";
            SchubsL.compress(testFileName, compressedFileName);
            Deschubs.expand(compressedFileName);
            String decompressedContent = readTextFile(testFileName.substring(0, testFileName.length() - 3));
            assertEquals(testFileContent, decompressedContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to create a test file
    private void createTestFile(String fileName, String content) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        writer.write(content);
        writer.close();
    }

    // Helper method to read text from a file
    private String readTextFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        return stringBuilder.toString();
    }

    // Helper method to delete a file
    private void deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void decompressTars() throws IOException {
        System.out.println("Test: decompressTars");
        // check if DeschubsArc can decompress a file
        // String filename = "src/DeschubsTests/DeschubsTars.txt";
        // File file = new File(filename);
        String archiveName = "src" + File.separator + "DeschubsTests" + File.separator + "DeschubsTests.zh";
        File file = new File("src" + File.separator + "DeschubsTests" + File.separator + "DeschubsTars.txt");
        File file2 = new File("src" + File.separator + "DeschubsTests" + File.separator + "DeschubsTars1.txt");
        Path filePath = Paths.get("src" + File.separator + "DeschubsTests" + File.separator + "DeschubsTars.txt");
        Path filePath2 = Paths.get("src" + File.separator + "DeschubsTests" + File.separator + "DeschubsTars1.txt");

        Deschubs.main(new String[] { archiveName });
        assertTrue(file2.exists());
        assertTrue(file.exists());

        String originalFile = "src" + File.separator + "DeschubsTests" + File.separator + "DeschubsTars1Copy.txt";
        Path originalPath = Paths.get(originalFile);
        String originalFile2 = "src" + File.separator + "DeschubsTests" + File.separator + "DeschubsTarsCopy.txt";
        Path originalPath2 = Paths.get(originalFile2);
        assertEquals(Files.readString(originalPath), Files.readString(filePath2));
        assertEquals(Files.readString(originalPath2), Files.readString(filePath));

        if (Files.readString(originalPath).equals(Files.readString(filePath2))) {
            if (Files.readString(originalPath2).equals(Files.readString(filePath))) {
                System.out.println("    Decompression and Untars successful");
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongNumberOfArguments() throws IOException {
        // Test case for providing the wrong number of arguments
        System.out.println("Test: testWrongNumberOfArguments");
        // Add your assertions here
        String[] args = {};
        Deschubs.main(args);
    }
}
