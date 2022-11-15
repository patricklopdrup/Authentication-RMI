package test;

import accessControl.RoleBasedAccessControl;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;

import java.io.FileReader;

public class RBACTest {
    @Test
    void test() {
        RoleBasedAccessControl test = new RoleBasedAccessControl();
        test.test();
    }
}
