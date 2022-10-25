package com.company;

import server.PrintServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    public static void main(String[] args) throws RemoteException {
        Registry registry = MyRegistry.getRegistry();

        if (args[0].equals("start")) {
            registry.rebind("getPrinterServer", new PrintServer());
        } else if (args[0].equals("stop")) {
            try {
                registry.unbind("getPrinterServer");
            } catch (NotBoundException e) {
                System.out.println("Already unbind!");
                e.printStackTrace();
            }
        }
    }

    private static class MyRegistry {
        private static Registry _registry;

        static {
            try {
                _registry = LocateRegistry.createRegistry(5099);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public static Registry getRegistry() {
            return _registry;
        }
    }

}
