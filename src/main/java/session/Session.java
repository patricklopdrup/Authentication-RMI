package session;
import crypto.Blowfish;
import crypto.ICrypto;
import java.util.Date;
import java.util.HashMap;

public class Session {
    ICrypto blowfish;
    static HashMap<String, Date> allSessions = new HashMap<>();

    public Session() {
        blowfish = new Blowfish();
        //allSessions = new HashMap<String, Date>();
    }
    public void recordNewSessionForUser(String username, Date currentDatetime){
        allSessions.put(username,currentDatetime);
    }
    public Date constructAuthenticatedSessionToken(String username){
        Date currentDateTime = new Date();
        recordNewSessionForUser(username, currentDateTime);
        return currentDateTime;
    }
    
    public boolean checkSessionForUser(String username){
        if (!allSessions.containsKey(username)){
            return false;
        }
        Date recordedDateTime = (Date) allSessions.get(username);
        Date currentDateTime = new Date();

        if (currentDateTime.getTime() - recordedDateTime.getTime() <= 30*60*1000) { //checks for 30  minutes
            return true;
        }
        return  false;
    }

    public Date login(String username, String password){
        Date sessionToken = new Date();
        boolean isPasswordCorrect = blowfish.isPasswordCorrectForUser(username, password);
        if (isPasswordCorrect){
            sessionToken = constructAuthenticatedSessionToken(username);
        }else{
            return null;
        }
        return sessionToken;
    }

}
