package accessControl;

public interface IAccessControl {
    boolean userHasAccess(String username, String rule);
}
