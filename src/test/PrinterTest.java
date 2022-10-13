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
    Printer printer;

    @BeforeEach
    public void setUpStreams() {
        printer = new Printer();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStream() {
        System.setOut(originalOut);
    }

    @Test
    void get_currentJobIdEqualOne_whenOneFileAdded() {
        String filename = "file1";
        printer.addToQueue(filename);
        assert(printer.getCurrentJobId() == 1);
    }

    @Test
    void print_filenameFromQueue_whenAdded() {
        String filename = "file1";
        printer.addToQueue(filename);
        printer.printQueue();
        assertEquals("1 file1\r\n", outContent.toString());
    }

    @Test
    void print_multipleFilenamesFromQueue_whenAdded() {
        String[] filenames = { "file1", "file2", "file3" };
        for (String filename: filenames) {
            printer.addToQueue(filename);
        }
        printer.printQueue();
        // Maybe fails if not run on Windows?
        String expected =
                "1 file1\r\n" +
                "2 file2\r\n" +
                "3 file3\r\n";
        assertEquals(expected, outContent.toString());
    }

}
