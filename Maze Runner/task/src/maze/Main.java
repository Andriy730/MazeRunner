package maze;

import exceptions.InvalidFormatException;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String startMenu = "=== Menu ===\n"
                + "1. Generate a new maze\n"
                + "2. Load a maze\n"
                + "0. Exit";
        String menuWithMaze = "=== Menu ===\n"
                + "1. Generate a new maze\n"
                + "2. Load a maze\n"
                + "3. Save the maze\n"
                + "4. Display the maze\n"
                + "5. Find the escape\n"
                + "0. Exit";
        String defaultMessage = "Incorrect option. Please try again";
        Maze maze = null;
        String input;
        String menu = startMenu;
        System.out.println(menu);
        while (!(input = scanner.nextLine()).equals("0")) {
            switch (input) {
                case "1": {
                    System.out.println("Enter the size of a maze");
                    int size = scanner.nextInt();
                    scanner.nextLine();
                    if(size < Maze.MINIMAL_SIZE)
                        System.out.println("Invalid size for maze");
                    else {
                        maze = Maze.generateMaze(size, size);
                        maze.display();
                        menu = menuWithMaze;
                    }
                    break;
                }
                case "2": {
                    String filename = scanner.nextLine();
                    if(!filename.substring(filename.lastIndexOf('.')).equals(".txt")) {
                        System.out.println("Cannot save the maze. Enter .txt file");
                    }
                    else {
                        try {
                            maze = Maze.load(filename);
                            menu = menuWithMaze;
                        } catch (FileNotFoundException e) {
                            System.out.println("The file " + filename + " does not exist");
                        } catch (InvalidFormatException e) {
                            System.out.println("Cannot load the maze. It has an invalid format");
                        }
                    }
                    break;
                }
                case "3": {
                    if(maze == null)
                        System.out.println(defaultMessage);
                    else {
                        String filename = scanner.nextLine();
                        if(!filename.substring(filename.lastIndexOf('.')).equals(".txt")) {
                            System.out.println("Cannot save the maze. Enter .txt file");
                        }
                        else {
                            try {
                                maze.save(filename);
                            } catch (FileNotFoundException e) {
                                System.out.println("The file " + filename + " does not exist");
                            }
                        }
                    }
                    break;
                }
                case "4": {
                    if(maze == null)
                        System.out.println(defaultMessage);
                    else
                        maze.display();
                    break;
                }
                case "5": {
                    if(maze == null)
                        System.out.println(defaultMessage);
                    else
                        maze.findEscape();
                    break;
                }
                default:
                    System.out.println(defaultMessage);
            }
            System.out.println(menu);
        }
        System.out.println("Bye!");
    }
}


/*
{{2, 6}, {2, 4, 6, 8}, {5, 8}, {1}, {2, 5, 7, 8}, {2}, {3, 5, 7}, {1, 5, 8}};
*/