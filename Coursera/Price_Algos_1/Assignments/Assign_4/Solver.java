import java.util.Comparator;

public class Solver {
    private final boolean solvable;
    private final int moves;
    private Stack<Board> pathstack;
    
    private class SearchNode implements Comparable<SearchNode> {
       private final Board board;   // game board
       private final int moves;     // number of moves made to reach the board
       private final SearchNode parentnode;  // previous search node
       private final int mandistance;
       private final int manpriority;
       
       public SearchNode(Board board, SearchNode parent) {
           this.board = board;
           this.parentnode = parent;
           if (parent != null) this.moves = parent.getMoves()+1;
           else this.moves = 0;
           this.mandistance = this.board.manhattan();
           this.manpriority = this.mandistance + moves;
       }
       
       // Is this Search Node's priority lower than that one
       // Compare using Manhattan Priority and break ties with hamming distance
       public int compareTo(SearchNode that) {
           if (this.manpriority < that.manpriority) return -1;
           if (this.manpriority > that.manpriority) return +1;
           if (this.mandistance < that.mandistance) return -1;
           if (this.mandistance > that.mandistance) return +1;
           return 0;
       }
       // return board
       public Board getBoard() {
           return board;
       }
       
       /// return moves for search node
       public int getMoves() {
           return moves;
       }
       
       // return parent of the search node
       public SearchNode getParent() {
           return parentnode;
       }
    }
    
    private class ManPriorityOrder implements Comparator<SearchNode> {
        public int compare(SearchNode x, SearchNode y) {
            return x.compareTo(y);
        }  
    }
   
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        Comparator<SearchNode> MANPRIORITYORDER = new ManPriorityOrder();
        MinPQ<SearchNode> boardminpq = new MinPQ<SearchNode>(MANPRIORITYORDER);
        MinPQ<SearchNode> twinminpq = new MinPQ<SearchNode>(MANPRIORITYORDER);
        SearchNode nextnode = new SearchNode(initial, null);
        SearchNode twinnextnode = new SearchNode(initial.twin(), null);
        boardminpq.insert(nextnode);
        twinminpq.insert(twinnextnode);
        pathstack = new Stack<Board>();
        //int inserts = 1, del = 0;
        
        // Loop until the goal game board for the initial borad 
        // or its twin is reached
        while (!nextnode.getBoard().isGoal() && !twinnextnode.getBoard().isGoal()) {
            nextnode = boardminpq.delMin();
            //del++;
            for (Board neighbor : nextnode.getBoard().neighbors()) {
                // Critical optimization: Enqueue a neighbor only if its board 
                // is the different from the board of the previous search node.
                if (nextnode.getParent() == null 
                        || !neighbor.equals(nextnode.getParent().getBoard())) 
                {
                    SearchNode neighbornode = new SearchNode(neighbor, nextnode);
                    boardminpq.insert(neighbornode);
                    //inserts++;
                }
            }
            
            // run the A* algorithm on the twin board
            twinnextnode = twinminpq.delMin();
            //del++;
            for (Board neighbor : twinnextnode.getBoard().neighbors()) {
                // Critical optimization: Enqueue a neighbor only if its board 
                // is the different from the board of the previous search node.
                if (twinnextnode.getParent() == null 
                        || !neighbor.equals(twinnextnode.getParent().getBoard())) {
                    SearchNode neighbornode = new SearchNode(neighbor, twinnextnode);
                    twinminpq.insert(neighbornode);
                    //inserts++;
                }
            }
        }
        
        if (nextnode.getBoard().isGoal()) {
            moves = nextnode.getMoves();
            solvable = true;
            while (nextnode != null) {
                pathstack.push(nextnode.getBoard());
                nextnode = nextnode.getParent();
            }
        } else {
            moves = -1;
            solvable = false;
            pathstack = null;
        }   
    }
    
    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }
    
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }
    
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return pathstack;
    }
    
    // solve a slider puzzle
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
        
        Stopwatch watch = new Stopwatch();
        Board initial = new Board(blocks);
        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
        
        StdOut.println("Exec time: "+watch.elapsedTime());
    }
}