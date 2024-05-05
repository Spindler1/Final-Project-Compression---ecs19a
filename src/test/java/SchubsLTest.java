
/*************************************************************************
 *  Compilation:  mvn compile
 *  Execution:    mvn test
 *  Tests:        - testCompressMissingFile: tests for handling a missing file
 *                - testCompressManyThings: tests for compressing a file containing many things
 *                - testWrongNumberOfArguments: tests for handling wrong number of arguments
 *                - testCompressLongWord: tests for compressing a file containing one really long word with no spaces
 *                - testCompressEmptyFile: tests for compressing an empty file
 *                - testCompressLowercase: tests for compressing a file containing only lowercase characters
 *                - testCompressUppercase: tests for compressing a file containing only uppercase characters
 * 
 * 
 * 
 *  Description:  This program tests against SchubsL.java to make sure it can compress files using LZW encoding with edge cases
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
 *                      Usage: java SchubsL file1 file2 file3 ... OR java SchubsL <GLOB>
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
import static org.junit.Assert.*;

import org.junit.Test;
import java.io.*;

public class SchubsLTest {

    // Test for handling a missing file
    @Test(expected = FileNotFoundException.class)
    public void testCompressMissingFile() throws IOException {
        System.out.println("Test: testMissingFile");
        String filename = "missingFile.txt"; // This file should not exist
        SchubsL.compress(filename, filename + ".lzw");
    }

    // Test for compressing a file containing many things
    @Test
    public void testCompressManyThings() throws IOException {
        String filename = "manyThings.txt";
        createFileWithContents(filename, "This file contains many things!");

        SchubsL.compress(filename, filename + ".lzw");
        File compressedFile = new File(filename + ".lzw");
        assertTrue(compressedFile.exists() && compressedFile.length() > 0);

        // Clean up
        compressedFile.delete();
        new File(filename).delete();
    }

    // Test for handling wrong number of arguments
    @Test(expected = IllegalArgumentException.class)
    public void testWrongNumberOfArguments() throws IOException {
        System.out.println("Test: testWrongNumberOfArguments");
        String[] args = {};
        SchubsL.main(args);
    }

    // Test for compressing a file containing one really long word with no spaces
    @Test
    public void testCompressLongWord() throws IOException {
        System.out.println("Test: testCompressLongWord");
        String filename = "longWord.txt";
        createFileWithContents(filename, "supercalifragilisticexpialidocious");

        SchubsL.compress(filename, filename + ".lzw");

        File compressedFile = new File(filename + ".lzw");
        assertTrue(compressedFile.exists() && compressedFile.length() > 0);

        // Clean up
        compressedFile.delete();
        new File(filename).delete();
    }

    // Test for compressing an empty file
    @Test
    public void testCompressEmptyFile() throws IOException {
        System.out.println("Test: testEmptyFile");
        String filename = "emptyFile.txt";
        File file = new File(filename);
        File compressedFile = new File(filename + ".lzw");
        assertTrue(compressedFile.length() == 0);

        // Clean up
        compressedFile.delete();
        new File(filename).delete();
    }

    // Test for compressing a file containing only lowercase characters
    @Test
    public void testCompressLowercase() throws IOException {
        System.out.println("Test: testOnlyLowercaseCharacters");
        String filename = "lowercase.txt";
        createFileWithContents(filename, "abcdefghijklmnopqrstuvwxyz");

        SchubsL.compress(filename, filename + ".lzw");

        File compressedFile = new File(filename + ".lzw");
        assertTrue(compressedFile.exists() && compressedFile.length() > 0);

        // Clean up
        compressedFile.delete();
        new File(filename).delete();
    }

    // Test for compressing a file containing only uppercase characters
    @Test
    public void testCompressUppercase() throws IOException {
        System.out.println("Test: testOnlyUppercaseCharacters");
        String filename = "uppercase.txt";
        createFileWithContents(filename, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");

        SchubsL.compress(filename, filename + ".lzw");

        File compressedFile = new File(filename + ".lzw");
        assertTrue(compressedFile.exists() && compressedFile.length() > 0);

        // Clean up
        compressedFile.delete();
        new File(filename).delete();
    }

    // Helper method to create a file with specified contents
    private void createFileWithContents(String filename, String contents) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write(contents);
        writer.close();
    }
}
