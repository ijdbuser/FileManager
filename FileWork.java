import java.awt.*;
import java.io.File;

class FileWork { // This class is responsible for handling individual files

    private File file;

    FileWork (File file) {

        System.out.println("\nThe element is a file.");
        this.file = file;

    }



    boolean deleteFile() { // Deletion of a file
        try {
            return file.delete();
        } catch (Exception e) {
            System.out.println("Something went wrong with deleting file " + file.getName());
            return false;
        }
    }

    void openFile() { // Opens file using default OS dialogue
        try {
            Desktop.getDesktop().open(file);
        } catch (Exception e) {
            System.out.println("Failed to open the file.");
        }
    }


}
