package client;

import com.company.Main;
import server.IPrintServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Scanner;

public class PrintClient {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        IPrintServer printServer = (IPrintServer) Naming.lookup("rmi://localhost:5099/getPrinterServer");

        Scanner _scanner = new Scanner(System.in);
        boolean shouldExit = false;
        while (true) {
            printOptions();
            System.out.print("> ");
            String _input = _scanner.nextLine();
            if (!isInputLegal()) {
                System.out.println("Input not legal. Try again.");
                continue;
            }

            String[] _inputSplit = _input.toLowerCase().split(" ");
            String _type = _inputSplit[0];
            String[] _arg = Arrays.copyOfRange(_inputSplit, 1, _inputSplit.length);
            switch (_type) {
                case "exit":
                    shouldExit = true;
                    break;
                case "start":
                    Main main = new Main();
                    break;
                case "print":
                case "p":
                    printServer.print(_arg[0], _arg[1]);
                    break;
                case "queue":
                case "q":
                    printServer.queue(_arg[0]);
                    break;
            }

            if (shouldExit)
                break;
        }
    }

    private static boolean isInputLegal() {
        return true;
    }

    private static void printOptions() {
        System.out.println(
                "Options:\n" +
                "P <filename> <printer>\tQ <printer>"
        );
    }
}
