package library;

import java.util.ArrayList;

/**
 * User class for users
 * @author Ministler team
 */
public class User {

    // instances
    private String id;
    private String name, phoneNo;
    private String email;

    /**
     * Empty constructor
     */
    public User() {

    }

    /**
     * Main constructor
     * @param name name of the user
     * @param id id of the user
     * @param phoneNo phone number of the user
     * @param email e-mail address of the user
     */
    public User( String name,  String id , String phoneNo , String email ) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.id = id;
        this.email = email;
    }

    /**
     * Getter for the e-mail address
     * @return e-mail address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for the id of the user
     * @param id id of the user
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Setter for the name of the user
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for the phone number of the user
     * @param phoneNo phone number of the user
     */
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    /**
     * Returns the id of the user.
     * @return the id of the user
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the phone number of the user.
     * @return the phone number of the user
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     * Returns the name of the user.
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

}