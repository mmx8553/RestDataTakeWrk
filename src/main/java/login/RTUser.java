package login;

/**
 * Created by OsipovMS on 21.03.2018.
 */
public class RTUser {

    //todo usin arraylist of user to store info
    private String login;
    private String password;
    private String token;
    private boolean statusOk;


    public boolean isStatusOk() {
        return statusOk;
    }

    public void setStatusOk(boolean statusOk) {
        this.statusOk = statusOk;
    }


    public RTUser(String login, String password, String token, boolean statusOk) {
        this.login = login;
        this.password = password;
        this.token = token;
        this.statusOk = statusOk;
    }


    public String getLogin() {

        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



}
