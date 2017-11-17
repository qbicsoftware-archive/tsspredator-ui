package model.beans;

/**
 * @author jmueller
 */
public class ProjectBean {
    private String name, registrationDate;

    @Override
    public String toString() {
        return name + " (" + registrationDate + ")";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }
}
