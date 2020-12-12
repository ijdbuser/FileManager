import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class StructureWork { // This class is responsible for parsing a

    private File [] directory;
    private HashMap<String, ArrayList<File>> typeFileMap;
    //private ArrayList<File> oneTypeFiles;

    StructureWork (File folder) {

        this.directory = folder.listFiles();
        typeHash();

    }

    File [] getStructure () {

        return directory;

    }

    void printTypeHash () {

        // Table for file types and names
        System.out.println("Type: \t\t Files:");
        System.out.println("--------------------");
        for (HashMap.Entry<String, ArrayList<File>> key : typeFileMap.entrySet()) {
            System.out.print(key.getKey() + " ::: ");
            int i = 0;
            for (File kek : key.getValue()) {
                System.out.print(++i + ". " + kek.getName() + " :: ");
            }
            System.out.println("(" + i + " total)\n");
        }

    }

    HashMap<String, ArrayList<File>> getTypeFileMap () {

        return typeFileMap;

    }

    private void typeHash() { // Returns the assorted hashmap of files at given location

        HashMap<String, ArrayList<File>> map = new HashMap<>(); // Will store files under a type. E.g. .exe : file, file2, file3

        for (File element : directory) {
            try {
                if (element.isFile()) { // For each file
                    String[] kek = element.getName().split("\\."); // Some files look like 'name.name.type'
                    String type = element.getName().split("\\.")[kek.length - 1]; // Thus I only take the last element of array (.type)
                    if (map.containsKey(type)) // if type already encountered
                        map.get(type).add(element); // add filename to array
                    else
                        // else put new pair type : ArrayList<File> in the collection
                        map.put(type, new ArrayList<>(Arrays.asList(element)));
                }
            } catch (Exception e) {
                System.out.println("Wrong file type... of " + element); // Case parsing went wrong
            }

        }

        typeFileMap = map;

    }


}
