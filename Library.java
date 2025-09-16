import java.util.*;
import java.io.*;

public class Library {
    private ArrayList<Book> books = new ArrayList<>();
    private final String FILE_NAME = "books.txt";

    public void addBook(Book book) {
        books.add(book);
        saveToFile();
    }

    public void borrowBook(String title) {
        try {
            for (Book b : books) {
                if (b.getTitle().equalsIgnoreCase(title)) {
                    if (!b.isBorrowed()) {
                        b.borrowBook();
                        System.out.println("You borrowed: " + b.getTitle());
                        saveToFile();
                        return;
                    } else {
                        throw new Exception("Book already borrowed!");
                    }
                }
            }
            throw new Exception("Book not found!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void returnBook(String title) {
        for (Book b : books) {
            if (b.getTitle().equalsIgnoreCase(title) && b.isBorrowed()) {
                b.returnBook();
                System.out.println("Book returned: " + b.getTitle());
                saveToFile();
                return;
            }
        }
        System.out.println("Error: Book not found or not borrowed!");
    }

    public void showBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        for (Book b : books) {
            System.out.println(b);
        }
    }

    public void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Book b : books) {
                bw.write(b.getTitle() + "," + b.getAuthor() + "," + b.isBorrowed());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        books.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Book book = new Book(data[0], data[1]);
                if (Boolean.parseBoolean(data[2])) {
                    book.borrowBook();
                }
                books.add(book);
            }
        } catch (IOException e) {
            System.out.println("No saved data found, starting fresh...");
        }
    }
}
