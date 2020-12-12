import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

class CopyWork {

    private File resultTypeFolder;
    private ArrayList <File> oneTypeFiles;

    CopyWork (ArrayList <File> oneTypeFiles) {

        this.oneTypeFiles = oneTypeFiles;
        resultTypeFolder = userNewFolder();

    }

    File getResultTypeFolder () {

        return resultTypeFolder;

    }

    private File userNewFolder() { // Creates a new folder either at specified location, or at path of the file provided

        File path = oneTypeFiles.get(0);
        String choice;
        File newPath;
        path = path.getParentFile();
        System.out.println("Choose an option:\nA. Using the same path: " + path.getPath() + "\nB. Enter a new path." + "\nC. Auto mode");

        // User IO
        System.out.print("Enter your choice: ");
        choice = getString();
        switch (choice.toLowerCase()) {
            case ("a"): {
                try {
                    System.out.print("Enter the name at " + path.getPath() + "\\");
                    // createDirectories creates all folders at given Path.
                    Path kek = Files.createDirectories(new File(path.getPath() + "\\" + getString()).toPath());
                    System.out.println(kek + " created.");
                    return kek.toFile();
                } catch (Exception e) {
                    System.out.println("Operation failed.");
                    userNewFolder(); // recursive call to give user another try if smth went wrong with the input
                }
                break;
            }
            case ("b"): {
                try {
                    System.out.println("\nEnter the destination to an existing or a new folder: ");

                    newPath = new File(getString()); // Complete path entered by user

                    //if (newPath.isDirectory() && newPath.exists())

                    Path kek = Files.createDirectories(newPath.toPath());
                    System.out.println(kek.getFileName() + " created.");
                    return kek.toFile();
                } catch (Exception e) {

                    System.out.println("The creation of the folder folder failed. Invalid path?");
                    userNewFolder();

                }
                break;
            }


            default: {
                // None of choices selected therefore quit
                System.out.println("Operation aborted by user.");

            }
        }
        return null;
    }

    private static String getString() { // Getter for String values
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            return br.readLine();
        } catch (Exception e) {
            System.out.println("Wrong data entered, please try again.");
            return getString();
        }
    }
}
