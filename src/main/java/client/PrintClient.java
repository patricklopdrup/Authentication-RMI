package client;

import server.IPrintServer;
import server.PrintServer;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class PrintClient {
    public static void main(String[] args) throws RemoteException {
        IPrintServer printServer = new PrintServer();

        Scanner _scanner = new Scanner(System.in);
        boolean _isPrintServerStarted = false;
        boolean shouldExit = false;
        Date _sessionToken = null;
        boolean _isLoggedIn = false;
        String _username = "";
        String _password = "";

        while (true) {
            System.out.println("\nLogin to use the Print server");
            System.out.print("Username: ");
            _username = _scanner.nextLine();
            System.out.print("Password: ");
            _password = _scanner.nextLine();

            try {
                _sessionToken = printServer.login(_username, _password);
            }
            catch (Exception e) {
                if (e.getMessage().contains("Invalid Credentials")) {
                    System.out.println("Invalid Credentials");
                } else if (e.getMessage().contains("Invalid/Non-Existent Session")) {
                    System.out.println("Invalid/Non-Existent Session");
                } else {
                    System.out.println("Error. Try again." + e.getMessage());
                }
                continue;
            }
            if (_sessionToken != null) {
                _isLoggedIn = true;
                break;
            }
        }

        while (true) {
            printOptions();
            System.out.print("> ");
            String _input = _scanner.nextLine();

            String[] _inputSplit = _input.toLowerCase().split(" ");
            String _type = _inputSplit[0];
            String[] _arg = Arrays.copyOfRange(_inputSplit, 1, _inputSplit.length);
            if (!isInputLegal(_isPrintServerStarted, _input)) {
                System.out.println("Print server is not started!");
                continue;
            }
            try {
                switch (_type) {
                    case "exit":
                        shouldExit = true;
                        System.out.println("Exiting program...");
                        break;
                    case "s":
                        printServer = printServer.start(_username);
                        _isPrintServerStarted = true;
                        break;
                    case "q":
                        printServer.stop(_username);
                        _isPrintServerStarted = false;
                        break;
                    case "r":
                        printServer.restart(_username);
                        break;
                    case "p":
                        printServer.print(_username, _arg[0], _arg[1]);
                        break;
                    case "pq":
                        printServer.queue(_username, _arg[0]);
                        break;
                    case "pt":
                        printServer.topQueue(_username, _arg[0], Integer.parseInt(_arg[1]));
                        break;
                    case "ps":
                        printServer.status(_username, _arg[0]);
                        break;
                    case "cr":
                        printServer.readConfig(_username, _arg[0]);
                        break;
                    case "cs":
                        printServer.setConfig(_username, _arg[0], _arg[1]);
                        break;
                }

                if (shouldExit)
                    break;
            } catch (Exception e) {
                if (e.getMessage().contains("Invalid Credentials")) {
                    System.out.println("Invalid Credentials");
                } else if (e.getMessage().contains("Invalid/Non-Existent Session")) {
                    System.out.println("Invalid/Non-Existent Session");
                }else if (e.getMessage().contains("Permission Error")) {
                    System.out.println(_username + " does not have permission to " + _type);
                } else {
                    System.out.println("Error. Try again." + e.getMessage());
                }
                continue;
            }
        }
    }

    private static boolean isInputLegal(boolean isPrintServerStarted, String input) throws RemoteException {
        String[] _inputSplit = input.split(" ");
        if (_inputSplit[0].equals("exit"))
            return true;
        return _inputSplit[0].equals("s") || isPrintServerStarted;
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
