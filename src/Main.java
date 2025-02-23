import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    // max length for rows and columns
    static int Maxlen = 1000;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Grid myGrid;

        System.out.print("Do you want to use a predefined grid or create a new one? (Enter 'predefined' or 'new'): ");
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("predefined")) {
            // Define the predefined grid as a 2D array
            int[][] predefinedGrid = {
                    { 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 1, 1, 1, 1, 1, 0 },
                    { 0, 0, 0, 1, 0, 1, 0 },
                    { 0, 1, 0, 1, 0, 1, 0 },
                    { 0, 1, 0, 1, 0, 1, 0 },
                    { 0, 1, 1, 1, 1, 1, 0 },
                    { 0, 0, 0, 0, 0, 0, 0 }
            };

            int noOfRows = predefinedGrid.length;
            int noOfColumns = predefinedGrid[0].length;
            myGrid = new Grid(noOfRows, noOfColumns);

            // Set obstacles based on the predefined grid
            for (int i = 0; i < noOfRows; i++) {
                for (int j = 0; j < noOfColumns; j++) {
                    if (predefinedGrid[i][j] == 1) {
                        myGrid.setObstacles(i, j);
                    }
                }
            }
        } else if (choice.equalsIgnoreCase("new")) {
            System.out.println();
            System.out.println("Notice !!!");
            System.out.println("\"3\" define the  obstacles");
            System.out.println("\"0\" define the empty cells");
            System.out.println("\"1\" define the starting point");
            System.out.println("\"2\" define the ending point");
            System.out.println();
            int noOfRows;
            int noOfColumns;

            // Getting user input and validating dimensions
            do {
                System.out.print("Enter the number of rows you need: ");
                try {
                    noOfRows = scanner.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    scanner.nextLine();
                    System.out.println();
                }
            } while (true);

            do {
                System.out.print("Enter the number of columns you need to add: ");
                try {
                    noOfColumns = scanner.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    scanner.nextLine();
                    System.out.println();
                }
            } while (true);

            myGrid = new Grid(noOfRows, noOfColumns);

            System.out.println();
            while (true) {
                System.out.print("Do you want to add obstacles (yes/no): ");
                String addObs = scanner.next();
                if (addObs.equalsIgnoreCase("yes")) {
                    while (true) {
                        int obsR;
                        int obsC;
                        // Checking the validity of user inputs
                        do {
                            System.out.print("Enter the row and column indices to add an obstacle (space-separated): ");
                            try {
                                obsR = scanner.nextInt();
                                obsC = scanner.nextInt();
                                scanner.nextLine();
                                if (!myGrid.isValid(obsR, obsC)) {
                                    System.out.println("Invalid indices. Enter valid range.");
                                } else {
                                    break;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please enter valid integers separated by a space.");
                                scanner.nextLine();
                                System.out.println();
                            }
                        } while (true);

                        // Setting obstacles
                        myGrid.setObstacles(obsR, obsC);

                        System.out.print("Do you want to add more obstacles (yes/no): ");
                        String response = scanner.nextLine();
                        if (response.equalsIgnoreCase("yes")) {
                            continue;
                        } else if (response.equalsIgnoreCase("no")) {
                            break;
                        } else {
                            System.out.println("Please enter 'yes' or 'no'");
                        }
                    }
                    break;
                } else if (addObs.equalsIgnoreCase("no")) {
                    break;
                } else {
                    System.out.println("Please enter 'yes' or 'no'");
                }
            }
        } else {
            System.out.println("Invalid choice. Exiting...");
            return;
        }

        // Get starting and ending points from the user
        int startR, startC, endR, endC;
        do {
            System.out.print("Enter the row and column indices of the starting point (space-separated): ");
            try {
                startR = scanner.nextInt();
                startC = scanner.nextInt();
                scanner.nextLine();
                if (!myGrid.isValid(startR, startC)) {
                    System.out.println("Invalid indices. Enter valid range.");
                    System.out.println();
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter valid integers separated by a space.");
                scanner.nextLine();
                System.out.println();
            }
        } while (true);

        do {
            System.out.print("Enter the row and column indices of the ending point (space-separated): ");
            try {
                endR = scanner.nextInt();
                endC = scanner.nextInt();
                scanner.nextLine();
                if (!myGrid.isValid(endR, endC)) {
                    System.out.println("Invalid indices. Enter valid range.");
                    System.out.println();
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter valid integers separated by a space.");
                scanner.nextLine();
                System.out.println();
            }
        } while (true);

        // Check if starting point and ending point are the same
        if (startR == endR && startC == endC) {
            System.out.println("Both starting point and ending point are the same.");
            return;
        }

        RobotState robotState = new RobotState(startR, startC, endR, endC);
        robotState.setStart(myGrid.getGrid());
        robotState.setEnd(myGrid.getGrid());

        System.out.println();
        myGrid.display();
        System.out.println();

        PathPlanning pathPlanning = new PathPlanning(myGrid);
        pathPlanning.findPath(startR, startC, endR, endC);
    }

    // Method to check if the entered dimensions exceed the limit
    public static boolean exceedLimit(int i) {
        if (i > Maxlen) {
            System.out.println("Length is too much. Enter a smaller value.");
            return true;
        } else {
            return false;
        }
    }
}