package accessControl;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public interface IAccessControl {
    boolean userHasAccess(String username, String rule) throws IOException, ParseException;
}
