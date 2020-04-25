package org.didierdominguez.beans;


public class Book {
    private int isbn;
    private String title;
    private String author;
    private String editorial;
    private int year;
    private int edition;
    private Category category;
    private String language;
    private User user;

    public Book(int isbn, String title, String author, String editorial, int year, int edition,
                Category category, String language, User user) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.editorial = editorial;
        this.year = year;
        this.edition = edition;
        this.category = category;
        this.language = language;
        this.user = user;
    }

    public int getISBN() {
        return isbn;
    }

    public void setISBN(int isbn) {
        this.isbn = isbn;
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

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
