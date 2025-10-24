import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Library implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Book> books = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    public synchronized void addBook(Book book) {
        if (!books.contains(book)) books.add(book);
    }

    public synchronized void removeBookById(String id) {
        books.removeIf(b -> b.getId().equals(id));
    }

    public synchronized Optional<Book> findBookById(String id) {
        return books.stream().filter(b -> b.getId().equals(id)).findFirst();
    }

    public synchronized List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    public synchronized void addUser(User user) {
        if (!users.contains(user)) users.add(user);
    }

    public synchronized List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public synchronized Optional<User> findUserById(String userId) {
        return users.stream().filter(u -> u.getUserId().equals(userId)).findFirst();
    }

    public synchronized boolean borrowBook(String bookId, String userId) {
        Optional<Book> ob = findBookById(bookId);
        Optional<User> ou = findUserById(userId);
        if (ob.isPresent() && ou.isPresent()) {
            Book b = ob.get();
            if (!b.isBorrowed()) {
                b.setBorrowed(true);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean returnBook(String bookId) {
        Optional<Book> ob = findBookById(bookId);
        if (ob.isPresent()) {
            Book b = ob.get();
            if (b.isBorrowed()) {
                b.setBorrowed(false);
                return true;
            }
        }
        return false;
    }
}

