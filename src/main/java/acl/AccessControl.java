package acl;

import org.mindrot.jbcrypt.BCrypt;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

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
                // System.out.println(line);
                String[] parts = line.split(":");
                String[] roles = parts[1].split(",");
                accessControlList.put(parts[0], roles);
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
        String[] stringValues = (String[])accessControlList.get(username);
        return Arrays.asList(stringValues).contains(service);
    }
}
