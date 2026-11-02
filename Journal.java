package metLibrary.model;

/**
 *Represents a journal in the library catalog
 *<p>
 *A journal is a {@link LibraryItem} with additional attributes:
 *editor and ISSN.
 */
public class Journal extends LibraryItem {

    /**Editor of the journal*/
    private String editor;

    /**ISSN identifier (may include dashes)*/
    private String issn;

    /**
     *Constructs a journal item.
     *
     *@param itemId  internal library item ID (e.g., J001)
     *@param title   journal title
     *@param editor  journal editor
     *@param issn    ISSN code
     */
    public Journal(String itemId, String title, String editor, String issn) {
        super(itemId, title);
        this.editor = editor;
        this.issn = issn;
    }

    /**
     *@return the editor name
     */
    public String getEditor() {
        return editor;
    }

    /**
     *@return the ISSN code
     */
    public String getIssn() {
        return issn;
    }
}


