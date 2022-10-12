package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPrintServer extends Remote {
    public void print(String filename, String printer) throws RemoteException;
    public void queue(String printer) throws RemoteException;
    public void topQueue(String printer, int job);
    public void start();
    public void stop();
    public void restart();
    public void status(String printer);
    public void readConfig(String parameter);
    public void setConfig(String parameter, String value);
}
