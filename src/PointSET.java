import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PointSET {
    private Set<Point2D> points;

    public PointSET(){
        points = new TreeSet<>();
    }

    public boolean isEmpty(){
        return points.isEmpty();
    }

    public int size(){
        return points.size();
    }

    public void insert(Point2D p){
        points.add(p);
    }

    public boolean contains(Point2D p){
        return points.contains(p);
    }

    public void draw(){
        for (Point2D point: points){
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect){
        List<Point2D> inRect = new LinkedList<>();
        for (Point2D point: points){
            if (rect.contains(point)) inRect.add(point);
        }

        return inRect;
    }

    public Point2D nearest(Point2D p){
        Point2D nearest = null;
        double min = Double.MAX_VALUE;
        for (Point2D point: points){
            if (min > p.distanceTo(point)){
                min = p.distanceTo(point);
                nearest = point;
            }
        }

        return nearest;
    }
}
