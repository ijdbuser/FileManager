import java.io.File;

class FolderWork {

    private File folder;
    private File[] directory;

    FolderWork (File folder) { // This class is responsible for handling individual folders

        System.out.println("\nThe element is a folder");
        this.folder = folder;
        directory = folder.listFiles();

    }

    File returnFolder () {

        return folder;

    }

    void printFolder () {

        int counter = 0;

        // Header
        System.out.println("\n-------------------------------------");

        // Every folder must have root and home
        if (!(folder.getParent() == null)) // If a folder is a root directory, it won't have a parent file
            System.out.println("-1. --UP-- " + folder.getParentFile().getPath() + " ▲");
        else System.out.println("-1. --UP-- " + folder.getPath() + " ▲");

        System.out.println("0. --HOME-- " + folder.getPath() + " ◄");

        try {
            if (directory.length > 0) {
                for (File element : directory) { // For each element in directory
                    System.out.print(++counter + ". " + element.getName());
                    if (element.isFile())
                        System.out.printf("(%,dKB) •", element.length() / 1000); // If a file, then calculate size.
                    else System.out.print(" ►");
                    // Usable to differentiate between folders and files.
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println("The folder is empty.");
        }
        System.out.println("-------------------------------------\n");
    }

}
