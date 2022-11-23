package test;

import accessControl.RoleBasedAccessControl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RBACTest {

    RoleBasedAccessControl rbac = new RoleBasedAccessControl();

    @Test
    void get_policyForUserInRoleWithMultipleMembers_expectPolicyUser() {
        try {
            RoleBasedAccessControl.Policy[] policies = rbac.getPoliciesForUser("Erika");
            RoleBasedAccessControl.Policy[] expected = new RoleBasedAccessControl.Policy[] {
                    RoleBasedAccessControl.Policy.PolicyUser
            };
            assertArrayEquals(policies, expected);
        } catch (Exception e) { }
    }

    @Test
    void get_policyForUserInManagerRole() {
        try {
            RoleBasedAccessControl.Policy[] policies = rbac.getPoliciesForUser("Alice");
            RoleBasedAccessControl.Policy[] expected = new RoleBasedAccessControl.Policy[] {
                    RoleBasedAccessControl.Policy.PolicyAdmin
            };
            assertArrayEquals(policies, expected);
        } catch (Exception e) {}
    }

    @Test
    void get_policyForUserNotInSystem_expectLowestPrivileges() {
        try {
            RoleBasedAccessControl.Policy[] policies = rbac.getPoliciesForUser("ImNotInThisDomain");
            RoleBasedAccessControl.Policy[] expected = new RoleBasedAccessControl.Policy[] {
                    RoleBasedAccessControl.Policy.PolicyUser
            };
            assertArrayEquals(policies, expected);
        } catch (Exception e) {}
    }

    @Test
    void get_rulesForUserPolicy() {
        try {
            Set<String> roles = rbac.getRulesForPolicy(RoleBasedAccessControl.Policy.PolicyUser);
            ArrayList<String> expected = new ArrayList<>();
            expected.add("print");
            expected.add("queue");

            assertTrue(expected.size() == roles.size()
                    && expected.containsAll(roles)
                    && roles.containsAll(expected));
        } catch (Exception e) {}
    }

    @Test
    void get_rulesForTechnicianPolicy() {
        try {
            Set<String> roles = rbac.getRulesForPolicy(RoleBasedAccessControl.Policy.PolicyTechnician);
            ArrayList<String> expected = new ArrayList<>();
            expected.add("start");
            expected.add("stop");
            expected.add("restart");
            expected.add("status");
            expected.add("readConfig");
            expected.add("setConfig");

            assertTrue(expected.size() == roles.size()
                    && expected.containsAll(roles)
                    && roles.containsAll(expected));
        } catch (Exception e) {}
    }

    @Test
    void get_rulesForPowerUserPolicy() {
        try {
            Set<String> roles = rbac.getRulesForPolicy(RoleBasedAccessControl.Policy.PolicyPowerUser);
            ArrayList<String> expected = new ArrayList<>();
            expected.add("topQueue");
            expected.add("restart");
            // can also do normal user stuff
            expected.add("print");
            expected.add("queue");

            assertTrue(expected.size() == roles.size()
                    && expected.containsAll(roles)
                    && roles.containsAll(expected));
        } catch (Exception e) {}
    }

    @Test
    void get_rulesForAdminPolicy() {
        try {
            Set<String> roles = rbac.getRulesForPolicy(RoleBasedAccessControl.Policy.PolicyAdmin);
            ArrayList<String> expected = new ArrayList<>();
            // PowerUser stuff
            expected.add("topQueue");
            // Technician stuff
            expected.add("start");
            expected.add("stop");
            expected.add("restart");
            expected.add("status");
            expected.add("readConfig");
            expected.add("setConfig");
            // can also do normal user stuff
            expected.add("print");
            expected.add("queue");

            assertTrue(expected.size() == roles.size()
                    && expected.containsAll(roles)
                    && roles.containsAll(expected));

        } catch (Exception e) {}
    }

    @Test
    void check_ifUserCanStartServer_expectFalse() {
        try {
            RoleBasedAccessControl.Policy[] userPolicy = new RoleBasedAccessControl.Policy[] { RoleBasedAccessControl.Policy.PolicyUser };
            boolean canUserStartServer = rbac.isRuleInPolicies(userPolicy, "start");
            assertFalse(canUserStartServer);
        } catch (Exception e) {}
    }

    @Test
    void canNormalUserPrint_expectTrue() {
        try {
            assertTrue(rbac.userHasAccess("David", "print"));
        } catch (Exception e) {}
    }

    @Test
    void canAdminUserPrint_expectTrue() {
        try {
            assertTrue(rbac.userHasAccess("Alice", "print"));
        } catch (Exception e) {}
    }

    @Test
    void canTechnicianMakeTopQueue_expectFalse() {
        try {
            assertFalse(rbac.userHasAccess("Bob", "topQueue"));
        } catch (Exception e) {}
    }

    @Test
    void canTechnicianMakePrint_expectFalse() {
        try {
            assertFalse(rbac.userHasAccess("Bob", "print"));
        } catch (Exception e) {}
    }

    @Test
    void canPowerUserReadConfig_expectFalse() {
        try {
            assertFalse(rbac.userHasAccess("Cecilia", "readConfig"));
        } catch (Exception e) {}
    }
}
