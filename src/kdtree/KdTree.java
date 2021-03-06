package kdtree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KdTree {

    private static final RectHV BORAD_RECT = new RectHV(0, 0, 1, 1);

    private TreeNode tree;
    private int treeSize;

    public KdTree() {
        tree = null;
        treeSize = 0;
    }

    public boolean isEmpty() {
        return treeSize == 0;
    }

    public int size() {
        return treeSize;
    }

    public void insert(Point2D p) {
        tree = insertHelper(tree, p, true);
    }

    private TreeNode insertHelper(TreeNode node, Point2D p, boolean isVertical) {
        if (node == null) {
            ++treeSize;
            return new TreeNode(p.x(), p.y(), null, null, isVertical);
        } else {
            if (node.x == p.x() && node.y == p.y()) {
                return node;
            } else if ((node.isVertical && p.x() <= node.x) || (!node.isVertical && p.y() <= node.y)) {
                node.left = insertHelper(node.left, p, !node.isVertical);
            } else { // if ((node.isVertical && p.x() > node.x) || (!node.isVertical && p.y() > node.y)) {
                node.right = insertHelper(node.right, p, !node.isVertical);
            }
            return node;
        }
    }

    public boolean contains(Point2D p) {
        TreeNode node = tree;
        while (node != null) {
            if (node.x == p.x() && node.y == p.y()) {
                return true;
            } else if ((node.isVertical && p.x() <= node.x) || (!node.isVertical && p.y() <= node.y)) {
                node = node.left;
            } else { //if ((node.isVertical && p.x() > node.x) || (!node.isVertical && p.y() > node.y)) {
                node = node.right;
            }
        }
        return false;
    }

    public void draw() {
        StdDraw.setScale(0, 1);
        draw(tree, BORAD_RECT);
    }

    private RectHV getLeftChildRect(RectHV nodeRect, TreeNode node) {
        if (node.isVertical) {
            return new RectHV(nodeRect.xmin(), nodeRect.ymin(), node.x, nodeRect.ymax());
        } else {
            return new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), node.y);
        }
    }

    private RectHV getRightChildRect(RectHV nodeRect, TreeNode node) {
        if (node.isVertical) {
            return new RectHV(node.x, nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax());
        } else {
            return new RectHV(nodeRect.xmin(), node.y, nodeRect.xmax(), nodeRect.ymax());
        }
    }
    private void draw(TreeNode node, RectHV rect) {
        if (node != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.02);
            new Point2D(node.x, node.y).draw();
            StdDraw.setPenRadius();

            if (node.isVertical) {
                StdDraw.setPenColor(StdDraw.RED);
                new Point2D(node.x, rect.ymin()).drawTo(new Point2D(node.x, rect.ymax()));
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                new Point2D(rect.xmin(), node.y).drawTo(new Point2D(rect.xmax(), node.y));
            }
            draw(node.left, getLeftChildRect(rect, node));
            draw(node.right, getRightChildRect(rect, node));
        }
    }

    private void rangeHelper(TreeNode node, RectHV nodeRect, RectHV rect, List<Point2D> pointInRect) {
        if (node != null) {
            if (rect.intersects(nodeRect)) {
                Point2D p = new Point2D(node.x, node.y);
                if (rect.contains(p)) {
                    pointInRect.add(p);
                }
                rangeHelper(node.left, getLeftChildRect(nodeRect, node), rect, pointInRect);
                rangeHelper(node.right, getRightChildRect(nodeRect, node), rect, pointInRect);
            }
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        final List<Point2D> pointInRect = new ArrayList<Point2D>();

        rangeHelper(tree, BORAD_RECT, rect, pointInRect);

        return new Iterable<Point2D>() {
            @Override
            public Iterator<Point2D> iterator() {
                return pointInRect.iterator();
            }
        };
    }

    private Point2D nearestHelper(TreeNode node, RectHV rect, double x, double y, Point2D nearestPointCandidate) {

        Point2D nearestPoint = nearestPointCandidate;

        if (node != null) {
            RectHV leftRect = null;
            RectHV rightRect = null;
            Point2D queryPoint = new Point2D(x, y);

            if (nearestPoint == null || queryPoint.distanceSquaredTo(nearestPoint) > rect.distanceSquaredTo(queryPoint)) {

                Point2D nodePoint = new Point2D(node.x, node.y);

                if (nearestPoint == null) {
                    nearestPoint = nodePoint;
                } else {
                    if (queryPoint.distanceSquaredTo(nearestPoint) > queryPoint.distanceSquaredTo(nodePoint)) {
                        nearestPoint = nodePoint;
                    }
                }

                if (node.isVertical) {
                    leftRect = new RectHV(rect.xmin(), rect.ymin(), node.x, rect.ymax());
                    rightRect = new RectHV(node.x, rect.ymin(), rect.xmax(), rect.ymax());
                    if (x <= node.x) {
                        nearestPoint = nearestHelper(node.left, leftRect, x, y, nearestPoint);
                        nearestPoint = nearestHelper(node.right, rightRect, x, y, nearestPoint);
                    } else {
                        nearestPoint = nearestHelper(node.right, rightRect, x, y, nearestPoint);
                        nearestPoint = nearestHelper(node.left, leftRect, x, y, nearestPoint);
                    }
                } else {
                    leftRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.y);
                    rightRect = new RectHV(rect.xmin(), node.y, rect.xmax(), rect.ymax());
                    if (y <= node.y) {
                        nearestPoint = nearestHelper(node.left, leftRect, x, y, nearestPoint);
                        nearestPoint = nearestHelper(node.right, rightRect, x, y, nearestPoint);
                    } else {
                        nearestPoint = nearestHelper(node.right, rightRect, x, y, nearestPoint);
                        nearestPoint = nearestHelper(node.left, leftRect, x, y, nearestPoint);
                    }
                }
            }
        }
        return nearestPoint;
    }

    public Point2D nearest(Point2D p) {
        return nearestHelper(tree, BORAD_RECT, p.x(), p.y(), null);
    }
}

class TreeNode {
    double x;
    double y;
    boolean isVertical;

    TreeNode left;
    TreeNode right;

    TreeNode(double x, double y, TreeNode left, TreeNode right, boolean isVertical) {
        this.x = x;
        this.y = y;
        this.left = left;
        this.right = right;
        this.isVertical = isVertical;
    }
}