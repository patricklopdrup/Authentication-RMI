package printer;

import java.util.ArrayDeque;

public class Printer {
    private ArrayDeque<String> _printQueue;

    public Printer() {
        _printQueue = new ArrayDeque<String>();
    }

    public void addToQueue(String filename) {
        _printQueue.add(filename);
    }

    public void addToQueue(String[] filenames) {
        for (String filename : filenames) {
            addToQueue(filename);
        }
    }

    public void printQueue() {
        int _jobIdCount = 0;
        for (String filename : _printQueue) {
            System.out.println(_jobIdCount + " " + filename);
            _jobIdCount++;
        }
    }

    public void moveJobToTopOfQueue(int jobId) {
        String filename = getFilenameFromQueueById(jobId);
        if (!filename.isEmpty()) {
            _printQueue.remove(filename);
            _printQueue.addFirst(filename);
        }
    }

    public String getTopOfQueue() {
        return _printQueue.peek();
    }


    private String getFilenameFromQueueById(int jobId) {
        int _jobIdCount = 0;
        for (String filename : _printQueue) {
            if (_jobIdCount == jobId) {
                return filename;
            }
            _jobIdCount++;
        }
        return "";
    }

}
