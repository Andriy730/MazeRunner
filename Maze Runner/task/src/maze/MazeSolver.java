package maze;

import java.util.*;
import java.util.stream.Collectors;

public class MazeSolver {

    private static  List<Pair> wayToExit;
    private static int width;

    public static List<Pair> solve(int[][] maze, Pair enter, Pair exit) {
        if(maze.length < 2 || maze[0].length < 2)
            throw new IllegalArgumentException();
        wayToExit = new ArrayList<>();
        width = maze[0].length;
        findEscape(convertMazeToGraph(maze), mapCellToNode(enter), mapCellToNode(exit));
        return wayToExit;
    }

    private static void findEscape(Map<Integer, ArrayList<Integer>> graph, int enter, int exit) {
        Map<Integer, Byte> used = new HashMap<>();
        for(int node: graph.keySet()) {
            used.put(node, (byte)0);
        }
        Queue<Integer> queue = new LinkedList<>();
        List<Integer> path = new ArrayList<>();
        Map<Integer, Integer> prev = new HashMap<>();
        queue.add(enter);
        while(!queue.isEmpty()) {
            int node = queue.poll();
            if(node == exit)
                break;
            if(!graph.containsKey(node))
                continue;
            graph.get(node).forEach(elem -> {
                if(used.containsKey(elem) && used.get(elem) == 0) {
                    used.put(elem, (byte) 1);
                    if(!prev.containsKey(elem) && !Objects.equals(prev.get(node), elem))
                        prev.put(elem, node);
                    queue.add(elem);
                }
            });
        }
        int node = exit;
        path.add(exit);
        while (prev.containsKey(node)){
            node = prev.get(node);
            path.add(node);
        }
        makePath(path);
    }

    private static Map<Integer, ArrayList<Integer>> convertMazeToGraph(int[][] maze) {
        Map<Integer, ArrayList<Integer>> graph = new HashMap<>();
        int height = maze.length;
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                ArrayList<Integer> nodes;
                if(maze[i][j] == 0) {
                    nodes = new ArrayList<>();
                    graph.put(i*width + j, nodes);
                }
                else
                    continue;
                if(i + 1 < height)
                    nodes.add(mapCellToNode(i +1 , j));
                if(i - 1 >= 0)
                    nodes.add(mapCellToNode(i - 1, j));
                if(j + 1 < width)
                    nodes.add(mapCellToNode(i,j + 1));
                if(j - 1 >= 0)
                    nodes.add(mapCellToNode(i, j - 1));
            }
        }
        return graph;
    }

    private static void makePath(List<Integer> nodes) {
        wayToExit.addAll(nodes.stream().map(MazeSolver::mapNodeToCell).collect(Collectors.toList()));
    }

    private static int mapCellToNode(Pair cell) {
        return cell.getFirst()*width + cell.getSecond();
    }
    private static int mapCellToNode(int x, int y) {
        return x*width + y;
    }

    private static Pair mapNodeToCell(int node) {
        return new Pair(node / width, node - (node / width) * width);
    }
}
