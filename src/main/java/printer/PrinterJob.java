package printer;

public class PrinterJob {
    private String filename;

    public PrinterJob(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return filename;
    }

}
