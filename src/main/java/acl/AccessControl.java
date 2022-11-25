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
        return accessControlList;
    }

    public boolean checkUserAccess(String username, String service){
        ArrayList<String> services = (ArrayList<String>) accessControlList.get(username);
        return services.contains(service);
    }
}
