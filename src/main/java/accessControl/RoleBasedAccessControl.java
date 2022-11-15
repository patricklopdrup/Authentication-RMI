package accessControl;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class RoleBasedAccessControl implements IAccessControl {

    @Override
    public boolean userHasAccess(String username, String rule) {
        return false;
    }

    public void test() {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("C:\\dev\\Authentication\\src\\main\\java\\accessControl\\rbac.json")) {
            JSONObject obj = (JSONObject) jsonParser.parse(reader);
            JSONArray roles = (JSONArray) obj.get("policies");

            roles.forEach(mem -> System.out.println(mem));

//            System.out.println(obj);
//
//            JSONArray roles = new JSONArray();
//            roles.add(obj);
//            role
//            roles.forEach(role -> System.out.println(role));
//
//            System.out.println(roles);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
