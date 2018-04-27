package collinear;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final LineSegment[] lineSegments;
    private final int numOfLineSegments;

    public FastCollinearPoints(Point[] points){
        if (points == null) throw new IllegalArgumentException();
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException();
            }
        }

        ArrayList<LineSegment> lsegs = new ArrayList<>();

        for (int p = 0; p < points.length; p++) {
            Point[] otherPoints = new Point[points.length - 1];
            int counter = 0;
            for (int q = 0; q < points.length; q++) {
                if (q != p){
                    if (points[p] == points[q]) throw new IllegalArgumentException();
                    otherPoints[counter] = points[q];
                    counter++;
                }
            }

            Arrays.sort(otherPoints, points[p].slopeOrder());
            int start = 0;
            int end = 0;

            for (int i = 1; i < otherPoints.length; i++) {
                if (otherPoints[i].slopeTo(points[p]) == otherPoints[i - 1].slopeTo(points[p])){
                    end = i;
                }
                else if (end - start + 1 >= 3){
                    Point[] linePoints = new Point[end - start + 2];
                    linePoints[0] = points[p];
                    System.arraycopy(otherPoints, start, linePoints, 1, end + 1 - start);
                    Point[] lineEndPoints = lineEndPoints(linePoints);
                    LineSegment line = new LineSegment(lineEndPoints[0], lineEndPoints[1]);
                    if (!linSearchForLineSeg(lsegs, line))lsegs.add(line);
                    start = i;
                    end = i;
                }
                else {
                    start = i;
                    end = i;
                }
            }
            if (end - start + 1 >= 3){
                Point[] linePoints = new Point[end - start + 2];
                linePoints[0] = points[p];
                System.arraycopy(otherPoints, start, linePoints, 1, end + 1 - start);
                Point[] lineEndPoints = lineEndPoints(linePoints);
                LineSegment line = new LineSegment(lineEndPoints[0], lineEndPoints[1]);
                if (!linSearchForLineSeg(lsegs, line))lsegs.add(line);
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

    private boolean linSearchForLineSeg(ArrayList<LineSegment> lineSegments, LineSegment key){
        for (LineSegment lineSegment : lineSegments) {
            if (key.toString().equals(lineSegment.toString())) return true;
        }
        return false;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
