package metLibrary.persistence;

import java.io.*;
import java.util.List;

/**
 *Handles persistence for the MetLibrary application.
 *<p>
 *This class is responsible for saving and loading all critical system data
 *using Java object serialization. Data is stored locally in binary (.dat) files.
 *<p>
 *The class provides generic helper methods for saving/loading lists, as well as
 *specific convenience methods for users, catalog items, and loans.
 *
 *<p><b>Design decision:</b>
 *Serialization was chosen for simplicity and suitability for a small standalone
 *desktop application, avoiding the complexity of a full database setup.
 */
public class DataStorage {

    /**File used to persist user accounts. */
    private static final String USERS_FILE = "users.dat";

    /**File used to persist library catalog items. */
    private static final String ITEMS_FILE = "items.dat";

    /**File used to persist loan records. */
    private static final String LOANS_FILE = "loans.dat";

    //Generic Save / Load Methods

    /**
     *Saves a list of objects to a file using Java serialization.
     *
     *@param list     the list of objects to save
     *@param filename the destination file name
     *@param <T>      the type of objects being saved
     */
    public static <T> void saveList(List<T> list, String filename) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(filename))) {

            oos.writeObject(list);

        } catch (IOException e) {
            System.out.println("Error saving data to " + filename);
        }
    }

    /**
     *Loads a list of objects from a file using Java serialization.
     *
     *@param filename the source file name
     *@param <T>  the expected type of the list elements
     *@return the loaded list, or {@code null} if the file doesn't exist or fails to load
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> loadList(String filename) {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(filename))) {

            return (List<T>) ois.readObject();

        } catch (FileNotFoundException e) {
            //File doesn't exist yet (first run of the application)
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data from " + filename);
            return null;
        }
    }

    //Convenience Methods (Used by Services)

    /**
     *Saves the list of users to persistent storage.
     *
     *@param users list of users to save
     */
    public static void saveUsers(List<?> users) {
        saveList(users, USERS_FILE);
    }

    /**
     *Loads the list of users from persistent storage.
     *
     *@return list of users, or null if no data exists
     */
    public static List<?> loadUsers() {
        return loadList(USERS_FILE);
    }

    /**
     *Saves the list of catalog items to persistent storage.
     *
     *@param items list of library items to save
     */
    public static void saveItems(List<?> items) {
        saveList(items, ITEMS_FILE);
    }

    /**
     *Loads the list of catalog items from persistent storage.
     *
     *@return list of library items, or null if no data exists
     */
    public static List<?> loadItems() {
        return loadList(ITEMS_FILE);
    }

    /**
     *Saves the list of loan records to persistent storage.
     *
     *@param loans list of loans to save
     */
    public static void saveLoans(List<?> loans) {
        saveList(loans, LOANS_FILE);
    }

    /**
     *Loads the list of loan records from persistent storage.
     *
     *@return list of loans, or null if no data exists
     */
    public static List<?> loadLoans() {
        return loadList(LOANS_FILE);
    }
}

