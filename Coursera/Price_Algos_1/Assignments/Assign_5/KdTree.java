public class KdTree {
    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;
    private Node root;
    private int size;
    
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding 
                                // to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        
        public Node(Point2D p) {
            this.p = p;
            this.rect = null;
        }
    }
    
    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }
    
    // is the set empty?
    public boolean isEmpty() {
        return (size() == 0);
        
    }
    
    // number of points in the set
    public int size() {
        return size;
    }
    
    private int compare(Point2D p1, Point2D p2, boolean orientation) {
        if (orientation == VERTICAL) {
            if (p1.x() < p2.x()) return -1;
            if (p1.x() > p2.x()) return +1;
        } else {
            if (p1.y() < p2.y()) return -1;
            if (p1.y() > p2.y()) return +1;
        }
        return 0;
    }
    
    private Node insert(Node n, Point2D p, boolean orientation) {
        if (n == null) {
            size++;
            return new Node(p);
        }
        if (!p.equals(n.p)) {
            int cmp = compare(p, n.p, orientation);
            if (cmp < 0) {
                n.lb = insert(n.lb, p, !orientation);
                if (n.lb.rect == null) {
                    if (orientation == VERTICAL) {
                        n.lb.rect = new RectHV(n.rect.xmin(), n.rect.ymin()
                                              , n.p.x(), n.rect.ymax());
                    } else {
                        n.lb.rect = new RectHV(n.rect.xmin(), n.rect.ymin()
                                              , n.rect.xmax(), n.p.y());
                    }
                }
            } else {
                n.rt = insert(n.rt, p, !orientation);
                if (n.rt.rect == null) {
                    if (orientation == VERTICAL) {
                        n.rt.rect = new RectHV(n.p.x(), n.rect.ymin()
                                              , n.rect.xmax(), n.rect.ymax());
                    } else {
                        n.rt.rect = new RectHV(n.rect.xmin(), n.p.y()
                                              , n.rect.xmax(), n.rect.ymax());
                    }
                }   
            }
        }
        return n;
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException("Null Point Passed");
        if (isEmpty()) {
            RectHV rect = new RectHV(0, 0, 1, 1);
            root = new Node(p);
            root.rect = rect;
            size++;
        } else {
            root = insert(root, p, VERTICAL);
        }
    }
    
    private boolean contains(Node n, Point2D p, boolean orientation) {
        if (n == null) return false;
        if (p.equals(n.p)) return true;
        if (compare(p, n.p, orientation) < 0) 
            return contains(n.lb, p, !orientation);
        else 
            return contains(n.rt, p, !orientation);
    }
    
    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException("Null Point Passed");
        return contains(root, p, VERTICAL);
    }
    
    private void draw(Node n, boolean orientation) {
        if (n == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        // make the points a bit larger
        StdDraw.setPenRadius(0.01);  
        n.p.draw();
        // reset the pen radius
        StdDraw.setPenRadius();
        if (orientation == VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
        }
        draw(n.lb, !orientation);
        draw(n.rt, !orientation);
    }
    
    // draw all points to standard draw 
    public void draw() {
        draw(root, VERTICAL);
    }
    
    private void range(Node n, RectHV rect, Queue<Point2D> rangeq) {
        if (n == null) return;
        // Point in the node lies inside the query rectangle
        // Insert the point to the range queue
        if (rect.contains(n.p)) 
            rangeq.enqueue(n.p);
        if (rect.intersects(n.rect)) {
            range(n.lb, rect, rangeq);
            range(n.rt, rect, rangeq);
        }
    }
    
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException("Null Rect Passed");
        Queue<Point2D> rangeq =  new Queue<Point2D>();
        range(root, rect, rangeq);
        return rangeq;
    }
    
    private Point2D nearest(Node n, Point2D p, Point2D nearestpoint
                           , boolean orientation) 
    {
        if (n == null) return nearestpoint;
        double nearestdist = nearestpoint.distanceSquaredTo(p);
        double curdistance = n.p.distanceSquaredTo(p);
        Node firstnode, secondnode;
        Point2D closestpoint = nearestpoint;
        if (curdistance < nearestdist) {
            nearestdist = curdistance;
            closestpoint = n.p;
        }
        
        if (compare(p, n.p, orientation) < 0) {
            firstnode = n.lb;
            secondnode = n.rt;
        } else {
            firstnode = n.rt;
            secondnode = n.lb;
        }
        
        if (firstnode != null 
                && firstnode.rect.distanceSquaredTo(p) < nearestdist)
            closestpoint = nearest(firstnode, p, closestpoint, !orientation);
        if (secondnode != null 
                && secondnode.rect.distanceSquaredTo(p) < nearestdist)
            closestpoint = nearest(secondnode, p, closestpoint, !orientation);
            
        return closestpoint;
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException("Null Point Passed");
        if (isEmpty()) return null;
        return nearest(root, p, root.p, VERTICAL);
    }
    
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);

        // initialize the data structures with N points from standard input
        KdTree kdtree = new KdTree();
 
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        
        StdDraw.clear();
        kdtree.draw();
        StdDraw.show(40);
    }
}