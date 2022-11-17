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

    @Override
    public boolean userHasAccess(String username, String rule) {
        try {
            Policy policyForUser = getPolicyForUser(username);
            return isRuleInPolicy(policyForUser, rule);
        } catch (Exception e) {
            return false;
        }
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

    private Policy getLowestPrivilege() {
        return Policy.PolicyUser;
    }

    private Policy convertPolicyToEnum(String policy) {
        for (Policy p : Policy.values()) {
            if (p.toString().equals(policy))
                return p;
        }
        return getLowestPrivilege();
    }

    public boolean isRuleInPolicy(Policy policy, String rule) throws IOException, ParseException {
        Set<String> roles = getRulesForPolicy(policy);
        return roles.contains(rule);
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

    private ArrayList<Policy> getAllUnderlyingPoliciesForPolicy(Policy policy) {
        ArrayList<Policy> result = new ArrayList<>();
        result.add(policy);
        Policy[] curUnderLyingPolicies = getHierarchyForPolicy(policy);
        if (curUnderLyingPolicies != null) {
            result.addAll(Arrays.asList(curUnderLyingPolicies));
        }

        return result;
    }

    private JSONObject getJsonObject() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("src\\main\\java\\accessControl\\rbac.json");
        JSONObject obj = (JSONObject) jsonParser.parse(reader);
        return obj;
    }
}
