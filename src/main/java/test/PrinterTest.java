package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import printer.Printer;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PrinterTest {
    // To test System.out.println method
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private Printer _printer;

    @BeforeEach
    public void setUpStreams() {
        _printer = new Printer();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStream() {
        System.setOut(originalOut);
    }

    @Test
    void print_filenameFromQueue_whenAdded() {
        String filename = "file1";
        _printer.addToQueue(filename);
        _printer.printQueue();
        assertEquals("0 file1\r\n", outContent.toString());
    }

    @Test
    void print_multipleFilenamesFromQueue_whenAdded() {
        String[] filenames = { "file1", "file2", "file3" };
        _printer.addToQueue(filenames);
        _printer.printQueue();
        // Maybe fails if not run on Windows?
        String expected =
                "0 file1\r\n" +
                "1 file2\r\n" +
                "2 file3\r\n";
        assertEquals(expected, outContent.toString());
    }

    @Test
    void print_multipleFilenamesFromQueue_afterMoveToTop() {
        String[] filenames = { "file1", "file2", "file3" };
        _printer.addToQueue(filenames);
        int jobIdOfFile2 = 1;
        _printer.moveJobToTopOfQueue(jobIdOfFile2);
        _printer.printQueue();
        String expected =
                "0 file2\r\n" + // file2 in first place
                "1 file1\r\n" +
                "2 file3\r\n";
        assertEquals(expected, outContent.toString());
    }

    @Test
    void move_jobToTopInQueue() {
        String[] filenames = { "file1", "file2", "file3" };
        _printer.addToQueue(filenames);
        int jobIdOfFile2 = 1;
        _printer.moveJobToTopOfQueue(jobIdOfFile2);
        assertEquals("file2", _printer.getTopOfQueue());
    }

    @Test
    void print_statusOfPrinterWithMultipleJobs() {
        String[] filenames = { "file1", "file2", "file3" };
        _printer.addToQueue(filenames);
        _printer.printStatus();
        String content = outContent.toString();
        assert(
                content.contains("Printer has 3 job(s)")
                && content.contains("file1")
                && content.contains("file2")
                && content.contains("file3")
        );
    }

    @Test
    void print_statusOfPrinterWithNoJobs() {
        _printer.printStatus();
        String content = outContent.toString();
        assert(
                content.contains("Printer has 0 job(s)")
                && !content.contains("Printing the job(s) now:")
        );
    }

}
