package metLibrary.service;

import metLibrary.model.User;
import metLibrary.persistence.DataStorage;

import java.util.ArrayList;
import java.util.List;

/**
 *Provides operations for managing system users and authentication
 *<p>
 *Responsibilities include:
 *<ul>
 *<li>Loading and saving users (persistence)</li>
 *<li>Adding and removing student accounts</li>
 *<li>Authenticating users via a simple login method</li>
 *</ul>
 *User data is persisted using {@link DataStorage}.
 */
public class UserService {

    /* In-memory list of all system users. */
    private List<User> users;

    /**
     *Constructs the service and loads users from storage
     *If no data exists yet, an empty list is used
     */
    public UserService() {
        List<?> loaded = DataStorage.loadUsers();
        if (loaded != null) {
            users = (List<User>) loaded;
        } else {
            users = new ArrayList<>();
        }
    }

    /**
     *Adds a user to the system and persists the updated list
     *
     *@param user the user to add
     */
    public void addUser(User user) {
        users.add(user);
        DataStorage.saveUsers(users);
    }

    /**
     *Removes a user from the system and persists the updated list
     *
     *@param user the user to remove
     */
    public void removeUser(User user) {
        users.remove(user);
        DataStorage.saveUsers(users);
    }

    /**
     *Returns all users in the system
     *
     *@return list of users
     */
    public List<User> getAllUsers() {
        return users;
    }

    /**
     *Authenticates a user by matching ID and password
     *<p>
     *Note: Passwords are stored in plain text for simplicity in this coursework
     *
     *@param id       user ID
     *@param password password
     *@return the authenticated user, or null if credentials are invalid
     */
    public User login(String id, String password) {
        for (User user : users) {
            if (user.getId().equals(id) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}
