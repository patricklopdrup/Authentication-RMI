package test;

import crypto.Blowfish;
import crypto.ICrypto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class BlowfishTest {

    ICrypto blowfish;

    @BeforeEach
    void setUp() {
        blowfish = new Blowfish();
    }

    @Test
    void make_twoHashesFromSamePassword_getDifferentHashes() {

        String password = "Se@cu@re_!p!ass1*2)3";
        String hash1 = blowfish.getHash(password);
        String hash2 = blowfish.getHash(password);

        assertNotEquals(hash1, hash2);
    }

    @Test
    void check_hashFromPassword_expectTrue() {
        String password = "MyPass123";
        String hash = blowfish.getHash(password);

        assert(blowfish.isPasswordCorrect(password, hash));
    }
}
