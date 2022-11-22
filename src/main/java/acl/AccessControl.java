package acl;

import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AccessControl {
    public HashMap accessControlList;
    public AccessControl() {
        accessControlList = readAccessControlFile();
    }

    public HashMap readAccessControlFile(){
        BufferedReader reader;
        HashMap accessControlList = new HashMap<String, String[]>();

        try {
            reader = new BufferedReader(new FileReader(
                    "src/main/java/acl/AccessControlList.txt"));
            String line = reader.readLine();
            while (line != null) {
                ArrayList<String> parts = new ArrayList<String>(Arrays.asList(line.split(":")));
                ArrayList<String> roles = new ArrayList<String>(Arrays.asList(parts.get(1).split(",")));
                accessControlList.put(parts.get(0), roles);
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Printing hashmap");
        
        for (Object objectName : accessControlList.keySet()) {
        System.out.println(objectName);
        System.out.println(accessControlList.get(objectName));
        }
        return accessControlList;
    }

    public boolean checkUserAccess(String username, String service){
        ArrayList<String> services = (ArrayList<String>) accessControlList.get(username);
        return services.contains(service);
    }

    public String addServiceAccessForUser(String username, String service){
        ArrayList<String> services = (ArrayList<String>) accessControlList.get(username);
        if (services.contains(service)){
            return "User already has access to the service";
        }else{
            services.add(service);
            if(writeAccessValuesToFile()){
                return "Service added successfully for user";
            }
            return "Unable to write services to file";
        }
    }

    public String removeServiceForUser(String username, String service){
        ArrayList<String> services = (ArrayList<String>) accessControlList.get(username);
        if (services.contains(service)){
            services.remove(service);
            if(writeAccessValuesToFile()){
                return "The service has been removed for the user";
            }
            return "Unable to write services to file";
        }else{
            return "The user does not have any existing access to the service";
        }
    }
    public Boolean writeAccessValuesToFile(){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("src/main/java/acl/AccessControlList.txt")));
        } catch (Exception e) {
            System.out.println("Access-Control-List File not found");
            writer.close();
            return false;
        }
        for (Object objectName : accessControlList.keySet()) {
            String currentUsername = (String) objectName;
            ArrayList<String> services = (ArrayList<String>) accessControlList.get(objectName);
            String joinedServices = String.join(",", services);
            System.out.println(joinedServices);
            writer.println(currentUsername + ":" + joinedServices);
        }
        writer.close();
        return true;
    }
}
