package lib;

import java.time.LocalDate;
import javax.persistence.*;

@Entity
@Table(name = "BORROWEDBOOKS")
public class BorrowedBooks {
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

    
}