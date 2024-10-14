import java.util.*;

// Hlavní třída
public class Bludiste {

    static final int WIDTH = 5;
    static final int HEIGHT = 5;
    static final int WALL = 1;
    static final int PATH = 0;

    static int[][] maze;
    static boolean[][] visited;

    
    static final int[] dx = {-1, 1, 0, 0};
    static final int[] dy = {0, 0, -1, 1};

    public static void main(String[] args) {
        maze = new int[HEIGHT][WIDTH];
        visited = new boolean[HEIGHT][WIDTH];

        
        for (int i = 0; i < HEIGHT; i++) {
            Arrays.fill(maze[i], WALL);
        }

        generateGuaranteedPath(0, 0, HEIGHT - 1, WIDTH - 1);
        generateMaze(0, 0);

        System.out.println("Bludiště:");
        printMaze();

        System.out.println("\nVyřešené Bludiště:");
        solveMazeBFS(0, 0, HEIGHT - 1, WIDTH - 1);
    }

    public static void generateGuaranteedPath(int startX, int startY, int endX, int endY) {
        int x = startX;
        int y = startY;
        maze[x][y] = PATH;
        visited[x][y] = true;

        Random rand = new Random();
        while (x != endX || y != endY) {
            if (rand.nextBoolean()) { 
                if (x < endX) x++;
                else if (x > endX) x--;
            } else {
                if (y < endY) y++;
                else if (y > endY) y--;
            }
            maze[x][y] = PATH;
            visited[x][y] = true;
        }
    }
    public static void generateMaze(int x, int y) {
        List<Integer> directions = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(directions);

        for (int dir : directions) {
            int newX = x + dx[dir];
            int newY = y + dy[dir];

            if (isValid(newX, newY) && !visited[newX][newY]) {
                if (Math.random() < 0.7) { 
                    maze[newX][newY] = WALL;
                } else {
                    maze[newX][newY] = PATH;
                }
                visited[newX][newY] = true;
                generateMaze(newX, newY); 
            }
        }
    }
    public static boolean isValid(int x, int y) {
        return x >= 0 && y >= 0 && x < HEIGHT && y < WIDTH;
    }
    public static void solveMazeBFS(int startX, int startY, int endX, int endY) {
        boolean[][] visitedBFS = new boolean[HEIGHT][WIDTH];
        int[][] parentX = new int[HEIGHT][WIDTH];
        int[][] parentY = new int[HEIGHT][WIDTH];

        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{startX, startY});
        visitedBFS[startX][startY] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0], y = current[1];

            if (x == endX && y == endY) {
                break; 
            }
            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];

                if (isValid(newX, newY) && !visitedBFS[newX][newY] && maze[newX][newY] == PATH) {
                    queue.offer(new int[]{newX, newY});
                    visitedBFS[newX][newY] = true;
                    parentX[newX][newY] = x;
                    parentY[newX][newY] = y;
                }
            }
        }
        int x = endX, y = endY;
        while (x != startX || y != startY) {
            maze[x][y] = 2; 
            int tempX = parentX[x][y];
            int tempY = parentY[x][y];
            x = tempX;
            y = tempY;
        }
        maze[startX][startY] = 3; 
        maze[endX][endY] = 4;     

        printMaze();
    }

    public static void printMaze() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (maze[i][j] == WALL) {
                    System.out.print("# ");
                } else if (maze[i][j] == PATH) {
                    System.out.print(". ");
                } else if (maze[i][j] == 2) {
                    System.out.print("o "); 
                } else if (maze[i][j] == 3) {
                    System.out.print("S "); 
                } else if (maze[i][j] == 4) {
                    System.out.print("K "); 
                }
            }
            System.out.println();
        }
    }
}
