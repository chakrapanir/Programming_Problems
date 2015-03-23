public class Board {
    private final short[] tiles;    // 1D array to store N-by-N array of board blocks
    private final int N;          // Board dimension
    private int blankidx;         // position where blank is present
    private int hammingdistance;  // number of blocks in the wrong position
    private int manhattandistance; // sum of the Manhattan distances from 
                                   // the blocks to their goal positions
    
    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        N = blocks.length;
        tiles = new short[N*N];
        hammingdistance = 0;
        manhattandistance = 0;
        
        for (int  i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                short value = (short) blocks[i][j];
                int tileidx = (i*N)+j;
                
                tiles[tileidx] = value;
                if (value == 0) {
                    blankidx = tileidx;
                } else {
                    // Calculate hamming distance
                    if (value != (tileidx +1)) hammingdistance++;
                    
                    // Calculate manhattan distance
                    int refrow = (value - 1) / N;
                    int refcol = (value - 1) % N;
                    
                    manhattandistance += Math.abs(i - refrow) + Math.abs(j - refcol);
                }
            }
        }           
    }
    
    // Private Constructor for Twin
    private Board(Board origboard, int swapidx1, int swapidx2) {
        this.N = origboard.N;
        tiles = new short[N*N];
        hammingdistance = 0;
        manhattandistance = 0;
        
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                int tileidx = (i*N)+j;
                short value;
                if (tileidx == swapidx1) {
                    value = origboard.tiles[swapidx2];
                } else if (tileidx == swapidx2) {
                    value = origboard.tiles[swapidx1];
                } else {
                    value = origboard.tiles[tileidx];
                }
                tiles[tileidx] = value;
                if (value == 0) {
                    blankidx = tileidx;
                } else {
                    // Calculate hamming distance
                    if (value != tileidx+1) hammingdistance++;
                
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
        Board twinboard;
        
        if (blankidx < N) {
            twinboard = new Board(this, N, N+1);
        } else {
            twinboard = new Board(this, 0, 1);
        }
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
        
        for (int i = 0; i < (N*N); i++) {
            if (this.tiles[i] != that.tiles[i]) return false; 
        }
        
        return true;
    }
        
    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neighborsq = new Queue<Board>();
        
        // Find neighbor by making one move down
        if (blankidx < (N-1)*N) {
            int swapidx = blankidx+N;
            Board neighborboard = new Board(this, blankidx, swapidx);
            neighborsq.enqueue(neighborboard);
        }
        
        // Find neighbor by making one move to the right
        if ((blankidx+1) % N != 0) {
            int swapidx = blankidx+1;
            Board neighborboard = new Board(this, blankidx, swapidx);
            neighborsq.enqueue(neighborboard);
            
        }
        
        // Find neighbor by making one move up
        if (blankidx >= N) {
            int swapidx = blankidx-N;
            Board neighborboard = new Board(this, blankidx, swapidx);
            neighborsq.enqueue(neighborboard);
        }
        
        // Find neighbor by making one move to the left
        if (blankidx % N != 0) {
            int swapidx = blankidx-1;
            Board neighborboard = new Board(this, blankidx, swapidx);
            neighborsq.enqueue(neighborboard);
        }
        
        return neighborsq;
    }
    
    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tiles[(i*N)+j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    
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
            StdOut.println("Neighbors Hamming: "+b.hamming());
            StdOut.println("Neighbors Manhattan: " +b.manhattan());
        }
        
        Board twin = initial.twin();
        StdOut.println("Twin: ");
        StdOut.println(twin.toString());
        StdOut.println("Twin Hamming: "+twin.hamming());
        StdOut.println("Twin Manhattan: " + twin.manhattan());
    }
    
}

