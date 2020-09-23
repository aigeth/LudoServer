/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AccountManager;

/**
 *
 * @author Aigeth
 */
public class Account {
    private long id;
    private String firstName,lastName, url;
    private boolean facebook, Authorized = false;

    public Account(long id, String firstName, String lastName, boolean facebook, String url) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.facebook = facebook;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isFacebook() {
        return facebook;
    }

    public void setFacebook(boolean facebook) {
        this.facebook = facebook;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAuthorized() {
        this.Authorized = true;
    }

    public boolean isAuthorized() {
        return Authorized;
    }
    
    @Override
    public String toString() {
        return id + "£" + firstName + "£" + lastName + "£" + facebook + "£" + url;
    }
    
    
    
    
}
