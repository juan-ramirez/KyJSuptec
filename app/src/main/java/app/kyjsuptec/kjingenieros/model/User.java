package app.kyjsuptec.kjingenieros.model;

/**
 * Created by USER on 21/01/2017.
 */

public class User {
    public String uuid;
    public String approved;
    public String checked;
    public String project;
    public String username;
    public boolean isAdmin;

    public User() {
    }

    public String getUuid() {
        return uuid;
    }

    public String getApproved() {
        return approved;
    }

    public String getChecked() {
        return checked;
    }

    public String getProject() {
        return project;
    }

    public String getUsername() {
        return username;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

}
