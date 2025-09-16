import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        library.loadFromFile();
        Scanner sc = new Scanner(System.in);

        Librarian lib = new Librarian("Admin", 1);
        Student s1 = new Student("John", 101);

        while (true) {
            System.out.println("\n--- Library Menu ---");
            System.out.println("1. Add Book (Librarian)");
            System.out.println("2. Show Books");
            System.out.println("3. Borrow Book (Student)");
            System.out.println("4. Return Book (Student)");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter book title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter author: ");
                    String author = sc.nextLine();
                    lib.addBook(library, new Book(title, author));
                    break;
                case 2:
                    library.showBooks();
                    break;
                case 3:
                    System.out.print("Enter book title to borrow: ");
                    library.borrowBook(sc.nextLine());
                    break;
                case 4:
                    System.out.print("Enter book title to return: ");
                    library.returnBook(sc.nextLine());
                    break;
                case 5:
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
