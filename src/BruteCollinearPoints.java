import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private final Point[] collinearPoints;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        collinearPoints = new Point[points.length];
        int lastCollinearPointIdx = -1;

        for (int i = 0; i < points.length; i++) {
            Point[] checkPoints = new Point[4];
            checkPoints[0] = points[i];

            if (checkPoints[0] == null) {
                throw new IllegalArgumentException();
            }

            for (int j = 0; j < points.length; j++) {
                if (j == i) {
                    continue;
                }

                checkPoints[1] = points[j];

                if (checkPoints[1] == null) {
                    throw new IllegalArgumentException();
                }

                double slope12 = checkPoints[0].slopeTo(checkPoints[1]);

                if (slope12 == Double.NEGATIVE_INFINITY) {
                    throw new IllegalArgumentException();
                }

                for (int k = 0; k < points.length; k++) {
                    if (k == i || k == j) {
                        continue;
                    }

                    checkPoints[2] = points[k];

                    if (checkPoints[2] == null) {
                        throw new IllegalArgumentException();
                    }

                    double slope13 = checkPoints[0].slopeTo(checkPoints[2]);

                    if (slope13 == Double.NEGATIVE_INFINITY) {
                        throw new IllegalArgumentException();
                    }

                    if (slope12 != slope13) {
                        checkPoints[2] = null;
                        continue;
                    }

                    for (int m = 0; m < points.length; m++) {
                        if (m == i || m == j || m == k) {
                            continue;
                        }

                        checkPoints[3] = points[m];

                        if (checkPoints[3] == null) {
                            throw new IllegalArgumentException();
                        }

                        double slope14 = checkPoints[0].slopeTo(checkPoints[3]);

                        if (slope14 == Double.NEGATIVE_INFINITY) {
                            throw new IllegalArgumentException();
                        }

                        if (slope13 == slope14) {
                            break;
                        }

                        checkPoints[3] = null;
                    }

                    if (checkPoints[3] != null) {
                        break;
                    }
                }

                if (checkPoints[3] != null) {
                    break;
                }
            }

            if (checkPoints[3] != null) {
                Arrays.sort(checkPoints);

                boolean hasSameCollinearLine = false;
                for (int j = 0; j < collinearPoints.length; j += 2) {
                    Point cStartPoint = collinearPoints[j];
                    if (cStartPoint == null) {
                        break;
                    }
                    Point cEndPoint = collinearPoints[j + 1];
                    if (cStartPoint == checkPoints[0] && cEndPoint == checkPoints[3]) {
                        hasSameCollinearLine = true;
                        break;
                    }
                }

                if (hasSameCollinearLine) {
                    continue;
                }

                collinearPoints[++lastCollinearPointIdx] = checkPoints[0];
                collinearPoints[++lastCollinearPointIdx] = checkPoints[3];
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        int counter = 0;

        for (int i = 0; i < collinearPoints.length; i++) {
            if (i % 2 != 0 && collinearPoints[i] != null) {
                counter++;
            }
        }

        return counter;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[numberOfSegments()];

        for (int i = 0; i < numberOfSegments(); i++) {
            Point point1 = collinearPoints[i * 2];
            Point point2 = collinearPoints[(i * 2) + 1];
            segments[i] = new LineSegment(point1, point2);
        }

        return segments;
    }

    public static void main(String[] args) {
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
