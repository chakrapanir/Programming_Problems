public class PointSET {
    private SET<Point2D> pointset;
    
    // construct an empty set of points
    public PointSET() {
        pointset = new SET<Point2D>();
    }
    
    // is the set empty?
    public boolean isEmpty() {
        return pointset.isEmpty();
    }
    
    // number of points in the set
    public int size() {
        return pointset.size();
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        pointset.add(p);
    }
    
    // does the set contain point p?
    public boolean contains(Point2D p) {
        return pointset.contains(p);
    }
    
    // draw all points to standard draw 
    public void draw() {
        for (Point2D point : pointset) {
            point.draw();
        }
    }
    
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> rangeq = new Queue<Point2D>();
        
        for (Point2D point : pointset) {
            if (rect.contains(point)) {
                rangeq.enqueue(point);
            }
        }
        return rangeq;
    }
    
    
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        Point2D nearestpoint = null;
        for (Point2D point : pointset) {
            if (nearestpoint == null 
                    || (p.DISTANCE_TO_ORDER.compare(point,nearestpoint) < 0)) 
            {
                nearestpoint = point;
            }   
        }
        return nearestpoint;
    }
    
    /*
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        
        StdDraw.show(0);

        // initialize the data structures with N points from standard input
        PointSET brute = new PointSET();
 
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }

        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        brute.draw();
        StdDraw.show(0);
    }
    */
}