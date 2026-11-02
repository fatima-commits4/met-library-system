package metLibrary.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *Represents the borrowing of a library item by a student member
 *<p>
 *A loan records the loan date, calculates a due date, and can determine
 *whether the loan has become overdue
 */
public class Loan implements Serializable {

    /**
     *Standard loan duration in days.
     *This value can be temporarily adjusted for testing/demonstration
     */
    private static final int LOAN_PERIOD_DAYS = 14;

    private LibraryItem item;
    private StudentMember member;
    private LocalDate loanDate;
    private LocalDate dueDate;

    /**
     *Creates a new loan and automatically assigns loan and due dates
     *
     *@param item   the item is being borrowed
     *@param member the student member is borrowing the item
     */
    public Loan(LibraryItem item, StudentMember member) {
        this.item = item;
        this.member = member;
        this.loanDate = LocalDate.now();
        this.dueDate = loanDate.plusDays(LOAN_PERIOD_DAYS);
    }

    /**
     *Checks whether the loan is overdue based on the current date
     *
     *@return true if today is after the due date
     */
    public boolean isOverdue() {
        return LocalDate.now().isAfter(dueDate);
    }

    /**
     *@return the borrowed library item
     */
    public LibraryItem getItem() {
        return item;
    }

    /**
     *@return the student member who borrowed the item
     */
    public StudentMember getMember() {
        return member;
    }

    /**
     *@return the date the loan was created
     */
    public LocalDate getLoanDate() {
        return loanDate;
    }

    /**
     *@return the calculated due date
     */
    public LocalDate getDueDate() {
        return dueDate;
    }
}
