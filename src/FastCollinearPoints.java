import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private final LineSegment[] lineSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        Point[] initialCheckedPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            Point currentPoint = points[i];
            if (currentPoint == null) {
                throw new IllegalArgumentException();
            }
            for (Point checkedPoint : initialCheckedPoints) {
                if (checkedPoint == null) {
                    continue;
                }
                if (checkedPoint.slopeTo(currentPoint) == Double.NEGATIVE_INFINITY) {
                    throw new IllegalArgumentException();
                }
            }
            initialCheckedPoints[i] = currentPoint;
        }

        Point[] collinearPoints = new Point[points.length];
        int lastCollinearPointIdx = -1;

        Point[] pointsCopy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            pointsCopy[i] = points[i];
        }

        for (int i = 0; i < pointsCopy.length; i++) {
            Point initPoint = pointsCopy[i];
            Point[] checkPoints = new Point[points.length];
            int lastCheckPointIdx = -1;
            checkPoints[++lastCheckPointIdx] = initPoint;
            Arrays.sort(points, initPoint.slopeOrder());

            for (int j = 1; j < points.length; j++) {
                if (j + 1 == points.length) {
                    break;
                }

                Point point1 = points[j];
                Point point2 = points[j + 1];
                if (initPoint.slopeTo(point1) != initPoint.slopeTo(point2)) {
                    continue;
                }

                if (lastCheckPointIdx == 0) {
                    checkPoints[++lastCheckPointIdx] = point1;
                }
                checkPoints[++lastCheckPointIdx] = point2;
            }

            if (lastCheckPointIdx >= 3) {
                Point[] sortedCheckPoints = new Point[lastCheckPointIdx + 1];

                for (int j = 0; j < checkPoints.length; j++) {
                    if (checkPoints[j] == null) {
                        break;
                    }
                    sortedCheckPoints[j] = checkPoints[j];
                }

                Arrays.sort(sortedCheckPoints);

                boolean hasSameCollinearLine = false;
                for (int j = 0; j < collinearPoints.length; j += 2) {
                    Point cStartPoint = collinearPoints[j];
                    if (cStartPoint == null) {
                        break;
                    }
                    Point cEndPoint = collinearPoints[j + 1];
                    if (cStartPoint == sortedCheckPoints[0] && cEndPoint == sortedCheckPoints[lastCheckPointIdx]) {
                        hasSameCollinearLine = true;
                        break;
                    }
                }

                if (hasSameCollinearLine) {
                    continue;
                }

                collinearPoints[++lastCollinearPointIdx] = sortedCheckPoints[0];
                collinearPoints[++lastCollinearPointIdx] = sortedCheckPoints[lastCheckPointIdx];
            }
        }

        int lineSegmentsCount = 0;
        for (int i = 0; i < collinearPoints.length; i++) {
            if (i % 2 != 0 && collinearPoints[i] != null) {
                lineSegmentsCount++;
            }
        }

        lineSegments = new LineSegment[lineSegmentsCount];
        for (int i = 0; i < numberOfSegments(); i++) {
            Point point1 = collinearPoints[i * 2];
            Point point2 = collinearPoints[(i * 2) + 1];
            lineSegments[i] = new LineSegment(point1, point2);
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] copy = new LineSegment[numberOfSegments()];
        for (int i = 0; i < numberOfSegments(); i++) {
            copy[i] = lineSegments[i];
        }
        return copy;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
