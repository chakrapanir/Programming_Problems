/*************************************************************************
 *  Compilation:  javac Brute.java
 *  Execution:    java Brute input.txt
 *  Dependencies: Point.java, In.java, StdDraw.java
 * 
 *  Takes the name of a file as a command-line argument.
 *  Reads in an integer N followed by N pairs of points (x, y)
 *  with coordinates between 0 and 32,767.
 * 
 *  The brute force algorithm to find sets of 4 colinear points examines 
 *  4 points at a time and checks whether they all lie on the 
 *  same line segment. All such line segments are printed on standard
 *  output. The line segements are drawn using standard drawing
 *************************************************************************/

public class Brute {
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
        Point min;
        Point max;
        
        points = new Point[N];
        // Input points less than minimum required
        if (N < 4) return;
        
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
 
        for (int i = 0; i < N-3;i++) {
            for (int j = i+1; j < N-2; j++) {
                for (int k = j+1; k < N-1; k++) {
                    // If three points are not collienar, no point in checking the 
                    // fourth point. Move onto the next one
                    if (points[i].slopeTo(points[j]) != points[i].slopeTo(points[k])) continue;
                    for (int l = k+1; l < N; l++) {
                        if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[k]) && points[i].slopeTo(points[k]) == points[i].slopeTo(points[l])) 
                        {
                            // Points are i, j, k and l are collinear. Find the minimum and maximum points to draw a line segment
                            min = points[i];
                            max = points[i];
                            if (points[j].compareTo(min) == -1) min = points[j];
                            if (points[j].compareTo(max) == 1) max = points[j];
                            if (points[k].compareTo(min) == -1) min = points[k];
                            if (points[k].compareTo(max) == 1) max = points[k];
                            if (points[l].compareTo(min) == -1) min = points[l];
                            if (points[l].compareTo(max) == 1) max = points[l];
                            //StdOut.println("Min: "+min.toString() +" Max: "+max.toString());
                            // Print and Draw
                            StdOut.println(points[i].toString() + " -> " + points[j].toString() + " -> " + points[k].toString() + " -> " + points[l].toString());
                            StdDraw.setPenColor(StdDraw.BLUE);
                            min.drawTo(max);
                        }
                    }
                }
            }
        }
        // display to screen all at once
        StdDraw.show(0);
    }
}
