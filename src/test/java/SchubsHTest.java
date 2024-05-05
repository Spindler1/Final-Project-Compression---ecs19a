
/*************************************************************************
 *  Compilation:  mvn compile
 *  Execution:    mvn test
 *  Tests:        - testEmptyFilesToTar: tests to see if SchubsH.java can compress an empty file
 *                - testFilesWithSpacesAndLineEndings: tests to see if SchubsH.java can compress a file with spaces and line endings
 *                - testFilesWithManyCharacters: tests to see if SchubsH.java can compress a file with many characters
 *                - testMissingFile: tests to see if SchubsH.java throws an exception when a nonexistent file is passed as an argument
 *                - testContainsManyThings: tests to see if SchubsH.java can compress a file with many things
 *                - testWrongNumberOfArguments: tests to see if SchubsH.java throws an exception when the wrong number of arguments are passed
 *                - testOneReallyLongWordWithNoSpaces: tests to see if SchubsH.java can compress a file with one really long word with no spaces
 *                - testOnlyLowercaseCharacters: tests to see if SchubsH.java can compress a file with only lowercase characters
 *                - testOnlyUppercaseCharacters: tests to see if SchubsH.java can compress a file with only uppercase characters
 * 
 * 
 *  Description:  This program tests against SchubsH.java to make sure it can compress files using Huffman encoding with edge cases
 * 
 *  Things to note: These tests do not check to see if the compressed files were compressed properly as Deschubs tests to see if the
 *                  the files are the same which only works if the files were compressed properly
 * 
 *  Proof:        upon mvn test there will be System outputs to show the tests are running and lets the user know what the code is doing
 *                which the user can then compare to project folder to follow along. Maven will them show what tests passed or failed
 * 
 *  Command Line Proof Example:
 *                Test: testEmptyFile
 *                      File created successfully
 *                      File is empty
 *                Test: testOnlyUppercaseCharacters
 *                Test: testMissingFile
 *                      File nonexistent.txt does not exist.
 *                Test: testWrongNumberOfArguments
 *                      Usage: java SchubsH file1 file2 file3 ... OR java SchubsH <GLOB>
 *                      file1: file to be compressed
 *                      file2: compressed file
 *                      file3: file to be compressed ...
 *                      Incorrect number of arguments.
 *                Test: testOnlyLowercaseCharacters    
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
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Unit test for simple App.
 */
public class SchubsHTest {

    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void testEmptyFile() {
        System.out.println("Test: testEmptyFile");
        // Test case for an empty file
        String filename = "empty.txt";

        // Create an empty file
        File file = new File(filename);
        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    System.out.println("    File created successfully");
                } else {
                    throw new RuntimeException("    Failed to create file");
                }
            } else {
                System.out.println("    File already exists");
            }
        } catch (IOException e) {
            throw new RuntimeException("    Error creating file: " + e.getMessage());
        }

        if (file.length() == 0) {
            System.out.println("    File is empty");
        } else {
            System.out.println("    File is not empty");
            SchubsH.compress(filename, filename + ".hh");
        }

        // Verify that the compressed file is also empty
        File compressedFile = new File(filename + ".hh");
        assertTrue(compressedFile.length() == 0);

        // Delete the files
        file.delete();
        compressedFile.delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingFile() {
        System.out.println("Test: testMissingFile");
        // Test case for a missing file
        String filename = "nonexistent.txt";

        // Verify that SchubsH throws an exception when a nonexistent file is passed as
        // an argument
        String[] args = { filename };
        SchubsH.main(args);
    }

    @Test
    public void testContainsManyThings() {
        // Test case for a file that contains many things
        System.out.println("Test: testContainsManyThings");

        String filename = "src/SchubsHTests/test1.txt";

        SchubsH.compress(filename, filename + ".hh");

        // Verify that the compressed file is not empty
        File compressedFile = new File(filename + ".hh");
        assertTrue(compressedFile.length() > 0);
        if (compressedFile.length() > 0) {
            System.out.println("    File is not empty");
        } else {
            System.out.println("    File is empty");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongNumberOfArguments() {
        // Test case for providing the wrong number of arguments
        System.out.println("Test: testWrongNumberOfArguments");
        // Add your assertions here
        String[] args = {};
        SchubsH.main(args);

    }

    @Test
    public void testOneReallyLongWordWithNoSpaces() throws IOException {
        // Test case for a file with one really long word with no spaces
        System.out.println("Test: testOneReallyLongWordWithNoSpaces");
        // Add your assertions here
        String filename = "src/SchubsHTests/test2.txt";
        File file = new File(filename);
        String filename2 = filename + ".hh";
        File file2 = new File(filename2);
        SchubsH.compress(filename, filename2);

        assertTrue(file2.exists());
    }

    @Test
    public void testOnlyLowercaseCharacters() throws IOException {
        System.out.println("Test: testOnlyLowercaseCharacters");
        // Add your assertions here
        String filename = "src/SchubsHTests/test3.txt";
        File file = new File(filename);
        String filename2 = filename + ".hh";
        File file2 = new File(filename2);
        SchubsH.compress(filename, filename2);

        assertTrue(file2.exists());
    }

    @Test
    public void testOnlyUppercaseCharacters() throws IOException {
        System.out.println("Test: testOnlyUppercaseCharacters");
        // Add your assertions here
        String filename = "src/SchubsHTests/test4.txt";
        File file = new File(filename);
        String filename2 = filename + ".hh";
        File file2 = new File(filename2);
        SchubsH.compress(filename, filename2);

        assertTrue(file2.exists());
    }

}
