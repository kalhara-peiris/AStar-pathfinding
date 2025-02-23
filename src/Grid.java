public class Grid {
    private int[][] grid;
    private int rows;
    private int coloumns;

    public Grid(int rows, int coloumns) {
        this.rows = rows;
        this.coloumns = coloumns;
        grid = new int[rows][coloumns];
    }

    public boolean isValid(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < coloumns;
    }

    public void setObstacles(int row, int coloumn) {
        if (isValid(row, coloumn)) {
            grid[row][coloumn] = 3;
        }
    }

    public boolean isObstacle(int r, int c) {
        return grid[r][c] == 3;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getRows() {
        return rows;
    }

    public int getColoumns() {
        return coloumns;
    }

    public void display() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }

    }
}