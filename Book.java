import java.io.Serializable;
import java.util.Objects;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id; // unique id e.g. ISBN-like or generated
    private String title;
    private String author;
    private boolean borrowed;

    public Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.borrowed = false;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isBorrowed() { return borrowed; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setBorrowed(boolean borrowed) { this.borrowed = borrowed; }

    @Override
    public String toString() {
        return String.format("%s — \"%s\" by %s %s", id, title, author, (borrowed ? "(Borrowed)" : ""));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book b = (Book) o;
        return Objects.equals(id, b.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
