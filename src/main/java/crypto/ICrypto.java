package crypto;

public interface ICrypto {
    String getHash(String password);
    boolean isPasswordCorrect(String password, String hash);
    boolean isPasswordCorrectForUser(String username, String password);

}
