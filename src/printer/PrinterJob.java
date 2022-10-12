package printer;

public class PrinterJob {
    private String filename;
    private int jobId;

    public PrinterJob(String filename, int jobId) {
        this.filename = filename;
        this.jobId = jobId;
    }

    @Override
    public String toString() {
        return jobId + " " + filename;
    }
}
