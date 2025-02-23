public class RobotState {
    private int startR, startC;
    private int endR, endC;

    public RobotState(int startR, int startC, int endR, int endC) {
        this.startR = startR;
        this.startC = startC;
        this.endR = endR;
        this.endC = endC;
    }

    public void setStart(int[][] grid) {
        grid[startR][startC] = 1;
    }

    public void setEnd(int[][] grid) {
        grid[endR][endC] = 2;
    }
}