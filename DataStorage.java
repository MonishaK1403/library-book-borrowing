import java.io.*;

public class DataStorage {
    private final File storageFile;

    public DataStorage(String filename) {
        this.storageFile = new File(filename);
    }

    public Library loadLibrary() {
        if (!storageFile.exists()) {
            return new Library();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storageFile))) {
            Object obj = ois.readObject();
            if (obj instanceof Library) {
                return (Library) obj;
            } else {
                return new Library();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // If corrupted / unreadable, return new instance
            return new Library();
        }
    }

    public void saveLibrary(Library lib) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storageFile))) {
            oos.writeObject(lib);
            oos.flush();
        }
    }
}
