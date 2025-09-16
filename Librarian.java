public class Librarian extends User {
    public Librarian(String name, int id) {
        super(name, id);
    }

    public void addBook(Library lib, Book book) {
        lib.addBook(book);
        System.out.println("Book added by librarian: " + book.getTitle());
    }
}
