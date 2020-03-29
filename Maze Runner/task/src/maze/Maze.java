package maze;

import exceptions.InvalidFormatException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Maze {
    private int[][] grid;
    private int height;
    private int width;
    private List<Pair> way = null;

    private static final String BLOCK_STRING = "\u2588\u2588";
    private static final String ESCAPE_STRING = "//";
    public static final int MINIMAL_SIZE = 2;
    private static final char BLOCK_CHARACTER = '*';
    private static final char FREE_SPACE_CHARACTER = ' ';

    private Maze(int height, int width) {
        this.height = height;
        this.width = width;
        grid = new int[height][width];
    }

    public static Maze load(String filename) throws FileNotFoundException, InvalidFormatException {
        List<String> rows;
        try(Scanner scanner = new Scanner(new FileInputStream(filename))) {
            rows = new ArrayList<>();
            while (scanner.hasNextLine())
                rows.add(scanner.nextLine());
        }
        int height = rows.size();
        if(height < MINIMAL_SIZE)
            throw new InvalidFormatException();
        int width = rows.get(0).length();
        for(String row: rows)
            if(row.length() < 1 || row.length() != width)
                throw new InvalidFormatException();
        Maze maze = new Maze(height, width);
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                if(rows.get(i).charAt(j) == BLOCK_CHARACTER)
                    maze.grid[i][j] = 1;
                else if(rows.get(i).charAt(j) == FREE_SPACE_CHARACTER)
                    maze.grid[i][j] = 0;
                else
                    throw new InvalidFormatException();
            }
        }
        return maze;
    }

    public void save(String filename) throws FileNotFoundException {
        try(PrintWriter writer = new PrintWriter(new FileOutputStream(filename))) {
            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    if(grid[i][j] == 1)
                        writer.print(BLOCK_CHARACTER);
                    else
                        writer.print(FREE_SPACE_CHARACTER);
                }
                writer.println("");
            }
        }
    }

    public static Maze generateMaze(int height, int width) {
        Maze maze = new Maze(height, width);
        maze.grid = MazeGenerator.generate(height, width);
        return maze;
    }

    public void findEscape() {
        if(way == null) {
            Pair enter = null, exit = null;
            for(int i = 0; i < height; i++) {
                if(grid[i][0] == 0)
                    enter = new Pair(i, 0);
                if(grid[i][width-1] == 0)
                    exit = new Pair(i, width - 1);
                if(enter != null && exit != null)
                    break;
            }
            for(int i = 0; i < width; i++) {
                if(grid[0][i] == 0)
                    enter = new Pair(0, i);
                if(grid[height-1][i] == 0)
                    exit = new Pair(height - 1, i);
                if(enter != null && exit != null)
                    break;
            }
            this.way = MazeSolver.solve(grid, enter, exit);
        }
        display(true);
    }

    public void display() {
        display(false);
    }

    private void display(boolean withEscape) {
        if(withEscape) {
            way.forEach(elem -> grid[elem.getFirst()][elem.getSecond()] = 2);
        }
        for(int[] i: grid) {
            for(int j: i) {
                if(j == 1) {
                    System.out.print(BLOCK_STRING);
                }
                else if(j == 2) {
                    System.out.print(ESCAPE_STRING);
                }
                else {
                    System.out.print("  ");
                }
            }
            System.out.println("");
        }
        if(withEscape) {
            way.forEach(elem -> grid[elem.getFirst()][elem.getSecond()] = 0);
        }
    }
}
