package maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MazeGenerator {
    private static int height;
    private static int width;

    public static int[][] generate(int height, int width) {
        MazeGenerator.height = height;
        MazeGenerator.width = width;
        int[][] maze = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                maze[i][j] = 1;
            }
        }
        randomMaze(maze);
        for (int i = 0; i < height; i++) {
            maze[i][0] = 1;
            maze[i][width - 1] = 1;
        }
        for (int j = 0; j < width; j++) {
            maze[0][j] = 1;
            maze[height-1][j] = 1;
        }
        makeEnterAndExit(maze);
        return maze;
    }

    private static void randomMaze(int[][] maze) {
        Random random = new Random();
        int i;
        int j;
        do {
            i = random.nextInt(height);
            j = random.nextInt(width);
        } while (inCorner(i, j));
        maze[i][j] = 0;
        List<Pair> list = new ArrayList<>();
        if(inside(i+2, j))
            list.add(new Pair(i+2, j));
        if(inside(i-2, j))
            list.add(new Pair(i-2, j));
        if(inside(i, j+2))
            list.add(new Pair(i, j+2));
        if(inside(i, j-2))
            list.add(new Pair(i, j-2));
        while (!list.isEmpty()) {
            Pair cell = list.remove(random.nextInt(list.size()));
            i = cell.getFirst();
            j = cell.getSecond();
            maze[i][j] = 0;
            List<Pair> neighbors = new ArrayList<>(4);
            if (inside(i + 2, j)) {
                if (maze[i + 2][j] == 1)
                    list.add(new Pair(i + 2, j));
                else
                    neighbors.add(new Pair(i + 1, j));
            }
            if (inside(i - 2, j)) {
                if (maze[i - 2][j] == 1)
                    list.add(new Pair(i - 2, j));
                else
                    neighbors.add(new Pair(i - 1, j));
            }
            if (inside(i, j + 2)) {
                if (maze[i][j + 2] == 1)
                    list.add(new Pair(i, j + 2));
                else
                    neighbors.add(new Pair(i, j + 1));
            }
            if (inside(i, j - 2)) {
                if(maze[i][j - 2] == 1)
                    list.add(new Pair(i, j-2));
                else
                neighbors.add(new Pair(i, j - 1));
            }
            if(!neighbors.isEmpty())
                cell = neighbors.get(random.nextInt(neighbors.size()));
                maze[cell.getFirst()][cell.getSecond()] = 0;
        }
    }

    private static void makeEnterAndExit(int[][] maze) {
        List<Integer> enterIndexesVertical = new ArrayList<>();
        List<Integer> exitIndexesVertical = new ArrayList<>();
        List<Integer> enterIndexesHorizontal = new ArrayList<>();
        List<Integer> exitIndexesHorizontal = new ArrayList<>();
        for(int i = 0; i < height; i++) {
            if(maze[i][1] == 0)
                enterIndexesVertical.add(i);
            if(maze[i][width-2] == 0)
                exitIndexesVertical.add(i);
        }
        for(int j = 0; j < width; j++) {
            if(maze[1][j] == 0)
                enterIndexesHorizontal.add(j);
            if(maze[height-2][j] == 0)
                exitIndexesHorizontal.add(j);
        }
        if(enterIndexesVertical.isEmpty() && enterIndexesHorizontal.isEmpty()) {
            for(int i = 0; i < height; i++)
                if(maze[i][2] == 0)
                    enterIndexesVertical.add(i);

            for(int j = 0; j < width; j++)
                if(maze[2][j] == 0)
                    enterIndexesHorizontal.add(j);

        }
        if(exitIndexesVertical.isEmpty() && exitIndexesHorizontal.isEmpty()) {
            for(int i = 0; i < height; i++)
                if(maze[i][width-3] == 0)
                    exitIndexesVertical.add(i);

            for(int j = 0; j < width; j++)
               if(maze[height-3][j] == 0)
                   exitIndexesHorizontal.add(j);
        }
        Random random = new Random();
        double choice = random.nextDouble();
        int enter, exit;
        byte verticalEnter = 0;
        byte verticalExit = 0;
        if((choice < 0.5 && !enterIndexesVertical.isEmpty())
                || (!enterIndexesVertical.isEmpty() && enterIndexesHorizontal.isEmpty())) {
            verticalEnter = 1;
            enter = enterIndexesVertical.get(random.nextInt(enterIndexesVertical.size()));
        }
        else
            enter = enterIndexesHorizontal.get(random.nextInt(enterIndexesHorizontal.size()));
        choice = random.nextDouble();
        if((choice < 0.5 && !exitIndexesVertical.isEmpty())
                || (!exitIndexesVertical.isEmpty() && exitIndexesHorizontal.isEmpty())) {
            verticalExit = 1;
            exit = exitIndexesVertical.get(random.nextInt(exitIndexesVertical.size()));
        }
        else
            exit = exitIndexesHorizontal.get(random.nextInt(exitIndexesHorizontal.size()));
        if(verticalEnter == 1) {
            maze[enter][0] = 0;
            maze[enter][1] = 0;
        }
        else {
            maze[0][enter] = 0;
            maze[1][enter] = 0;
        }
        if(verticalExit == 1) {
            maze[exit][width-1] = 0;
            maze[exit][width-2] = 0;
        }
        else {
            maze[height-1][exit] = 0;
            maze[height-2][exit] = 0;
        }
    }

    private static boolean inside(int i, int j) {
        return i < height-1 && i > 0 && j < width-1 && j > 0;
    }

    private static boolean inCorner(int i, int j) {
        return (i == 0 && j == 0)
                || (i == 0 && j == width-1)
                || (i == height-1 && j == 0)
                || (i == height-1 && j == width-1);
    }
}
