package metLibrary.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *Represents a student member of the library
 *<p>
 *Student members can search the catalog and view their own borrowing information
 */
public class StudentMember extends User implements Serializable {

    /**
     *Stores a history of loans associated with the student
     *Note: In this implementation, current loans are managed via {@link metLibrary.service.LoanService}.
     */
    private List<Loan> loanHistory = new ArrayList<>();

    /**
     *Constructs a student member account.
     *
     *@param id       unique student ID
     *@param name     student name
     *@param password student login password
     */
    public StudentMember(String id, String name, String password) {
        super(id, name, password);
    }

    /**
     *Returns the student's loan history list
     *(Optional feature: can be extended to record all past loans.)
     *
     *@return list of loans associated with this student
     */
    public List<Loan> getLoanHistory() {
        return loanHistory;
    }
}
