import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

public class LibraryApp {
    private static final String STORAGE_FILENAME = "library.dat";

    private Library library;
    private DataStorage storage;

    private JFrame frame;
    private DefaultListModel<Book> booksListModel = new DefaultListModel<>();
    private JList<Book> booksList;

    private DefaultListModel<User> usersListModel = new DefaultListModel<>();
    private JList<User> usersList;

    public LibraryApp() {
        storage = new DataStorage(STORAGE_FILENAME);
        library = storage.loadLibrary();
        initUI();
        refreshAll();
    }

    private void initUI() {
        frame = new JFrame("Library Book Borrowing System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Books", createBooksPanel());
        tabs.add("Users", createUsersPanel());
        tabs.add("Borrow/Return", createBorrowReturnPanel());

        frame.getContentPane().add(tabs, BorderLayout.CENTER);

        // Save on close
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                persist();
            }
        });

        frame.setVisible(true);
    }

    private JPanel createBooksPanel() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        booksList = new JList<>(booksListModel);
        booksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane sp = new JScrollPane(booksList);

        JPanel right = new JPanel(new BorderLayout(6,6));
        JButton newBookBtn = new JButton("New Book");
        JButton removeBookBtn = new JButton("Remove Selected");
        JButton markAvailBtn = new JButton("Toggle Borrowed");

        newBookBtn.addActionListener(e -> onNewBook());
        removeBookBtn.addActionListener(e -> onRemoveBook());
        markAvailBtn.addActionListener(e -> onToggleBorrowed());

        JPanel topBtns = new JPanel();
        topBtns.add(newBookBtn);
        topBtns.add(removeBookBtn);
        topBtns.add(markAvailBtn);

        right.add(topBtns, BorderLayout.NORTH);

        JTextArea details = new JTextArea();
        details.setEditable(false);
        right.add(new JScrollPane(details), BorderLayout.CENTER);

        booksList.addListSelectionListener(ev -> {
            Book b = booksList.getSelectedValue();
            if (b != null) {
                details.setText("ID: " + b.getId() + "\nTitle: " + b.getTitle() + "\nAuthor: " + b.getAuthor() + "\nStatus: " + (b.isBorrowed() ? "Borrowed" : "Available"));
            } else {
                details.setText("");
            }
        });

        p.add(sp, BorderLayout.CENTER);
        p.add(right, BorderLayout.EAST);
        p.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        return p;
    }

    private JPanel createUsersPanel() {
        JPanel p = new JPanel(new BorderLayout(6,6));
        usersList = new JList<>(usersListModel);
        usersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane sp = new JScrollPane(usersList);

        JPanel control = new JPanel();
        JTextField idField = new JTextField(8);
        JTextField nameField = new JTextField(10);
        JButton addUserBtn = new JButton("Add User");

        addUserBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "User ID and name required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            library.addUser(new User(id, name));
            refreshUsers();
            idField.setText("");
            nameField.setText("");
            persist();
        });

        control.add(new JLabel("ID:"));
        control.add(idField);
        control.add(new JLabel("Name:"));
        control.add(nameField);
        control.add(addUserBtn);

        p.add(sp, BorderLayout.CENTER);
        p.add(control, BorderLayout.SOUTH);
        p.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        return p;
    }

    private JPanel createBorrowReturnPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8,8,8,8);
        c.fill = GridBagConstraints.HORIZONTAL;

        JTextField bookIdField = new JTextField(12);
        JTextField userIdField = new JTextField(12);
        JButton borrowBtn = new JButton("Borrow");
        JButton returnBtn = new JButton("Return");

        c.gridx = 0; c.gridy = 0; p.add(new JLabel("Book ID:"), c);
        c.gridx = 1; p.add(bookIdField, c);
        c.gridx = 0; c.gridy = 1; p.add(new JLabel("User ID:"), c);
        c.gridx = 1; p.add(userIdField, c);

        c.gridx = 0; c.gridy = 2; p.add(borrowBtn, c);
        c.gridx = 1; p.add(returnBtn, c);

        borrowBtn.addActionListener(e -> {
            String bookId = bookIdField.getText().trim();
            String userId = userIdField.getText().trim();
            if (bookId.isEmpty() || userId.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Both Book ID and User ID required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean ok = library.borrowBook(bookId, userId);
            if (ok) {
                JOptionPane.showMessageDialog(frame, "Book borrowed successfully.");
                refreshBooks();
                persist();
            } else {
                JOptionPane.showMessageDialog(frame, "Could not borrow the book. Maybe it is already borrowed or ID invalid.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        returnBtn.addActionListener(e -> {
            String bookId = bookIdField.getText().trim();
            if (bookId.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Book ID required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean ok = library.returnBook(bookId);
            if (ok) {
                JOptionPane.showMessageDialog(frame, "Book returned successfully.");
                refreshBooks();
                persist();
            } else {
                JOptionPane.showMessageDialog(frame, "Could not return the book. It may not be borrowed or ID invalid.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        p.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        return p;
    }

    private void onNewBook() {
        NewBookDialog dlg = new NewBookDialog(frame);
        dlg.setDefaults("", "", "");
        dlg.setVisible(true);
        if (dlg.isSaved()) {
            Book b = new Book(dlg.getBookId(), dlg.getBookTitle(), dlg.getBookAuthor());
            library.addBook(b);
            refreshBooks();
            persist();
        }
    }

    private void onRemoveBook() {
        Book sel = booksList.getSelectedValue();
        if (sel == null) {
            JOptionPane.showMessageDialog(frame, "Select a book to remove.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int rc = JOptionPane.showConfirmDialog(frame, "Remove selected book?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (rc == JOptionPane.YES_OPTION) {
            library.removeBookById(sel.getId());
            refreshBooks();
            persist();
        }
    }

    private void onToggleBorrowed() {
        Book sel = booksList.getSelectedValue();
        if (sel == null) {
            JOptionPane.showMessageDialog(frame, "Select a book.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        sel.setBorrowed(!sel.isBorrowed());
        refreshBooks();
        persist();
    }

    private void refreshBooks() {
        SwingUtilities.invokeLater(() -> {
            booksListModel.clear();
            List<Book> all = library.getBooks();
            for (Book b : all) booksListModel.addElement(b);
        });
    }

    private void refreshUsers() {
        SwingUtilities.invokeLater(() -> {
            usersListModel.clear();
            for (User u : library.getUsers()) usersListModel.addElement(u);
        });
    }

    private void refreshAll() {
        refreshBooks();
        refreshUsers();
    }

    private void persist() {
        try {
            storage.saveLibrary(library);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to save library: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Setup small sample data if storage is empty
        SwingUtilities.invokeLater(() -> {
            LibraryApp app = new LibraryApp();
            if (app.library.getBooks().isEmpty() && app.library.getUsers().isEmpty()) {
                app.library.addBook(new Book("B001", "Introduction to Java", "Jane Doe"));
                app.library.addBook(new Book("B002", "Data Structures", "John Smith"));
                app.library.addUser(new User("U001", "Alice"));
                app.library.addUser(new User("U002", "Bob"));
                app.refreshAll();
                app.persist();
            }
        });
    }
}

