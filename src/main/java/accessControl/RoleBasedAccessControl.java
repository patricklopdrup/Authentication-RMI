package accessControl;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RoleBasedAccessControl implements IAccessControl {
    public enum Policy {
        PolicyAdmin,
        PolicyTechnician,
        PolicyPowerUser,
        PolicyUser
    }

    private Policy[] getHierarchyForPolicy(Policy policy) {
        switch (policy) {
            case PolicyAdmin:
                return new Policy[] { Policy.PolicyTechnician, Policy.PolicyPowerUser, Policy.PolicyUser };
            case PolicyTechnician:
                return new Policy[] { Policy.PolicyUser };
            case PolicyPowerUser:
                return new Policy[] { Policy.PolicyUser };
            case PolicyUser:
                return null;
        }
        return null;
    }

    private ArrayList<Policy> getAllUnderlyingPoliciesForPolicy(Policy policy) {
        ArrayList<Policy> result = new ArrayList<>();
        result.add(policy);
        Policy[] curUnderLyingPolicies = getHierarchyForPolicy(policy);
        result.addAll(Arrays.asList(curUnderLyingPolicies));

        return result;
    }

    private Policy getLowestPrivilege() {
        return Policy.PolicyUser;
    }

    @Override
    public boolean userHasAccess(String username, String rule) throws IOException, ParseException {
        Policy policyForUser = getPolicyForUser(username);
        return isRuleInPolicy(policyForUser, rule);
    }

    public boolean isRuleInPolicy(Policy policy, String rule) throws IOException, ParseException {
        Set<String> roles = getRulesForPolicy(policy);
        return roles.contains(rule);
    }

    private JSONObject getJsonObject() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("src\\main\\java\\accessControl\\rbac.json");
        JSONObject obj = (JSONObject) jsonParser.parse(reader);
        return obj;
    }

    public Policy getPolicyForUser(String username) throws IOException, ParseException {
        JSONObject json = getJsonObject();
        JSONArray roles = (JSONArray) json.get("roles");
        for (Object role : roles) {
            JSONArray members = (JSONArray) ((JSONObject)role).get("members");
            for (Object member : members) {
                if (member.toString().equals(username)) {
                    String policy = ((JSONObject)role).get("policy").toString();
                    return convertPolicyToEnum(policy);
                }
            }
        }
        return getLowestPrivilege();
    }

    public Set<String> getRulesForPolicy(Policy policy) throws IOException, ParseException {
        Set<String> result = new HashSet<String>() {};
        ArrayList<Policy> allUnderlyingPolicies = getAllUnderlyingPoliciesForPolicy(policy);
        JSONObject json = getJsonObject();
        JSONArray policies = (JSONArray) json.get("policies");
        for (Policy p : allUnderlyingPolicies) {
            for (Object pol : policies) {
                String jsonPolicyName = ((JSONObject)pol).get("name").toString();
                if (p.toString().equals(jsonPolicyName)) {
                    JSONArray rules = (JSONArray) ((JSONObject)pol).get("rules");
                    for (Object rule : rules) {
                        result.add(rule.toString());
                    }
                }
            }
        }

        return result;
    }


    private Policy convertPolicyToEnum(String policy) {
        for (Policy p : Policy.values()) {
            if (p.toString().equals(policy))
                return p;
        }
        return getLowestPrivilege();
    }

    public void test() {

//            JSONArray roles = (JSONArray) obj.get("policies");
//
//            for (Object x : roles) {
//                System.out.println(((JSONObject)x).get("name"));
//            }
//            roles.forEach(mem -> System.out.println(mem));

//            System.out.println(obj);
//
//            JSONArray roles = new JSONArray();
//            roles.add(obj);
//            role
//            roles.forEach(role -> System.out.println(role));
//
//            System.out.println(roles);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
    }

}
