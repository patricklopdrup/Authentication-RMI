package server;

import accessControl.IAccessControl;
import com.company.Main;
import printer.Printer;
import session.Session;
import accessControl.RoleBasedAccessControl;

import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.HashMap;

public class PrintServer extends UnicastRemoteObject implements IPrintServer {
    private HashMap<String, Printer> _printers = new HashMap<String, Printer>();
    private HashMap<String, String> _config = new HashMap<>();
    private IAccessControl _rbac = new RoleBasedAccessControl();

    private Session session;
    public PrintServer() throws RemoteException {
        super();
        session = new Session();
    }


    public void checkAccessAndValidateSession(String username, String method, String[] entities) throws RemoteException  {
        Date currentDateTime = new Date();
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("src/main/java/server/logfile.txt", true)));
        } catch (Exception e) {
            System.out.println("Logfile not found");
        }

        writer.println(username + ": " + method);
        writer.println(currentDateTime);
        for(String entity: entities){
            writer.println(entity);
        }

        if (!method.matches("Attempted login|Successfully logged in|Starting the server|Invalid Credentials") ) {
            if (!session.checkSessionForUser(username)) {
                writer.println("Invalid/Non-Existent Session");
                throw new RemoteException("Invalid/Non-Existent Session");
            }
            if (!_rbac.userHasAccess(username, method)) {
                throw new RemoteException("Permission Error");
            }
        }

        writer.println("========**========");
        writer.close();
    }
    @Override
    public Date login(String username, String password) throws RemoteException {
        Date sessionToken = session.login(username,password);
        if(sessionToken == null){
            checkAccessAndValidateSession(username, "Invalid Credentials", new String[]{});
            throw new RemoteException("Invalid Credentials");
        }
        System.out.println("Successfully Logged In");

        return sessionToken;
    }

    public boolean checkSession(String username) throws RemoteException{
        return session.checkSessionForUser(username);
    }

    @Override
    public void logout(String username) throws RemoteException {
        System.out.println(username + " logging out...");
    }

    @Override
    public void print(String username, String filename, String printer) throws RemoteException {
        checkAccessAndValidateSession(username, "print", new String[]{filename,printer});
        Printer _printer = _printers.get(printer);

        if (_printer != null) {
            _printer.addToQueue(filename);
        } else {
            Printer _newPrinter = addNewPrinter(username, printer);
            _newPrinter.addToQueue(filename);
        }
    }

    private Printer addNewPrinter(String username, String printer) {

        Printer _printerObj = new Printer();
        _printers.put(printer, _printerObj);
        return _printerObj;
    }

    @Override
    public void queue(String username, String printer) throws RemoteException {
        checkAccessAndValidateSession(username, "queue", new String[]{printer});

        Printer _printer = _printers.get(printer);

        if (_printer != null) {
            _printer.printQueue();
        }
    }

    @Override
    public void topQueue(String username, String printer, int job) throws RemoteException {
        checkAccessAndValidateSession(username, "topQueue", new String[]{printer,String.valueOf(job)});

        Printer _printer = _printers.get(printer);

        if (_printer != null) {
            _printer.moveJobToTopOfQueue(job);
        }
    }

    @Override
    public IPrintServer start(String username) throws RemoteException {
        checkAccessAndValidateSession(username, "start", new String[]{});

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
    public void stop(String username) throws RemoteException {
        checkAccessAndValidateSession(username, "stop", new String[]{});

        String[] _args = { "stop" };
        Main.main(_args);
        System.out.println("PrintServer stopping...");
    }

    @Override
    public void restart(String username) throws RemoteException {
        checkAccessAndValidateSession(username, "restart", new String[]{});
        stop(username);
        System.out.println("Clearing Print Queue...");
        _printers.clear();
        start(username);
    }


    @Override
    public void status(String username,String printer) throws RemoteException {
        checkAccessAndValidateSession(username, "status", new String[]{printer});

        Printer _printer = _printers.get(printer);
        _printer.printStatus();
    }

    @Override
    public void readConfig(String username, String parameter) throws RemoteException {
        checkAccessAndValidateSession(username, "readConfig", new String[]{parameter});

        String value = _config.getOrDefault(parameter, null);
        if (value == null)
            System.out.println("Parameter not found.");
        else
            System.out.println(parameter + ": " + value);
    }

    @Override
    public void setConfig(String username, String parameter, String value) throws RemoteException {
        checkAccessAndValidateSession(username, "setConfig", new String[]{parameter, value});
        _config.put(parameter, value);
    }

}
