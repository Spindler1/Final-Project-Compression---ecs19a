
/*************************************************************************
 *  Compilation:  mvn compile
 *  Execution:    mvn test
 *  Tests:        - testEmptyFilesToTar: tests when the file(s) to be Tars'd are empty
 *                - testNonExistentFilesToTar: tests when the file(s) to be Tars'd do not exist
 *                - testDestinationArchiveExists: tests when the destination archive already exists
 *                - testDirectoryAsFileToTar: tests when one of the files to be Tars'd is instead a directory and should therefore fail
 *                - testFilesWithManyCharacters: tests when the file(s) to be Tars'd contain many characters
 *                - testFilesWithSpacesAndLineEndings: tests when the file(s) to be Tars'd contain characters such as spaces and line
 *                - testCombinationOfFiles: tests any combination of the files to be Tars'd containing spaces, line
 *                - testWrongNumberOfArguments: tests when the user passes in the wrong amount of arguments
 * 
 * 
 *  Description:  This program tests against SchubsArc.java to make sure it can archive files after compressing them with Huffman encoding
 * 
 *  Things to note: These tests do not check to see if the archived files were archived properly as Deschubs tests to see if the
 *                  the files are the same which only works if the files were archived properly
 * 
 *  Proof:        upon mvn test there will be System outputs to show the tests are running and lets the user know what the code is doing
 *                which the user can then compare to project folder to follow along. Maven will them show what tests passed or failed
 * 
 *  Command Line Proof Example:
 *                Test: testEmptyFilesToTar
 *                      File exists
 *                      File is empty
 *                      File src/SchubsArcTest/empty.txt is empty.
 *                      File src/SchubsArcTest/empty.txt.hh does not exist or is not a regular file.
 *                      Archive src/SchubsArcTest\SchubsArcTest.zh created successfully.
 *                      Archive Test SHOULD pass
 *               Test: testFilesWithSpacesAndLineEndings
 *                      File src/SchubsArcTest/blue.txt compressed successfully.
 *                      src/SchubsArcTest/blue.txt.hh archived successfully.
 *                      Archive src/SchubsArcTest\SchubsArcTest.zh created successfully.
 *                      Archive Test SHOULD pass
 *              Test: testFilesWithManyCharacters
 *                      File src/SchubsArcTest/blue.txt compressed successfully.
 *                      src/SchubsArcTest/blue.txt.hh archived successfully.
 *                      Archive src/SchubsArcTest\SchubsArcTest.zh created successfully.
 *                      Archive Test SHOULD pass    
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

public class SchubsArcTest {

    @Test
    public void testEmptyFilesToTar() throws IOException {
        // Test when the file(s) to be Tars'd are empty
        System.out.println("Test: testEmptyFilesToTar");
        // Add your test code here
        String filename = "src/SchubsArcTest/empty.txt";
        String archiveName = "src/SchubsArcTest";
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
                System.out.println("    File exists");
            }
        } catch (IOException e) {
            throw new RuntimeException("    Error creating file: " + e.getMessage());
        }

        if (file.length() == 0) {
            System.out.println("    File is empty");
            SchubsArc.main(new String[] { archiveName, filename });
        } else {
            System.out.println("    File is not empty");
            SchubsArc.main(new String[] { archiveName, filename });
        }

        // Verify that the compressed file is exists
        String archiveResults = "src/SchubsArcTest/SchubsArcTest.zh";
        File compressedFile = new File(archiveResults);
        assertTrue(compressedFile.exists());
        if (compressedFile.exists()) {
            System.out.println("    Archive Test SHOULD pass");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonExistentFilesToTar() throws IOException {
        // Test when the file(s) to be Tars'd do not exist
        System.out.println("Test: testNonExistentFilesToTar");
        // Add your test code here
        String filename = "src/nonExistent.txt";
        String archiveName = "src/SchubsArcTest";
        SchubsArc.main(new String[] { archiveName, filename });
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("    File confirmed does not exist - Exception should be thrown");
        }
    }

    @Test
    public void testDestinationArchiveExists() throws IOException {
        // Test when the destination archive already exists
        System.out.println("Test: testDestinationArchiveExists");
        System.out.println(
                "    This test is assuming the Rule Reeves put in place: If the archive already exists, it will be overwritten");
        // Add your test code here
        String filename = "src/SchubsArcTest/blee.txt";
        String archiveName = "src/SchubsArcTest";
        File file = new File(archiveName + File.separator + "SchubsArcTest.zh");
        SchubsArc.main(new String[] { archiveName, filename });
        assertTrue(file.exists());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDirectoryAsFileToTar() throws IOException {
        // Test when one of the files to be Tars'd is instead a directory and should
        // therefore fail
        System.out.println("Test: testDirectoryAsFileToTar");
        // Add your test code here
        String filename = "src/SchubsArcTest";
        File folder = new File(filename);
        String archiveName = "src";
        if (folder.isDirectory()) {
            System.out.println("    File is a directory - Exception thrown");
        } else {
            System.out.println("    File is not a directory");
        }
        SchubsArc.main(new String[] { archiveName, filename });
    }

    @Test
    public void testFilesWithManyCharacters() throws IOException {
        // Test when the file(s) to be Tars'd contain many characters
        System.out.println("Test: testFilesWithManyCharacters");
        // Add your test code here
        String filename = "src/SchubsArcTest/blue.txt";
        String archiveName = "src/SchubsArcTest";

        SchubsArc.main(new String[] { archiveName, filename });

        // Verify that the compressed file is exists
        String archiveResults = "src/SchubsArcTest/SchubsArcTest.zh";
        File compressedFile = new File(archiveResults);
        assertTrue(compressedFile.exists());
        if (compressedFile.exists()) {
            System.out.println("    Archive Test SHOULD pass");
        }
    }

    @Test
    public void testFilesWithSpacesAndLineEndings() throws IOException {
        // Test when the file(s) to be Tars'd contain characters such as spaces and line
        System.out.println("Test: testFilesWithSpacesAndLineEndings");
        // Add your test code here
        String filename = "src/SchubsArcTest/blue.txt";
        String archiveName = "src/SchubsArcTest";

        SchubsArc.main(new String[] { archiveName, filename });

        // Verify that the compressed file is exists
        String archiveResults = "src/SchubsArcTest/SchubsArcTest.zh";
        File compressedFile = new File(archiveResults);
        assertTrue(compressedFile.exists());
        if (compressedFile.exists()) {
            System.out.println("    Archive Test SHOULD pass");
        }
    }

    @Test
    public void testCombinationOfFiles() throws IOException {
        // Test any combination of the files to be Tars'd containing spaces, line
        System.out.println("Test: testCombinationOfFiles");
        // endings, being empty, not existing, being directories, containing many
        // characters, or containing few characters.
        // Add your test code here
        // Add your test code here
        String filename1 = "src/SchubsArcTest/blah.txt";
        String filename2 = "src/SchubsArcTest/blue.txt";
        String filename3 = "src/SchubsArcTest/blee.txt";
        String archiveName = "src/SchubsArcTest";

        SchubsArc.main(new String[] { archiveName, filename1, filename2, filename3 });

        // Verify that the compressed file is exists
        String archiveResults = "src/SchubsArcTest/SchubsArcTest.zh";
        File compressedFile = new File(archiveResults);
        assertTrue(compressedFile.exists());
        if (compressedFile.exists()) {
            System.out.println("    Archive Test SHOULD pass");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongNumberOfArguments() throws IOException {
        // Test when the user passes in the wrong amount of arguments
        System.out.println("Test: testWrongNumberOfArguments");
        // Add your test code here
        String[] args = { "src/SchubsArcTest" };
        SchubsArc.main(args);

    }
}
