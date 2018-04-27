import edu.princeton.cs.algs4.WeightedQuickUnionUF;
/**
 * Programming assignment 1 from course.
 * */
public class Percolation {
    private boolean[][] grid;
    private int openSites;
    private int n;
    private WeightedQuickUnionUF unionFind;
    private WeightedQuickUnionUF checkIsFull;
//    private boolean someBottomIsFull;

    public Percolation(int n) {

        if (n <= 0){
            throw new IllegalArgumentException("n is too small");
        }
        else {
            grid = new boolean[n + 1][n + 1];
            openSites = 0;
            this.n = n;
//            someBottomIsFull = false;
            //grid boxes labelled from 1 to n*n, 0 is upper-connector, n*n + 1 is lower connecter.
            unionFind = new WeightedQuickUnionUF(n*n + 2);
            checkIsFull = new WeightedQuickUnionUF(n*n + 2);

            for (int i = 1; i <= n; i++) {
                unionFind.union(0, i);
                checkIsFull.union(0, i);
                unionFind.union(n*n + 1, n*n - (i - 1));
            }
        }
    }

    public void open(int row, int column) {
        if (row < 1 || row > grid.length - 1 || column < 1 || column > grid.length - 1) throw new IllegalArgumentException("dimensions out of range");
        else if (!isOpen(row, column)){
            openSites++;
            grid[row][column] = true;
            int num = findCorrespondingNumber(row, column);
            if (isDimensionWithinBounds(row - 1, column) && isOpen(row - 1, column)) {
                unionFind.union(findCorrespondingNumber(row - 1, column), num);
                checkIsFull.union(findCorrespondingNumber(row - 1, column), num);

            }
            if (isDimensionWithinBounds(row + 1, column) && isOpen(row + 1, column)){
                unionFind.union(findCorrespondingNumber(row + 1, column), num);
                checkIsFull.union(findCorrespondingNumber(row + 1, column), num);
            }
            if (isDimensionWithinBounds(row, column - 1) && isOpen(row, column - 1)) {
                unionFind.union(findCorrespondingNumber(row, column - 1), num);
                checkIsFull.union(findCorrespondingNumber(row, column - 1), num);
            }
            if (isDimensionWithinBounds(row, column + 1) && isOpen(row, column + 1)) {
                unionFind.union(findCorrespondingNumber(row, column + 1), num);
                checkIsFull.union(findCorrespondingNumber(row, column + 1), num);
            }

//            if (row == n) someBottomIsFull = isFull(row, column);
        }
    }

    public boolean isOpen(int row, int column) {
        if (row < 1 || row > grid.length - 1 || column < 1 || column > grid.length - 1) throw new IllegalArgumentException("dimensions out of range");
        else return grid[row][column];
    }

    public boolean isFull(int row, int column) {
        if (row < 1 || row > grid.length - 1 || column < 1 || column > grid.length - 1) throw new IllegalArgumentException("dimensions out of range");
        else {
            int num = findCorrespondingNumber(row, column);
            return isOpen(row, column) && checkIsFull.connected(num, 0);
        }
    }

    public int numberOfOpenSites(){
        return openSites;
    }

    public boolean percolates(){
        if (n == 1) return isFull(1,1);
        return unionFind.connected(0, n*n + 1);
    }

//    public boolean percolates(){
//        return someBottomIsFull;
//    }

    private int findCorrespondingNumber(int row, int column){
        if (row < 1 || row > grid.length - 1 || column < 1 || column > grid.length - 1) throw new IllegalArgumentException("dimensions out of range");
        else return column + (grid.length - 1)*(row - 1);
    }

    private boolean isDimensionWithinBounds(int row, int column){
        return row >= 1 && row <= grid.length - 1 && column >= 1 && column <= grid.length - 1;
    }
    
    public static void main(String[] args) {
        Percolation percolation = new Percolation(1);
        System.out.println(percolation.percolates());
    }
}
