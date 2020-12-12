import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FileManager {

    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));  // Command Line input

    public static void main(String[] args) {

        boolean check = false;

        while (!check) { // Program will run unless interrupted

            System.out.println("This program is able to group files based on their type into a chosen folder.");
            System.out.println("Start with typing in the path of a folder or hard drive (E.g. C:\\).\n");

            File folder = null;
            File[] directory;

            while (folder == null) { // Check for null folder
                System.out.print("Enter the path to the folder: ");
                folder = getFolder(getString()); // Getting folder at given path
            }

            FolderWork folderWorkObject = new FolderWork(folder);
            StructureWork structureWorkObject = new StructureWork(folder);

            folderWorkObject.printFolder();

            directory = structureWorkObject.getStructure(); // Getting the skeleton of files from a folder
            check = getType(directory, folder); // Catalogue browsing
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
        }

    }


    private static Boolean getType(File[] directory, File folder) { // Gets an element from folder and detects its type

        int index;
        File file;

        System.out.print("Enter the index of a file/folder: ");
        index = getInteger();

        while (index < -1 || index > directory.length) { // Getting index for the file/folder the user wants to operate.
            System.out.print("Invalid index. Enter the index of a file/folder: ");
            index = getInteger();
        }

        // Index at 0 returns current folder File
        // Index at -1 return parent folder File (with protection)
        if (index == 0) file = folder;
        else if (index == -1) {
            if (!(folder.getParent() == null))
                file = folder.getParentFile();
            else {
                file = folder;
            }

        } else file = directory[index - 1]; // else we get a File at index

        System.out.printf("The element number %d: '%s' chosen. Proceeding...", index, file.getPath());

        // The number of operations if element is a folder:
        if (file.isDirectory()) {
            return folderWork(file);
        } else if (file.isFile()) {
            return fileWork(file);
        } else return true;
    }

    private static boolean fileWork(File file) { // Method for file operations

        FileWork fileWorkObject = new FileWork(file);

        System.out.println("Choose an option: \nA. delete\nB. open\nElse to exit the program.");

        // User IO
        System.out.print("Enter the choice: ");
        String choice = getString();
        switch (choice.toLowerCase()) {
            case ("a"): {
                System.out.println("Are you sure you want to delete file " + file.getPath() + " (y/n)");

                choice = getString();

                if ("y".equals(choice.toLowerCase())) {
                    if (fileWorkObject.deleteFile())
                        System.out.println("Deletion of file: " + file.getName() + " is successful");
                    else System.out.println("File: " + file.getName() + " is not deleted.");
                    return false;
                }
                System.out.println("Operation aborted.");
                return false;

            }
            case ("b"): {
                fileWorkObject.openFile();
                return false;
            }
            default: {
                return true;
            }
        }
    }


    private static boolean folderWork(File folder) { // Method for folder operations

        System.out.println("Choose an option: \nA. Open\nB. Classify Files\nC. Auto Classify\nElse to exit the program.\n");

        FolderWork folderWorkObject = new FolderWork(folder);
        StructureWork structureWorkObject = new StructureWork(folder);

        // User IO
        String choice = getString();
        switch (choice.toLowerCase()) {
            case ("a"): {

                folderWorkObject.printFolder();
                getType(structureWorkObject.getStructure(), folder);
                break;

            }
            case ("b"): {

                folderWorkObject.printFolder();

                File[] directory = structureWorkObject.getStructure();

                structureWorkObject.printTypeHash();

                HashMap<String, ArrayList<File>> typeFileMap = structureWorkObject.getTypeFileMap();

                TypeWork typeWork = new TypeWork(typeFileMap, br);

                ArrayList<File> oneTypeFiles = typeWork.getOneTypeFiles();


                if (oneTypeFiles == null) {
                    System.out.println("No files to typeHash...");
                    return false;
                    // Making sure we break out of the loop if no files can be scanned
                } else {

                    CopyWork copyWorkObject = new CopyWork(oneTypeFiles);
                    File resultTypeFolder = copyWorkObject.getResultTypeFolder();

                    if (resultTypeFolder == null) return true; // Stop algorithm if the folder is not created.

                    System.out.println("New destination is obtained.");

                    for (File each : oneTypeFiles) // For each file in the array of files obtained
                        // the advantage of looped method call is that each File is treated individually
                        // therefore if an exception happens for one file, it will not affect other
                        moveFiles(each, resultTypeFolder);
                    return false; // false -> continue working
                }


            }

            case "c": {

                File[] directory = structureWorkObject.getStructure();

                structureWorkObject.printTypeHash();

                HashMap<String, ArrayList<File>> typeFileMap = structureWorkObject.getTypeFileMap();

                structureWorkObject.printTypeHash();

                //printTypeHash(typeFileMap); // prints out the typeFileMap

                for (Map.Entry<String, ArrayList<File>> entrySet : typeFileMap.entrySet()) {

                    String type = entrySet.getKey();
                    ArrayList<File> files = entrySet.getValue();

                    File resultTypeFolder = autoNewFolder(type, files.get(0));

                    for (File each : files) {

                        System.out.printf("Moving %s to %s\n", each.toString(), resultTypeFolder.toString());

                        moveFiles(each, resultTypeFolder);

                    }


                }

            }

            default: { // If user enters any other choice stop the program
                System.out.println("Chosen non-existing option. Closing...");
                return true;
            }
        }
        return true;
    }


    private static void moveFiles(File file, File folder) { // "moves" a file from file location to folder location

        try {

            File destFile = new File(folder.getPath() + "\\" + file.getName());  // Creating a File at the destination to write into
            // it will be the same type and name as the original one
            System.out.println("Copying " + file.getName() + "...");
            Files.copy(file.toPath(), destFile.toPath());
            System.out.println("Successful.");

            if (deleteFile(file)) { // Deleting old file to complete moving operation
                // notice that deletion happens only if Files.copy had not thrown an exception
                System.out.println("Old file deleted.");
            } else System.out.println("No rights to delete the file."); // Most likely cause of the exception


        } catch (FileAlreadyExistsException e) {
            System.out.println("File already exists at a given location..."); // Very likely to pop up
        } catch (Exception e) {
            System.out.println("Copying failed bruh... " + e);
        }

    }

    private static File autoNewFolder(String type, File path) {

        path = path.getParentFile();

        try {
            Path kek = Files.createDirectories(new File(path.getPath() + "\\" + "dump" + type.toUpperCase()).toPath());
            return kek.toFile();
        } catch (Exception e) {

            System.out.printf("Creation of the auto folder for type %s failed.", type);

        }
        return null;
    }

    private static boolean deleteFile(File file) { // Deletion of a file
        try {
            return file.delete();
        } catch (Exception e) {
            System.out.println("Something went wrong with deleting file " + file.getName());
            return false;
        }
    }

    private static File getFolder(String path) { // Returns the File only ifDirectory()
        try {
            File file = new File(path);
            if (file.isDirectory())
                return file;
            else {
                System.out.println("The chosen path does not lead to a directory. Please try again.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("The path does not exist.");
            return null;
        }
    }

    private static String getString() { // Getter for String values
        try {
            return br.readLine();
        } catch (Exception e) {
            System.out.println("Wrong data entered, please try again.");
            return getString();
        }
    }

    private static int getInteger() { // Getter for int values.
        try {
            return Integer.parseInt(br.readLine());
        } catch (Exception e) {
            System.out.println("Wrong data entered, please try again.");
            return getInteger();
        }
    }
}

// Name: Mikhail Semyanovskiy
// Project: File Manager
// Title: File Manager
// Date: 6/5/2019
