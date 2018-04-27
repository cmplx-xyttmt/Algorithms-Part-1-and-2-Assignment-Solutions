import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final LineSegment[] lineSegments;
    private final int numOfLineSegments;
    private final ArrayList<LineSegment> lsegs = new ArrayList<>();

    public BruteCollinearPoints(Point[] points){
        if (points == null) throw new IllegalArgumentException();
        Point[] copy = Arrays.copyOf(points, points.length);

        for (int i = 0; i < copy.length; i++) {
            if (copy[i] == null) throw new IllegalArgumentException();
            for (int j = i + 1; j < copy.length; j++) {
                if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException();
            }
        }

        for (int p = 0; p < copy.length; p++) {
            for (int q = p + 1; q < copy.length; q++) {
                for (int r = q + 1; r < copy.length; r++) {
                    for (int s = r + 1; s < copy.length; s++) {
                        double slope = copy[p].slopeTo(copy[q]);
                        if (slope == copy[p].slopeTo(copy[r]) && slope == copy[p].slopeTo(copy[s])){
                            Point[] linePoints = {copy[p], copy[q], copy[r], copy[s]};
                            Point[] lineEnd = lineEndPoints(linePoints);
                            lsegs.add(new LineSegment(lineEnd[0], lineEnd[1]));
                        }
                    }
                }
            }
        }

        numOfLineSegments = lsegs.size();
        lineSegments = new LineSegment[lsegs.size()];

        for (int i = 0; i < lsegs.size(); i++) {
            lineSegments[i] = lsegs.get(i);
        }
    }

    private Point[] lineEndPoints(Point[] points){
        Arrays.sort(points);
        return new Point[]{points[0], points[points.length - 1]};
    }

    public int numberOfSegments(){
        return numOfLineSegments;
    }

    public LineSegment[] segments(){
        LineSegment[] lineSegments = new LineSegment[this.lineSegments.length];
        System.arraycopy(this.lineSegments, 0, lineSegments, 0, lineSegments.length);
        return lineSegments;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
