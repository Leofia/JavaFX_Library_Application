package lib;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "previously_borrowedbooks")
public class PreviouslyBorrowedBooks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "BORROWEDDATE")
    private LocalDate borrowedDate;

    @Column(name = "RETURNEDDATE")
    private LocalDate returnedDate;

     @Column(name = "original_borrowed_book_id", unique = true)
    private Long originalBorrowedBookId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(LocalDate borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
    }
     public Long getOriginalBorrowedBookId() {
        return originalBorrowedBookId;
    }

    public void setOriginalBorrowedBookId(Long originalBorrowedBookId) {
        this.originalBorrowedBookId = originalBorrowedBookId;
    }
}