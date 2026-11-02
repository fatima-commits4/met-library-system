package metLibrary.model;

import java.io.Serializable;

/**
 *Abstract base class for all items stored in the library catalog
 *<p>
 *This class defines common attributes shared by all library items, such as:
 *an internal item ID, title, and availability status
 *<p>
 *Concrete subclasses (e.g., {@link Book}, {@link Journal}) extend this class
 *to add type-specific attributes
 */
public abstract class LibraryItem implements Serializable {

    /**Unique internal identifier for the item (e.g., B001, J002). */
    protected String itemId;

    /**Display title of the item. */
    protected String title;

    /**Whether the item is currently available for borrowing. */
    protected boolean available = true;

    /**
     *Creates a new library item with the given ID and title
     *
     *@param itemId the unique internal ID of the item
     *@param title  the title of the item
     */
    public LibraryItem(String itemId, String title) {
        this.itemId = itemId;
        this.title = title;
    }

    /**
     *@return the internal item ID
     */
    public String getItemId() {
        return itemId;
    }

    /**
     *@return the item title
     */
    public String getTitle() {
        return title;
    }

    /**
     *@return true if the item can be borrowed, false if currently on loan
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     *Updates the availability state of the item
     *
     *@param available true if the item should be marked available, false otherwise
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     *Provides a user-friendly text representation for GUI controls such as ComboBox
     *
     *@return a readable string containing ID and title
     */
    @Override
    public String toString() {
        return itemId + " - " + title;
    }
}
