/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nguyen_gameoflife;

/**
 *
 * @author Edward
 */
public class Life implements LifeInterface {

    int[][] grid = new int[50][50];

    public Life() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = 0;
            }
        }
    }

    public Life(int[][] start) {
        grid = start;
    }

    @Override
    public String toString() {
        String out = "";
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                out += grid[i][j] + " ";
            }
            out += "\n";
        }
        return out;
    }

    @Override
    public void killAllCells() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = 0;
            }
        }
    }

    @Override
    public void setPattern(int[][] startGrid) {
        grid = startGrid;
    }

    @Override
    public int countNeighbours(int cellRow, int cellCol) {
        int neighbours = 0;

        if (cellRow == 0) {
            if (cellCol == 0) {
                for (int i = cellRow; i <= cellRow + 1; i++) {
                    for (int j = cellCol; j <= cellCol + 1; j++) {
                        if (grid[i][j] == 1) {
                            neighbours++;
                        }
                    }
                }
            } else if (cellCol == grid.length - 1) {
                for (int i = cellRow; i <= cellRow + 1; i++) {
                    for (int j = cellCol - 1; j <= cellCol; j++) {
                        if (grid[i][j] == 1) {
                            neighbours++;
                        }
                    }
                }
            } else {
                for (int i = cellRow; i <= cellRow + 1; i++) {
                    for (int j = cellCol - 1; j <= cellCol + 1; j++) {
                        if (grid[i][j] == 1) {
                            neighbours++;
                        }
                    }
                }
            }
        } else if (cellRow == grid.length - 1) {
            if (cellCol == 0) {
                for (int i = cellRow - 1; i <= cellRow; i++) {
                    for (int j = cellCol; j <= cellCol + 1; j++) {
                        if (grid[i][j] == 1) {
                            neighbours++;
                        }
                    }
                }
            } else if (cellCol == grid[0].length - 1) {
                for (int i = cellRow - 1; i <= cellRow; i++) {
                    for (int j = cellCol - 1; j <= cellCol; j++) {
                        if (grid[i][j] == 1) {
                            neighbours++;
                        }
                    }
                }
            } else {
                for (int i = cellRow - 1; i <= cellRow; i++) {
                    for (int j = cellCol - 1; j <= cellCol + 1; j++) {
                        if (grid[i][j] == 1) {
                            neighbours++;
                        }
                    }
                }
            }
        } else if (cellCol == 0) {
            for (int i = cellRow - 1; i <= cellRow + 1; i++) {
                for (int j = cellCol; j <= cellCol + 1; j++) {
                    if (grid[i][j] == 1) {
                        neighbours++;
                    }
                }
            }
        } else if (cellCol == grid[0].length - 1) {
            for (int i = cellRow - 1; i <= cellRow + 1; i++) {
                for (int j = cellCol - 1; j <= cellCol; j++) {
                    if (grid[i][j] == 1) {
                        neighbours++;
                    }
                }
            }
        } else {
            for (int i = cellRow - 1; i <= cellRow + 1; i++) {
                for (int j = cellCol - 1; j <= cellCol + 1; j++) {
                    if (grid[i][j] == 1) {
                        neighbours++;
                    }
                }
            }
        }
        if (grid[cellRow][cellCol] == 1) {
            neighbours--;
        }
        return neighbours;
    }

    @Override
    public int applyRules(int cellRow, int cellCol) {
        Life board = new Life(grid);
        int cell = 0;
        if (grid[cellRow][cellCol] == 1) {
            if (board.countNeighbours(cellRow, cellCol) < 2) {
                cell = 0;
            } else if (board.countNeighbours(cellRow, cellCol) == 2 || board.countNeighbours(cellRow, cellCol) == 3) {
                cell = 1;
            } else if (board.countNeighbours(cellRow, cellCol) > 3) {
                cell = 0;
            }
        } else if (grid[cellRow][cellCol] == 0) {
            if (board.countNeighbours(cellRow, cellCol) == 3) {
                cell = 1;
            }
        }
        return cell;
    }

    @Override
    public void takeStep() {
        Life board = new Life(grid);
        int[][] tempGrid = new int[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
//                if (grid[i][j] == 1) {
//                    tempGrid[i][j] = 1;
//                }
                tempGrid[i][j] = board.applyRules(i, j);
                board = new Life(grid);
            }
        }
        grid = tempGrid;
    }

}
