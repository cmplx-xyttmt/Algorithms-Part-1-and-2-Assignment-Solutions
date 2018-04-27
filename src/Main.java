import edu.princeton.cs.algs4.*;

/**
 * Main file for SedgewickAlgorithms
 */
public class Main {

    public static void main(String[] args) {

//        int n = 50;
//        double[] a = new double[n];
//        for (int i = 0; i < n; i++)
//            a[i] = StdRandom.uniform();
//
//
//        Arrays.sort(a);
//        for (int i = 0; i < n; i++) {
//            double x = 1.0*i/n;
//            double y = a[i]/2.0;
//            double rw = 0.5/n;
//            double rh = a[i]/2.0;
//            StdDraw.filledRectangle(x, y, rw, rh);
//        }

        double x0 = 0.5, x1 = 0.1, x2 = 0.9, y0 = 0.9, y1 = 0.1, y2 = 0.1;
        StdDraw.line(x0, y0, x1, y1);
        StdDraw.line(x0, y0, x2, y2);
        StdDraw.line(x1, y1, x2, y2);

        double l1 = lineLength(x0, y0, x1, y1);
        double l2 = lineLength(x0, y0, x2, y2);
        double l3 = lineLength(x1, y1, x2, y2);

        for (int i = 1; i < 10*l1; i++) {
            StdDraw.line(x0 + 0.1/l1, y0, x1, y1);
        }
    }

    static double lineLength(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
