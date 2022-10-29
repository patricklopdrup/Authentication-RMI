package client;

import server.IPrintServer;
import server.PrintServer;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Scanner;

public class PrintClient {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        IPrintServer printServer = new PrintServer();

        Scanner _scanner = new Scanner(System.in);
        boolean _isPrintServerStarted = false;
        boolean shouldExit = false;
        while (true) {
            printOptions();
            System.out.print("> ");
            String _input = _scanner.nextLine();

            String[] _inputSplit = _input.toLowerCase().split(" ");
            String _type = _inputSplit[0];
            String[] _arg = Arrays.copyOfRange(_inputSplit, 1, _inputSplit.length);

            if (!isInputLegal(_isPrintServerStarted, _input)) {
                System.out.println("Input not legal. Try again.");
                continue;
            }
            try {
                switch (_type) {
                    case "exit":
                        shouldExit = true;
                        System.out.println("Exiting program...");
                        break;
                    case "s":
                        printServer = printServer.start();
                        _isPrintServerStarted = true;
                        break;
                    case "q":
                        printServer.stop();
                        _isPrintServerStarted = false;
                        break;
                    case "r":
                        printServer.restart();
                        break;
                    case "p":
                        printServer.print(_arg[0], _arg[1]);
                        break;
                    case "pq":
                        printServer.queue(_arg[0]);
                        break;
                    case "PT":
                        printServer.topQueue(_arg[0], Integer.parseInt(_arg[1]));
                        break;
                    case "ps":
                        printServer.status(_arg[0]);
                        break;
                    case "cr":
                        printServer.readConfig(_arg[0]);
                        break;
                    case "cs":
                        printServer.setConfig(_arg[0], _arg[1]);
                        break;
                }

                if (shouldExit)
                    break;
            } catch (Exception e) {
                System.out.println("Error. Try again.");
                continue;
            }
        }
    }

    private static boolean isInputLegal(boolean isPrintServerStarted, String input) {
        String[] _inputSplit = input.split(" ");
        if (_inputSplit[0].equals("exit"))
            return true;
        if (!_inputSplit[0].equals("s") && !isPrintServerStarted) {
            System.out.println("Print server is not started!");
            return false;
        }
        return true;
    }

    private static void printOptions() {
        System.out.println(
                "\nOptions: EXIT to exit program.\n" +

                "> Print Service:\n" +
                "\tS: Start\tQ: Stop\t\tR: Restart\n" +

                "> Printing:\n" +
                "\tP <filename> <printer>\t:\tPrint new job\n" +
                "\tPQ <printer>\t\t\t:\tPrint queue of printer\n" +
                "\tPT <printer> <jobId>\t:\tSend job to top of queue\n" +
                "\tPS <printer>\t\t\t:\tPrint status of printer:\n" +

                "> Config:\n" +
                "\tCR <parameter>\t\t\t:\tRead the parameter from config\n" +
                "\tCS <parameter> <value>\t:\tSet the parameter in config\n"
        );
    }
}
