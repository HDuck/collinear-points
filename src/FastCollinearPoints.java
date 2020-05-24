import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private static final int MIN_COLLINEAR_SEGMENTS = 4;
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

        Point[] sortedPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            sortedPoints[i] = points[i];
        }

        Point[] lineSegmentPoints = new Point[points.length * points.length];
        int lastLineSegmentPointIndex = -1;
        boolean isOneLineGrid = false;

        for (Point initPoint : points) {
            Arrays.sort(sortedPoints, initPoint.slopeOrder());

            int checkIndex = 1;
            while (checkIndex < sortedPoints.length) {
                int initCheckIndex = checkIndex;
                Point currentPoint = sortedPoints[initCheckIndex];
                double currentSlope = initPoint.slopeTo(currentPoint);
                double nextSlope;

                do {
                    checkIndex++;
                    if (checkIndex == sortedPoints.length) {
                        break;
                    }
                    Point nextPoint = sortedPoints[checkIndex];
                    nextSlope = initPoint.slopeTo(nextPoint);
                } while (nextSlope == currentSlope);

                if (checkIndex - initCheckIndex >= MIN_COLLINEAR_SEGMENTS - 1) {
                    Point[] collinearLinePoints = sliceCollinearPointsSegment(sortedPoints, initCheckIndex, checkIndex - 1);
                    Arrays.sort(collinearLinePoints);

                    isOneLineGrid = collinearLinePoints.length == sortedPoints.length;

                    Point startPoint = collinearLinePoints[0];
                    Point endPoint = collinearLinePoints[collinearLinePoints.length - 1];

                    if (!hasSameLineSegmentPoints(lineSegmentPoints, startPoint, endPoint)) {
                        lineSegmentPoints[++lastLineSegmentPointIndex] = startPoint;
                        lineSegmentPoints[++lastLineSegmentPointIndex] = endPoint;
                    }
                }
            }

            if (isOneLineGrid) {
                break;
            }
        }

        int collinearLinesCount = 0;
        for (int j = 0; j < lineSegmentPoints.length; j += 2) {
            if (lineSegmentPoints[j] == null) {
                break;
            }
            collinearLinesCount++;
        }

        lineSegments = new LineSegment[collinearLinesCount];
        for (int j = 0; j < collinearLinesCount; j++) {
            lineSegments[j] = new LineSegment(lineSegmentPoints[j * 2], lineSegmentPoints[(j * 2) + 1]);
        }
    }

    private Point[] sliceCollinearPointsSegment(Point[] collinearPoints, int startIndex, int endIndex) {
        Point[] result = new Point[endIndex - startIndex + 2];
        result[0] = collinearPoints[0];

        int index = 0;
        while (index < endIndex - startIndex + 1) {
            result[index + 1] = collinearPoints[startIndex + index];
            index++;
        }
        return result;
    }

    private boolean hasSameLineSegmentPoints(Point[] lineSegmentPoints, Point startPoint, Point endPoint) {
        boolean result = false;
        for (int i = 0; i < lineSegmentPoints.length; i += 2) {
            Point segmentStart = lineSegmentPoints[i];
            if (segmentStart == null) {
                break;
            }
            Point segmentEnd = lineSegmentPoints[i + 1];
            if (segmentStart.compareTo(startPoint) == 0 && segmentEnd.compareTo(endPoint) == 0) {
                result = true;
                break;
            }
        }
        return result;
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
