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
            RoleBasedAccessControl.Policy policy = rbac.getPolicyForUser("Erika");
            assertEquals(policy, RoleBasedAccessControl.Policy.PolicyUser);
        } catch (Exception e) { }
    }

    @Test
    void get_policyForUserInManagerRole() {
        try {
            RoleBasedAccessControl.Policy policy = rbac.getPolicyForUser("Alice");
            assertEquals(policy, RoleBasedAccessControl.Policy.PolicyAdmin);
        } catch (Exception e) {}
    }

    @Test
    void get_policyForUserNotInSystem_expectLowestPrivileges() {
        try {
            RoleBasedAccessControl.Policy policy = rbac.getPolicyForUser("ImNotInThisDomain");
            assertEquals(policy, RoleBasedAccessControl.Policy.PolicyUser); // User == lowest privilege
        } catch (Exception e) {}
    }

    @Test
    void get_rulesForUserPolicy() {
        try {
            Set<String> roles = rbac.getRulesForPolicy(RoleBasedAccessControl.Policy.PolicyUser);
            ArrayList<String> expected = new ArrayList<>();
            expected.add("print");
            expected.add("queue");

            assertEquals(roles, expected);
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
            boolean canUserStartServer = rbac.isRuleInPolicy(RoleBasedAccessControl.Policy.PolicyUser, "start");
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
}
