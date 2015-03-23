public class Board {
    private final short[][] tiles;    // N-by-N array of board blocks
    private final int N;              // Board dimension
    private int blankrow;             // rwo number where blank is present
    private int blankcol;
    private int hammingdistance;      // number of blocks in the wrong position
    private int manhattandistance;    // sum of the Manhattan distances from 
                                      // the blocks to their goal positions
    
    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        N = blocks.length;
        tiles = new short[N][N];
        hammingdistance = 0;
        manhattandistance = 0;
        
        for (int  i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                short value = (short) blocks[i][j];
                
                tiles[i][j] = value;
                if (value == 0) {
                    blankrow = i;
                    blankcol = j;
                } else {
                    // Calculate hamming distance
                    if (value != ((i * N) + j +1)) hammingdistance++;
                    
                    // Calculate manhattan distance
                    int refrow = (value - 1) / N;
                    int refcol = (value - 1) % N;
                    
                    manhattandistance += Math.abs(i - refrow) + Math.abs(j - refcol);
                }
            }
        }           
    }
    
    // board dimension N
    public int dimension() {
        return N;
    }
    
    // number of blocks out of place
    public int hamming() {
        return hammingdistance;
    }
    
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhattandistance;
    }
    
    // is this board the goal board?
    public boolean isGoal() {
        return hammingdistance == 0;
        
    }
    
    // a board that is obtained by exchanging two adjacent blocks in the same row
    public Board twin() {
        int[][] tempblocks = new int[N][N];
        Board twinboard;
        
        copyBlocks(tempblocks);
        if (blankrow != 0) {
            exch(tempblocks, 0, 0, 0, 1);
        } else {
            exch(tempblocks, 1, 0, 1, 1);
        }
        twinboard = new Board(tempblocks);
        return twinboard;
    }
    
    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        
        // First check if the dimensions are equal before 
        // looking at the individual elements
        if (this.dimension() != that.dimension()) return false;
        
        // Check if the hamming an manhattan distances are equal
        if (this.hammingdistance != that.hammingdistance 
                || this.manhattandistance != that.manhattandistance) return false;
        
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.tiles[i][j] != that.tiles[i][j]) return false; 
            }
        }
        
        return true;
    }
    
    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neighborsq = new Queue<Board>();
        int[][] tempblocks = new int[N][N];
        copyBlocks(tempblocks);
        
        // Find neighbor by making one move down
        if (blankrow != (N-1)) {
            exch(tempblocks, blankrow, blankcol, blankrow+1, blankcol);
            Board neighborboard = new Board(tempblocks);
            neighborsq.enqueue(neighborboard);
            exch(tempblocks, blankrow, blankcol, blankrow+1, blankcol);
        }
        
        // Find neighbor by making one move to the right
        if (blankcol != (N-1)) {
            exch(tempblocks, blankrow, blankcol, blankrow, blankcol+1);
            Board neighborboard = new Board(tempblocks);
            neighborsq.enqueue(neighborboard);
            exch(tempblocks, blankrow, blankcol, blankrow, blankcol+1);
        }
        
        // Find neighbor by making one move up
        if (blankrow != 0) {
            exch(tempblocks, blankrow, blankcol, blankrow-1, blankcol);
            Board neighborboard = new Board(tempblocks);
            neighborsq.enqueue(neighborboard);
            exch(tempblocks, blankrow, blankcol, blankrow-1, blankcol);
        }
        
        // Find neighbor by making one move to the left
        if (blankcol != 0) {
            exch(tempblocks, blankrow, blankcol-1, blankrow, blankcol);
            Board neighborboard = new Board(tempblocks);
            neighborsq.enqueue(neighborboard);
            exch(tempblocks, blankrow, blankcol-1, blankrow, blankcol);
        }
        
        return neighborsq;
    }
    
    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    private void copyBlocks(int[][] blocks) {
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                blocks[i][j] = tiles[i][j];
            }
        }
    }
    
    private void exch(int[][] board, int firstrow, int firstcol, 
                      int secondrow, int secondcol) 
    {
        int temp = board[firstrow][firstcol];
        board[firstrow][firstcol] = board[secondrow][secondcol];
        board[secondrow][secondcol] = temp;
    }
    
    /*
    // unit tests (not graded)
    public static void main(String[] args) {
        
        // Create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks =  new int[N][N];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);
        StdOut.println(initial.toString());
        StdOut.println("Hamming: "+initial.hamming());
        StdOut.println("Manhattan: " + initial.manhattan());
        StdOut.println("Neighbors: ");
        for (Board b : initial.neighbors()) {
            StdOut.println(b.toString());
        }
        Board twin = initial.twin();
        StdOut.println("Twin: ");
        StdOut.println(twin.toString());
    }
    */
}

