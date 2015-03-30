public class KdTree {
    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;
    private Node root;
    private int size;
    
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        
        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }
    
    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }
    
    // is the set empty?
    public boolean isEmpty() {
        return size()==0;
        
    }
    
    // number of points in the set
    public int size() {
        return size;
    }
    
    private Node insert(Node n, Point2D p, RectHV rect, boolean orientation) {
        if (n == null) {
            StdOut.println("Point: "+p.toString()+", Rect: "+rect.toString());
            return new Node(p, rect);
        }
        if (!p.equals(n.p)) {
            RectHV newrect;
            if (orientation == VERTICAL) {
                if (p.X_ORDER.compare(p, n.p) < 0) {
                    newrect = new RectHV(rect.xmin(), rect.ymin(), n.p.x(), rect.ymax());
                    n.lb = insert(n.lb, p, newrect, HORIZONTAL);
                } else {
                    newrect = new RectHV(n.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
                    n.rt = insert(n.rt, p, newrect, HORIZONTAL);
                }
            } else {
                if (p.Y_ORDER.compare(p, n.p) < 0) {
                    newrect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), n.p.y());
                    n.lb = insert(n.lb, p, newrect, VERTICAL);
                } else {
                    newrect = new RectHV(rect.xmin(), n.p.y(), rect.xmax(), rect.ymax());
                    n.rt = insert(n.rt, p, newrect, VERTICAL);
                }
            }
        }
        return n;
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        RectHV rect = new RectHV(0, 0, 1, 1);
        root = insert(root, p, rect, VERTICAL);
    }
    
    private boolean contains(Node n, Point2D p, boolean orientation) {
        if (n==null) return false;
        if (p.equals(n.p)) return true;
        int cmp;
        if (orientation == VERTICAL) cmp = p.X_ORDER.compare(p,n.p);
        else cmp = p.Y_ORDER.compare(p,n.p);
        if (cmp < 0) return contains(n.lb, p, !orientation);
        else return contains(n.rt, p, !orientation);
    }
    
    // does the set contain point p?
    public boolean contains(Point2D p) {
        return contains(root, p, VERTICAL);
    }
    
    private void draw(Node n, boolean orientation) {
        if (n==null) return;
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
        if (n==null) return;
        // Point in the node lies inside the query rectangle
        // Insert the point to the range queue
        if (rect.contains(n.p)) 
            rangeq.enqueue(n.p);
        // Query rectangle intersects the rectange corressponding to left-bottom node
        // Recursively serach bottom-left subtree
        if (n.lb!=null && rect.intersects(n.lb.rect))
            range(n.lb, rect, rangeq);
        // Query rectangle intersects the rectange corressponding to right-top node
        // Recursively serach right-top subtree
        else if (n.rt!=null && rect.intersects(n.rt.rect))
            range(n.rt, rect, rangeq);
    }
    
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> rangeq =  new Queue<Point2D>();
        range(root, rect, rangeq);
        return rangeq;
    }
    
    /*
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
       
    }
    */
    
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
            StdOut.println(p.toString()+"->"+kdtree.contains(p));
        }
        
        StdDraw.clear();
        kdtree.draw();
        StdDraw.show(40);
    }
}