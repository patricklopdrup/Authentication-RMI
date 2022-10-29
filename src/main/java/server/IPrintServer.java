package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface IPrintServer extends Remote {
    public Date login(String username, String password) throws RemoteException;
    public boolean checkSession(String username) throws RemoteException;
    public void print(String username, String filename, String printer) throws RemoteException;
    public void queue(String username, String printer) throws RemoteException;
    public void topQueue(String username, String printer, int job) throws RemoteException;
    public IPrintServer start(String username) throws RemoteException;
    public void stop(String username) throws RemoteException;
    public void restart(String username) throws RemoteException;
    public void status(String username, String printer) throws RemoteException;
    public void readConfig(String username, String parameter) throws RemoteException;
    public void setConfig(String username, String parameter, String value) throws RemoteException;
}
