/*************************************************************************
 * Name:
 * Email:
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new SlopeOrder();

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        // Horizontal Line
        if (this.y == that.y) return 0.0;
        // Vertical Line
        if (this.x == that.x) return Double.POSITIVE_INFINITY;
        // Degenerate Line Segment
        if (equals (that)) return Double.NEGATIVE_INFINITY;
        
        return ((double)(that.y - this.y)/(double)(that.x - this.x));
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        if (this.y < that.y) return -1;
        if (this.y > that.y) return +1;
        if (this.x < that.y) return -1;
        if (this.x > that.y) return +1;
        return 0;
    }
    
    /* Does this point equal the passed point
     * @param other other point
     * @return true if this point equals other; false otherwise
     */
    public boolean equals (Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Point that = (Point) other;
        return this.x == that.x && this.y == that.y;
    }
    
    private class SlopeOrder implements Comparator<Point> 
    {
        public int compare(Point p1, Point p2) 
        {
            double slope1 = slopeTo(p1);
            double slope2 = slopeTo(p2);
            if (slope1 < slope2) return -1;
            if (slope1 > slope2) return +1;
            return 0;
        }
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // unit test
    public static void main(String[] args) {
        /* YOUR CODE HERE */
    }
}
