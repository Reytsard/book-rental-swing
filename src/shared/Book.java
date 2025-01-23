package shared;

import java.time.LocalDate;
import java.util.Date;

public class Book {
    private int id;
    private int lenderId;
    private String title;
    private String author;
    private String category;
    private boolean available;
    private float price;
    private LocalDate dateBorrowed;

    public Book() {
        id = -1;
        lenderId = -1;
        title = "";
        author = "";
        category = "";
        available = true;
        price = 0.00f;
        dateBorrowed = LocalDate.now();
    }

    public Book(int id, int lenderId, String title, String author, String category, boolean available, float price, LocalDate dateBorrowed) {
        this.id = id;
        this.lenderId = lenderId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.available = available;
        this.price = price;
        this.dateBorrowed = dateBorrowed;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public LocalDate getDateBorrowed() {
        return dateBorrowed;
    }

    public void setDateBorrowed(LocalDate dateBorrowed) {
        this.dateBorrowed = dateBorrowed;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLenderId() {
        return lenderId;
    }

    public void setLenderId(int lenderId) {
        this.lenderId = lenderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String toString(){
        return "\""+id+"\""+
                "\""+lenderId+"\""+
                "\""+title+"\""+
                "\""+author+"\""+
                "\""+category+"\""+
                "\""+available+"\""+
                "\""+price+"\""+
                "\""+dateBorrowed+"\"";
    }

}
