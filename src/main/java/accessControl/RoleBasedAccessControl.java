package accessControl;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class RoleBasedAccessControl implements IAccessControl {
    public enum Policy {
        PolicyAdmin,
        PolicyTechnician,
        PolicyPowerUser,
        PolicyUser
    }

    @Override
    public boolean userHasAccess(String username, String rule) {
        try {
            Policy[] policiesForUser = getPoliciesForUser(username);
            return isRuleInPolicies(policiesForUser, rule);
        } catch (Exception e) {
            return false;
        }
    }

    public Policy[] getPoliciesForUser(String username) throws IOException, ParseException {
        List<Policy> policies = new ArrayList<>();
        JSONObject json = getJsonObject();
        JSONArray roles = (JSONArray) json.get("roles");
        for (Object role : roles) {
            JSONArray members = (JSONArray) ((JSONObject)role).get("members");
            for (Object member : members) {
                if (member.toString().equals(username)) {
                    JSONArray pols = (JSONArray) ((JSONObject)role).get("policies");
                    for (Object pol : pols) {
                        policies.add(convertPolicyToEnum(pol.toString()));
                    }
                }
            }
        }
        if (policies.size() == 0) {
            return new Policy[] { getLowestPrivilege() };
        } else {
            return policies.toArray(new Policy[policies.size()]);
        }
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

    public boolean isRuleInPolicies(Policy[] policies, String rule) throws IOException, ParseException {
        Set<String> roles = new HashSet<>();
        for (Policy pol : policies) {
            roles.addAll(getRulesForPolicy(pol));
        }
        return roles.contains(rule);
    }

    public Set<String> getRulesForPolicy(Policy policy) throws IOException, ParseException {
        Set<String> result = new HashSet<String>() {};
        JSONObject json = getJsonObject();
        JSONArray policies = (JSONArray) json.get("policies");
        for (Object pol : policies) {
            String jsonPolicyName = ((JSONObject)pol).get("name").toString();
            if (policy.toString().equals(jsonPolicyName)) {
                JSONArray rules = (JSONArray) ((JSONObject)pol).get("rules");
                for (Object rule : rules) {
                    result.add(rule.toString());
                }
            }
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
