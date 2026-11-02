package metLibrary.model;

/**
 *Represents a librarian user
 *<p>
 *Librarians have full administrative privileges:
 *managing catalog items, users, and loan transactions
 */
public class Librarian extends User {

    /**
     *Constructs a librarian account.
     *
     *@param id       unique librarian ID
     *@param name     librarian name
     *@param password librarian login password
     */
    public Librarian(String id, String name, String password) {
        super(id, name, password);
    }
}
