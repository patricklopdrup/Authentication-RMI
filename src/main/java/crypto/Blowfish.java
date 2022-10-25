package crypto;

import org.mindrot.jbcrypt.BCrypt;

public class Blowfish implements ICrypto {
    public String getHash(String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        return hashed;
    }

    public boolean isPasswordCorrect(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

    public boolean isPasswordCorrectForUser(String username, String password) {
        // Get hash from DB/file by username
        String hash = "";
        return isPasswordCorrect(password, hash);
    }
}
