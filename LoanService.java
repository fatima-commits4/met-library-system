package metLibrary.service;

import metLibrary.model.Loan;
import metLibrary.persistence.DataStorage;

import java.util.ArrayList;
import java.util.List;

/**
 *Provides operations for managing loan transactions
 *<p>
 *Responsibilities include:
 *<ul>
 *<li>Issuing loans (borrowing)</li>
 *<li>Returning loans</li>
 *<li>Retrieving all loans and overdue loans</li>
 *<li>Persisting loan records using {@link DataStorage}</li>
 *</ul>
 */
public class LoanService {

    /**In-memory list of currently active loans*/
    private List<Loan> loans;

    /**
     *Constructs the service and loads existing loans from storage
     *If no data exists yet, an empty list is used
     */
    public LoanService() {
        List<?> loaded = DataStorage.loadLoans();
        if (loaded != null) {
            loans = (List<Loan>) loaded;
        } else {
            loans = new ArrayList<>();
        }
    }

    /**
     *Records a new loan and persists the updated loan list
     *
     *@param loan the loan to issue
     */
    public void issueLoan(Loan loan) {
        loans.add(loan);
        DataStorage.saveLoans(loans);
    }

    /**
     *Removes a loan record (item returned) and persists the updated loan list
     *
     *@param loan the loan to remove
     */
    public void returnLoan(Loan loan) {
        loans.remove(loan);
        DataStorage.saveLoans(loans);
    }

    /**
     *Returns a list of all overdue loans
     *A loan is considered overdue when {@link Loan#isOverdue()} returns true
     *
     *@return list of overdue loans
     */
    public List<Loan> getOverdueLoans() {
        List<Loan> overdue = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.isOverdue()) {
                overdue.add(loan);
            }
        }
        return overdue;
    }

    /**
     *Returns all current loan records
     *
     *@return list of loans
     */
    public List<Loan> getAllLoans() {
        return loans;
    }
}
