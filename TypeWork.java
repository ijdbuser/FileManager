import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

class TypeWork {

    private HashMap<String, ArrayList<File>> typeFileMap;
    private BufferedReader br;
    private ArrayList<File> oneTypeFiles;

    TypeWork (HashMap<String, ArrayList<File>> typeFileMap, BufferedReader br) {

        this.typeFileMap = typeFileMap;
        this.br = br;
        oneTypeFiles = getThisOneTypeFiles();

    }

    ArrayList<File> getOneTypeFiles() {

        return oneTypeFiles;

    }

    private ArrayList<File> getThisOneTypeFiles() { // Returns an array with files that are input '.type'
        try {
            if (typeFileMap.isEmpty()) {
                System.out.println("The folder is empty...");
                return null;
            } else {
                System.out.print("Enter the type of the file: ");
                String type = getString();
                return typeFileMap.get(type);
            }
        } catch (Exception e) {
            System.out.println("The type and/or index do not exist. Try again.");
            return getOneTypeFiles(); // Recursion for user IO errors
        }

    }
    private String getString() { // Getter for String values
        try {
            return br.readLine();
        } catch (Exception e) {
            System.out.println("Wrong data entered, please try again.");
            return getString();
        }
    }
}
