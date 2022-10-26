package server;

import com.company.Main;
import printer.Printer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
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
    public IPrintServer start() throws RemoteException {
        String[] _args = { "start" };
        Main.main(_args);
        try {
            IPrintServer printServer = (IPrintServer) Naming.lookup("rmi://localhost:5099/getPrinterServer");
            System.out.println("PrintServer is started!");
            return printServer;
        } catch (Exception e) {
            System.out.println("Print server could not be started: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void stop() throws RemoteException {
        String[] _args = { "stop" };
        Main.main(_args);
        System.out.println("PrintServer stopping...");
    }

    @Override
    public void restart() throws RemoteException {
        stop();
        System.out.println("Clearing Print Queue...");
        _printers.clear();
        start();
    }


    @Override
    public void status(String printer) {
        Printer _printer = _printers.get(printer);
        _printer.printStatus();
    }

    @Override
    public void readConfig(String parameter) {

    }

    @Override
    public void setConfig(String parameter, String value) {

    }
}
