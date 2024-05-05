
/*************************************************************************
 *  Compilation:  mvn compile
 *  Execution:    mvn test
 *  Tests:        - checkHuffmanGlob: tests to see if SchubsH.java can compress multiple files as arguments
 *                - checkLZWGlob: tests to see if SchubsL.java can compress multiple files as arguments
 *                - CheckTarsGlob: tests to see if SchubsArc.java can archive multiple files as arguments
 * 
 *  Description:  This program tests against SchubsH.java|SchubsL.java|SchubsArc.java to make sure it works if given multiple files as arguments
 * 
 *  Things to note: There are few tests here because any edge cases other than multiple files as arguments are already tested in the
 *                  SchubsHTest.java|SchubsLTest.java|SchubsArcTest.java files
 * 
 *  Proof:        upon mvn test there will be System outputs to show the tests are running and lets the user know what the code is doing
 *                which the user can then compare to project folder to follow along. Maven will them show what tests passed or failed
 * 
 *  Command Line Proof Example:
 *                Test: checkTarsGlob
 *                      File src/GlobTests/globtest3.txt compressed successfully.
 *                          src/GlobTests/globtest3.txt.hh archived successfully.
 *                      File src/GlobTests/globtest1.txt compressed successfully.
 *                          src/GlobTests/globtest1.txt.hh archived successfully.
 *                      File src/GlobTests/globtest2.txt compressed successfully.
 *                          src/GlobTests/globtest2.txt.hh archived successfully.
 *                      Archive src/GlobTests/TarsGlob\TarsGlob.zh created successfully.         
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

public class GlobTest {
    @Test
    public void checkHuffmanGlob() throws IOException {
        System.out.println("Test: checkHuffmanGlob");
        // check if SchubsH can compress multiple arguments
        String[] args = new String[] { "src/GlobTests/globtest3.txt", "src/GlobTests/globtest1.txt",
                "src/GlobTests/globtest2.txt" };
        SchubsH.main(args);
        assertTrue(new File("src/GlobTests/globtest3.txt.hh").exists());
        assertTrue(new File("src/GlobTests/globtest1.txt.hh").exists());
        assertTrue(new File("src/GlobTests/globtest2.txt.hh").exists());
    }

    @Test
    public void checkLZWGlob() throws IOException {
        System.out.println("Test: checkLZWGlob");
        // check if SchubsL can compress multiple arguments
        String[] args = new String[] { "src/GlobTests/globtest3.txt", "src/GlobTests/globtest1.txt",
                "src/GlobTests/globtest2.txt" };
        SchubsL.main(args);
        assertTrue(new File("src/GlobTests/globtest3.txt.ll").exists());
        assertTrue(new File("src/GlobTests/globtest1.txt.ll").exists());
        assertTrue(new File("src/GlobTests/globtest2.txt.ll").exists());
    }

    @Test
    public void checkTarsGlob() throws IOException {
        System.out.println("Test: checkTarsGlob");
        // check if SchubsArc can compress multiple arguments
        String[] args = new String[] { "src/GlobTests/TarsGlob", "src/GlobTests/globtest3.txt",
                "src/GlobTests/globtest1.txt",
                "src/GlobTests/globtest2.txt" };
        SchubsArc.main(args);
        assertTrue(new File("src/GlobTests/TarsGlob/TarsGlob.zh").exists());
    }
}
