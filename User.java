package metLibrary.model;

import java.io.Serializable;

/**
 *Abstract base class for all system users.
 *<p>
 *The system supports multiple user roles (e.g., librarian and student)
 *Role checks are provided for simple access control in the GUI.
 */
public abstract class User implements Serializable {

    /**Unique user ID used for login and identification*/
    protected String id;

    /**Display name of the user*/
    protected String name;

    /**Password used for login (stored as plain text for simplicity in this coursework)*/
    protected String password;

    /**
     *Creates a new user with credentials
     *
     *@param id       unique user ID
     *@param name     user display name
     *@param password login password
     */
    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    /**
     *@return the user ID
     */
    public String getId() {
        return id;
    }

    /**
     *@return the user display name
     */
    public String getName() {
        return name;
    }

    /**
     *@return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     *Convenience role check used by the GUI to enable/disable features
     *
     *@return true if this user is a librarian
     */
    public boolean isLibrarian() {
        return this instanceof Librarian;
    }

    /**
     *Convenience role check used by the GUI to enable/disable features
     *
     *@return true if this user is a student member
     */
    public boolean isStudent() {
        return this instanceof StudentMember;
    }

    /**
     *Provides a user-friendly text representation for GUI controls and tables
     *
     *@return readable string containing name and ID
     */
    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}
