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
    public static void main(String[] args) {
        // rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);
        StdDraw.setPenRadius(0.01);  // make the points a bit larger

        // read in the input
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Point[] points;
        Point[] sortpoints;
        Point min;
        Point max;
        
        points = new Point[N];
        sortpoints = new Point[N];
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
        for(int j = 0; j < N; j++) sortpoints[j] = points[j];
        for (int i = 0; i < N; i++) {
            StdOut.println("Sort w.r.t"+points[i].toString());
            Arrays.sort(sortpoints, points[i].SLOPE_ORDER);
            min = sortpoints[0];
            max = sortpoints[0];
            int collinearpoints = 1;
            for (int k = 1; k < N-1; k++) {
                StdOut.println(sortpoints[k].toString());
                if (sortpoints[k].compareTo(min) == -1) min = sortpoints[k];
                if (sortpoints[k].compareTo(max) == 1) max = sortpoints[k];
                collinearpoints++;
                if (points[i].slopeTo(sortpoints[k]) != points[i].slopeTo(sortpoints[k+1])) 
                {
                    StdOut.println("Min"+min.toString());
                    StdOut.println("Max"+max.toString());
                    if (collinearpoints > 3 && points[i].equals(min)) {
                        StdDraw.setPenColor(StdDraw.BLUE);
                        min.drawTo(max);
                    }
                    collinearpoints = 1;
                    min = sortpoints[0];
                    max = sortpoints[0];
                    continue;
                }
            }
            
        }
        // display to screen all at once
        StdDraw.show(0);
    }
}