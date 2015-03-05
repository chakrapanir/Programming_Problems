/******************************************************************************
 *  Compilation:  javac Fast.java
 *  Execution:    java Fast input.txt
 *  Dependencies: Point.java, In.java, StdDraw.java
 * 
 *  Takes the name of a file as a command-line argument.
 *  Reads in an integer N followed by N pairs of points (x, y)
 *  with coordinates between 0 and 32,767.
 * 
 *  A faster sorting based solution to find sets of 4 or more colinear points.  
 *  All the colinear line segments are printed on standard output. 
 *  The line segements are drawn using standard drawing
 *****************************************************************************/
import java.util.Arrays;
    
public class Fast {
    private static boolean less(Point v, Point w) {
        return v.compareTo(w) < 0;
    }
    
    private static boolean equals(Point v, Point w) {
        return v.compareTo(w) == 0;
    }
    
    public static void main(String[] args) {
        // read in the input
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Point[] points;
        Point[] sortpoints;
        Point min;
        
        // Input points less than minimum required
        if (N < 4) return;
        points = new Point[N];
        sortpoints = new Point[N];
        
        // rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);
        StdDraw.setPenRadius(0.01);  // make the points a bit larger
        
        // Parse the input file and populate the points array
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
            points[i].draw();
        }
        
        // display to screen all at once
        StdDraw.show(0);
        // reset the pen radius
        StdDraw.setPenRadius();
        Arrays.sort(points);
        for (int i = 0; i < N; i++) 
            sortpoints[i] = points[i];
        for (int i = 0; i < N; i++) {
            Arrays.sort(sortpoints, points[i].SLOPE_ORDER);
            min = sortpoints[0];
            // tracks the number of collinear points
            // including the reference point (points[i])
            int collinearpoints = 1; 
            for (int j = 1; j < N; j++) {
                // Iterate over the slope sorted array and check if the 
                // slope of the adjacent elements is the same.
                if (points[i].slopeTo(sortpoints[j]) 
                        != points[i].slopeTo(sortpoints[j-1])) 
                {
                    // Print and draw when the number of collinear points
                    // is greater than 4 and the reference element is 
                    // the minimum element. Ignore all other combinations.
                    if (collinearpoints > 3 && equals(points[i], min)) 
                    {
                        int start = j - (collinearpoints - 1);
                        Arrays.sort(sortpoints, start, j);
                        StdOut.print(points[i].toString());
                        for (int k = 0; k < collinearpoints-1; k++) {
                            StdOut.print(" -> " + sortpoints[start + k]);
                        }
                        StdOut.print("\n");
                        StdDraw.setPenColor(StdDraw.BLUE);
                        points[i].drawTo(sortpoints[j-1]);
                        // display to screen all
                        StdDraw.show(0);
                    }
                    collinearpoints = 1;
                    min = sortpoints[0];
                }
                if (less(sortpoints[j], min)) min = sortpoints[j];
                collinearpoints++;
            }
            if (collinearpoints > 3 && equals(points[i], min))
            {
                int start = N - (collinearpoints - 1);
                Arrays.sort(sortpoints, start, N);
                StdOut.print(points[i].toString());
                for (int k = 0; k < collinearpoints-1; k++) {
                    StdOut.print(" -> " + sortpoints[start + k]);
                }
                StdOut.print("\n");
                StdDraw.setPenColor(StdDraw.BLUE);
                points[i].drawTo(sortpoints[N-1]);
                // display to screen
                StdDraw.show(0);
            }
        }
    }
}