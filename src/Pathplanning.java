public class PathPlanning {
    private int numRows;
    private int numCols;
    private final Cell[][] parentCell;// Matrix to store parent cells
    private int[] rowOffSet = { -1, 0, 1, 0 };
    private int[] colOffSet = { 0, 1, 0, -1 };
    private Grid gridMap;

    public PathPlanning(Grid grid) {
        this.gridMap = grid;
        this.numRows = grid.getRows();
        this.numCols = grid.getColoumns();
        this.parentCell = new Cell[numRows][numCols];// Intiazialize parent cell matrix
    }

    public void findPath(int startX, int startY, int endX, int endY) {
        // Initialize parent matrix to null
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                parentCell[i][j] = null;
            }
        }

        // Create a priority queue to store the nodes
        PriorityQueue queue = new PriorityQueue(numRows * numCols);

        // Create the starting cell and add it to the queue
        Cell startCell = new Cell(startX, startY, 0, heuristic(startX, startY, endX, endY));
        queue.enqueue(startCell);

        // Marking the starting cell parent
        parentCell[startX][startY] = startCell;

        // A* algorithm
        while (!queue.isEmpty()) {
            Cell currentCell = queue.dequeue();
            int currentRow = currentCell.row;
            int currentCol = currentCell.col;

            // Check if the goal is reached
            if (currentRow == endX && currentCol == endY) {
                break;
            }

            // Explore neighbor cells
            for (int i = 0; i < 4; i++) {
                int newRow = currentRow + rowOffSet[i];
                int newCol = currentCol + colOffSet[i];

                // Enqueue valid and unvisited cells
                if (gridMap.isValid(newRow, newCol) && gridMap.getGrid()[newRow][newCol] != 3) {
                    int gScore = currentCell.gScore + 1; // Cost from start to the new cell
                    int hScore = heuristic(newRow, newCol, endX, endY); // Heuristic cost estimate
                    int fScore = gScore + hScore;

                    Cell newCell = new Cell(newRow, newCol, gScore, hScore);
                    if (parentCell[newRow][newCol] == null) {
                        parentCell[newRow][newCol] = currentCell;
                        queue.enqueue(newCell);
                    } else if (fScore < parentCell[newRow][newCol].fScore) {
                        parentCell[newRow][newCol] = currentCell;
                        queue.update(newCell);
                    }
                }
            }
        }

        // Printing the path found
        printShortestPath(endX, endY);

    }

    private int heuristic(int x1, int y1, int x2, int y2) {
        // Manhattan distance as the heuristic
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private void printShortestPath(int endRow, int endCol) {
        if (parentCell[endRow][endCol] == null) {
            System.out.println("No path found");
            return;
        }

        System.out.println("Shortest path:");
        Cell[] path = new Cell[numRows * numCols];
        int count = 0;
        Cell current = new Cell(endRow, endCol, 0, 0);
        while (current != null && parentCell[current.row][current.col] != current) {
            path[count++] = current;
            current = parentCell[current.row][current.col];
        }
        // Printing the starting cell first
        if (current != null) {
            System.out.print(current + " -> ");
        }
        for (int i = count - 1; i >= 0; i--) {
            if (i == 0) {
                System.out.print(path[i]);
            } else {
                System.out.print(path[i] + " -> ");
            }
        }
        System.out.println();
        System.out.println("\nGrid with path marked:");
        System.out.println();
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                boolean isPathCell = false;
                for (Cell pathCell : path) {
                    if (pathCell != null && pathCell.row == row && pathCell.col == col) {
                        isPathCell = true;
                        break;
                    }
                }
                if (isPathCell) {
                    System.out.print("X "); // Mark path cells with 'X'
                } else if (gridMap.getGrid()[row][col] == 3) {
                    System.out.print("3 "); // Print obstacles as '#'
                } else {
                    System.out.print("0 "); // Print empty cells as '.'
                }
            }
            System.out.println();
        }
        System.out.println();

    }

    // Cell class to represent a cell within row and column
    private static class Cell {
        final int row;
        final int col;
        final int gScore; // Cost from start to this cell
        final int fScore; // f-score = g-score + h-score

        Cell(int row, int col, int gScore, int hScore) {
            this.row = row;
            this.col = col;
            this.gScore = gScore;
            this.fScore = gScore + hScore;
        }

        @Override
        public String toString() {
            return "(" + row + "," + col + ")";
        }
    }

    // Custom priority queue implementation
    private static class PriorityQueue {
        private Cell[] nodes;
        private int size;

        PriorityQueue(int capacity) {
            nodes = new Cell[capacity];
            size = 0;
        }

        void enqueue(Cell node) {
            if (size == nodes.length) {
                throw new IllegalStateException("Queue is full");
            }
            nodes[size++] = node;
            bubbleUp(size - 1);
        }

        Cell dequeue() {
            if (size == 0) {
                throw new IllegalStateException("Queue is empty");
            }
            Cell node = nodes[0];
            nodes[0] = nodes[--size];
            bubbleDown(0);
            return node;
        }

        void update(Cell node) {
            for (int i = 0; i < size; i++) {
                if (nodes[i].row == node.row && nodes[i].col == node.col) {
                    nodes[i] = node;
                    bubbleUp(i);
                    bubbleDown(i);
                    return;
                }
            }
        }

        boolean isEmpty() {
            return size == 0;
        }

        private void bubbleUp(int index) {
            while (index > 0) {
                int parentIndex = (index - 1) / 2;
                if (nodes[parentIndex].fScore > nodes[index].fScore) {
                    swap(parentIndex, index);
                    index = parentIndex;
                } else {
                    break;
                }
            }
        }

        private void bubbleDown(int index) {
            int leftChildIndex, rightChildIndex, smallerIndex;
            while (true) {
                leftChildIndex = 2 * index + 1;
                rightChildIndex = 2 * index + 2;
                smallerIndex = index;

                if (leftChildIndex < size && nodes[leftChildIndex].fScore < nodes[smallerIndex].fScore) {
                    smallerIndex = leftChildIndex;
                }

                if (rightChildIndex < size && nodes[rightChildIndex].fScore < nodes[smallerIndex].fScore) {
                    smallerIndex = rightChildIndex;
                }

                if (smallerIndex == index) {
                    break;
                }

                swap(index, smallerIndex);
                index = smallerIndex;
            }
        }

        private void swap(int i, int j) {
            Cell temp = nodes[i];
            nodes[i] = nodes[j];
            nodes[j] = temp;
        }
    }
}