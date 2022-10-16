package server;

import printer.Printer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class PrintServer extends UnicastRemoteObject implements IPrintServer {
    private HashMap<String, Printer> _printers = new HashMap<String, Printer>();

    public PrintServer() throws RemoteException {
        super();
    }

    @Override
    public void print(String filename, String printer) throws RemoteException {
        Printer _printer = _printers.get(printer);

        if (_printer != null) {
            _printer.addToQueue(filename);
        } else {
            Printer _newPrinter = addNewPrinter(printer);
            _newPrinter.addToQueue(filename);
        }
    }

    private Printer addNewPrinter(String printer) {
        Printer _printerObj = new Printer();
        _printers.put(printer, _printerObj);
        return _printerObj;
    }

    @Override
    public void queue(String printer) throws RemoteException {
        Printer _printer = _printers.get(printer);

        if (_printer != null) {
            _printer.printQueue();
        }
    }

    @Override
    public void topQueue(String printer, int job) throws RemoteException {
        Printer _printer = _printers.get(printer);

        if (_printer != null) {
            _printer.moveJobToTopOfQueue(job);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void restart() {

    }

    @Override
    public void status(String printer) {

    }

    @Override
    public void readConfig(String parameter) {

    }

    @Override
    public void setConfig(String parameter, String value) {

    }
}
