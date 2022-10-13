package printer;

import java.util.ArrayDeque;
import java.util.Queue;

public class Printer {
    private Queue<PrinterJob> _printQueue;
    private int _currentJobId = 0;

    public Printer() {
        _printQueue = new ArrayDeque<PrinterJob>();
    }

    public void addToQueue(String filename) {
        PrinterJob _printerJob = new PrinterJob(filename, _currentJobId);
        _currentJobId++;
        _printQueue.add(_printerJob);
    }

    public void printQueue() {
        for (PrinterJob job : _printQueue) {
            System.out.println(job.toString());
        }
    }

    public int getCurrentJobId() {
        return _currentJobId;
    }

}
