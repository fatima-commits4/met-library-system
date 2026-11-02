package metLibrary.model;

import java.io.Serializable;

/**
 *Represents a book in the library catalog
 *<p>
 *A book is a {@link LibraryItem} with additional attributes:
 *author and ISBN.
 */
public class Book extends LibraryItem implements Serializable {

    /**Author of the book*/
    private String author;

    /**ISBN identifier (may include dashes)*/
    private String isbn;

    /**
     *Constructs a book item.
     *
     *@param itemId  internal library item ID (e.g., B001)
     *@param title   book title
     *@param author  book author
     *@param isbn    ISBN code
     */
    public Book(String itemId, String title, String author, String isbn) {
        super(itemId, title);
        this.author = author;
        this.isbn = isbn;
    }

    /**
     *@return the author name
     */
    public String getAuthor() {
        return author;
    }

    /**
     *@return the ISBN code
     */
    public String getIsbn() {
        return isbn;
    }
}

