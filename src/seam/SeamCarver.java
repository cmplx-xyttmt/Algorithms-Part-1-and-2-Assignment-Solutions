package seam;

import edu.princeton.cs.algs4.Picture;

import java.awt.*;
import java.io.File;
import java.util.Arrays;

public class SeamCarver {
    private Picture picture;

    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.picture = picture;
    }

    public Picture picture() {
        return picture;
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public double energy(int x, int y) {
        if (x < 0 || x >= picture.width() || y < 0 || y >= picture.height()) throw new IllegalArgumentException();
        if (x == 0 || x == picture.width() - 1 || y == 0 || y == picture.height() - 1) return 1000;
        return Math.sqrt(xGradientSquare(x, y) + yGradientSquare(x, y));
    }

    public int[] findHorizontalSeam() {
        double[][] weights = new double[picture.height()][picture.width()];
        int[][] to = new int[picture.height()][picture.width()];
        int minCol = 0;
        double minEnergy = Double.MAX_VALUE;
        for (int j = width() - 1; j >= 0; j--) {
            for (int i = 0; i < height(); i++) {
                if (j == width() - 1) weights[i][j] = 1000;
                else {
                    double min = weights[i][j + 1];
                    to[i][j] = i;
                    if (i > 0 && weights[i - 1][j + 1] < min){
                        min = weights[i - 1][j + 1];
                        to[i][j] = i - 1;
                    }
                    if (i < weights.length - 1 && weights[i +  1][j + 1] < min){
                        min = weights[i + 1][j + 1];
                        to[i][j] = i + 1;
                    }
                    weights[i][j] = energy(j, i) + min;
                }
                if (j == 0 && weights[i][j] < minEnergy) {
                    minEnergy = weights[i][j];
                    minCol = i;
                }
            }
        }

        System.out.println(minEnergy);
        int[] ans = new int[width()];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = minCol;
            minCol = to[minCol][i];
        }

        return ans;
    }

    public int[] findVerticalSeam() {
        double[][] weights = new double[picture.height()][picture.width()];
        int[][] to = new int[picture.height()][picture.width()];
        int minCol = 0;
        double minEnergy = Double.MAX_VALUE;
        for (int i = weights.length - 1; i >= 0; i--) {
            for (int j = weights[i].length - 1; j >= 0; j--) {
                if (i == weights.length - 1) weights[i][j] = 1000;
                else {
                    double min = weights[i + 1][j];
                    to[i][j] = j;
                    if (j > 0 && weights[i + 1][j - 1] < min){
                        min = weights[i + 1][j - 1];
                        to[i][j] = j - 1;
                    }
                    if (j < width() - 1 && weights[i + 1][j + 1] < min){
                        min = weights[i + 1][j + 1];
                        to[i][j] = j + 1;
                    }
                    weights[i][j] = energy(j, i) + min;
                }
                if (i == 0 && weights[i][j] < minEnergy) {
                    minEnergy = weights[i][j];
                    minCol = j;
                }
            }
        }

        System.out.println(minEnergy);
        int[] ans = new int[height()];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = minCol;
            minCol = to[i][minCol];
        }

        return ans;

    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != width()) throw new IllegalArgumentException();
        if (seam[0] < 0 || seam[0] >= height()) throw new IllegalArgumentException();
        for (int i = 1; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= height()){
                System.out.println(Arrays.toString(seam) + " " + seam[i]);
                throw new IllegalArgumentException();
            }
            if (Math.abs(seam[i] - seam[i - 1]) > 1) {
                System.out.println(Arrays.toString(seam) + " " + seam[i]);
                throw new IllegalArgumentException();
            }
        }

        Picture changed = new Picture(width(), height() - 1);
        for (int i = 0; i < seam.length; i++) {
            int y = seam[i];
            for (int j = 0; j < y; j++) changed.set(i, j, picture.get(i, j));
            for (int j = y; j < height(); j++) {
                if (j + 1 < picture.height()) changed.set(i, j, picture.get(i, j + 1));
            }
        }

        picture = new Picture(changed);
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != height()) throw new IllegalArgumentException();
        if (seam[0] < 0 || seam[0] >= width()) throw new IllegalArgumentException();
        for (int i = 1; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= width()) throw new IllegalArgumentException();
            if (Math.abs(seam[i] - seam[i - 1]) > 1) throw new IllegalArgumentException();
        }

        Picture changed = new Picture(width() - 1, height());
        for (int i = 0; i < seam.length; i++) {
            int x = seam[i];
            for (int j = 0; j < x; j++) changed.set(j, i, picture.get(j, i));
            for (int j = x; j < width(); j++) {
                if (j + 1 < picture.width()) changed.set(j, i, picture.get(j + 1, i));
            }
        }

        picture = new Picture(changed);
    }

    private double xGradientSquare(int x, int y) {
        Color color1 = picture.get(x - 1, y);
        Color color2 = picture.get(x + 1, y);
        return Math.pow(color1.getRed() - color2.getRed(), 2) + Math.pow(color1.getGreen() - color2.getGreen(), 2) +
                Math.pow(color1.getBlue() - color2.getBlue(), 2);
    }

    private double yGradientSquare(int x, int y) {
        Color color1 = picture.get(x, y - 1);
        Color color2 = picture.get(x, y + 1);
        return Math.pow(color1.getRed() - color2.getRed(), 2) + Math.pow(color1.getGreen() - color2.getGreen(), 2) +
                Math.pow(color1.getBlue() - color2.getBlue(), 2);
    }

    public static void main(String[] args) {
        SeamCarver carver = new SeamCarver(new Picture(new File("seam/diagonals.png")));
        for (int i = 0; i < carver.height(); i++) {
            for (int j = 0; j < carver.width(); j++) {
                System.out.print(carver.energy(j, i) + " ");
            }
            System.out.println();
        }
        System.out.println(Arrays.toString(carver.findHorizontalSeam()));
        System.out.println(Arrays.toString(carver.findVerticalSeam()));
    }
}
