package crypto;

import org.mindrot.jbcrypt.BCrypt;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Blowfish implements ICrypto {
    public HashMap getCredentials(){
        BufferedReader reader;
        HashMap credentials = new HashMap<String, String>();

        try {
            reader = new BufferedReader(new FileReader(
                    "src/main/java/crypto/credentials.txt"));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                String[] parts = line.split(":");
                credentials.put(parts[0],parts[1]);
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Printing hashmap");

        for (Object objectName : credentials.keySet()) {
            System.out.println(objectName);
            System.out.println(credentials.get(objectName));
        }
        return credentials;
    }
    public String getHash(String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        return hashed;
    }

    public boolean isPasswordCorrect(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

    public boolean isPasswordCorrectForUser(String username, String password) {
        // Get hash from DB/file by username
//        credentials = getCredentials();
        HashMap credentials = getCredentials();
        String hash = (String) credentials.get(username);
        return isPasswordCorrect(password, hash);
    }
}
