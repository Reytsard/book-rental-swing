package shared;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthdate;
    private List<Integer> borrowedBooks;
    private String username;
    private String password;

    public User(){
        id = -1;
        firstName = "";
        lastName = "";
        middleName = "";
        birthdate = LocalDate.now();
        borrowedBooks = new ArrayList<>();
        username = "";
        password = "";
    }

    public User(int id, String firstName, String lastName, String middleName, LocalDate birthdate, List<Integer> borrowedBooks, String username, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.birthdate = birthdate;
        this.borrowedBooks = borrowedBooks;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public List<Integer> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<Integer> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
