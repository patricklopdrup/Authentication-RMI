package client;

import server.IPrintServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class PrintClient {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        IPrintServer printServer = (IPrintServer) Naming.lookup("rmi://localhost:5099/test");
        printServer.print("hej", "test");
        printServer.queue("test");
    }
}
