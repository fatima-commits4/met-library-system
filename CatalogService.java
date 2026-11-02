package metLibrary.service;

import metLibrary.model.Book;
import metLibrary.model.Journal;
import metLibrary.model.LibraryItem;
import metLibrary.persistence.DataStorage;

import java.util.ArrayList;
import java.util.List;

/**
 *Provides operations for managing the library catalog.
 *<p>
 *Responsibilities include:
 *<ul>
 *<li>Loading and saving catalog data (persistence)</li>
 *<li>Adding, removing, and updating catalog items</li>
 *<li>Searching items by multiple attributes</li>
 *</ul>
 *The catalog is persisted using {@link DataStorage} (serialized local files)
 */
public class CatalogService {

    /**In-memory list of all items currently in the catalog*/
    private List<LibraryItem> items;

    /**
     *Constructs the service and loads items from persistent storage
     *If no data exists yet, an empty list is created
     */
    public CatalogService() {
        List<?> loaded = DataStorage.loadItems();
        if (loaded != null) {
            items = (List<LibraryItem>) loaded;
        } else {
            items = new ArrayList<>();
        }
    }

    /**
     *Adds a new item to the catalog and persists the updated list
     *
     *@param item the item to add
     */
    public void addItem(LibraryItem item) {
        items.add(item);
        DataStorage.saveItems(items);
    }

    /**
     *Removes an item from the catalog and persists the updated list
     *
     *@param item the item to remove
     */
    public void removeItem(LibraryItem item) {
        items.remove(item);
        DataStorage.saveItems(items);
    }

    /**
     *Returns all items currently in the catalog
     *
     *@return list of catalog items
     */
    public List<LibraryItem> getAllItems() {
        return items;
    }

    /**
     *Backwards-compatible method name used by earlier UI code
     *Delegates to the more flexible {@link #search(String)} method
     *
     *@param title title (or any query string)
     *@return matching items
     */
    public List<LibraryItem> searchByTitle(String title) {
        return search(title);
    }

    /**
     *Searches the catalog for items matching the given query
     *<p>
     *The search checks:
     *<ul>
     *<li>Title</li>
     *<li>Internal item ID</li>
     *<li>Author/ISBN (for books)</li>
     *<li>Editor/ISSN (for journals)</li>
     *</ul>
     *
     *@param query the search query entered by the user
     *@return a list of matching items (might be empty)
     */
    public List<LibraryItem> search(String query) {
        String q = (query == null) ? "" : query.trim().toLowerCase();
        List<LibraryItem> results = new ArrayList<>();

        for (LibraryItem item : items) {

            //Match by title
            if (item.getTitle() != null && item.getTitle().toLowerCase().contains(q)) {
                results.add(item);
                continue;
            }

            //Match by internal item ID
            if (item.getItemId() != null && item.getItemId().toLowerCase().contains(q)) {
                results.add(item);
                continue;
            }

            //Match by book-specific fields
            if (item instanceof Book b) {
                if ((b.getAuthor() != null && b.getAuthor().toLowerCase().contains(q)) ||
                        (b.getIsbn() != null && b.getIsbn().toLowerCase().contains(q))) {
                    results.add(item);
                }
            }

            //Match by journal-specific fields
            if (item instanceof Journal j) {
                if ((j.getEditor() != null && j.getEditor().toLowerCase().contains(q)) ||
                        (j.getIssn() != null && j.getIssn().toLowerCase().contains(q))) {
                    results.add(item);
                }
            }
        }

        return results;
    }
}
