import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private final LineSegment[] lineSegments;
    private final static int MIN_COLLINEAR_SEGMENTS = 4;

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

        Point[] lineSegmentPoints = new Point[points.length * points.length / MIN_COLLINEAR_SEGMENTS];
        int lastLineSegmentPointIndex = -1;

        for (Point initPoint : points) {
            Point[] collinearPoints = new Point[points.length];
            int lastCollinearPointIndex = -1;
            collinearPoints[++lastCollinearPointIndex] = initPoint;

            Arrays.sort(sortedPoints, initPoint.slopeOrder());
            for (int i = 1; i < sortedPoints.length; i++) {
                if (i + 1 == sortedPoints.length) {
                    if (lastCollinearPointIndex >= MIN_COLLINEAR_SEGMENTS - 1) {
                        int collinearPointsCount = lastCollinearPointIndex + 1;
                        Point[] clonedCollinearPoints = new Point[collinearPointsCount];
                        for (int j = 0; j < collinearPointsCount; j++) {
                            clonedCollinearPoints[j] = collinearPoints[j];
                        }

                        Arrays.sort(clonedCollinearPoints);
                        Point firstCollinearPoint = clonedCollinearPoints[0];
                        Point lastCollinearPoint = clonedCollinearPoints[lastCollinearPointIndex];

                        boolean hasSameLineSegment = false;
                        for (int j = 0; j < lineSegmentPoints.length; j += 2) {
                            Point collinearLineStart = lineSegmentPoints[j];
                            Point collinearLineEnd = lineSegmentPoints[j + 1];

                            if (collinearLineStart == null || collinearLineEnd == null) {
                                break;
                            }
                            if (collinearLineStart.compareTo(firstCollinearPoint) == 0 && collinearLineEnd.compareTo(lastCollinearPoint) == 0) {
                                hasSameLineSegment = true;
                                break;
                            }
                        }

                        if (!hasSameLineSegment) {
                            lineSegmentPoints[++lastLineSegmentPointIndex] = firstCollinearPoint;
                            lineSegmentPoints[++lastLineSegmentPointIndex] = lastCollinearPoint;
                        }
                    }
                    break;
                }

                Point point1 = sortedPoints[i];
                Point point2 = sortedPoints[i + 1];

                double slope1 = initPoint.slopeTo(point1);
                double slope2 = initPoint.slopeTo(point2);

                if (slope1 != slope2) {
                    if (lastCollinearPointIndex >= MIN_COLLINEAR_SEGMENTS - 1) {
                        int collinearPointsCount = lastCollinearPointIndex + 1;
                        Point[] clonedCollinearPoints = new Point[collinearPointsCount];
                        for (int j = 0; j < collinearPointsCount; j++) {
                            clonedCollinearPoints[j] = collinearPoints[j];
                        }

                        Arrays.sort(clonedCollinearPoints);
                        Point firstCollinearPoint = clonedCollinearPoints[0];
                        Point lastCollinearPoint = clonedCollinearPoints[lastCollinearPointIndex];

                        boolean hasSameLineSegment = false;
                        for (int j = 0; j < lineSegmentPoints.length; j += 2) {
                            Point collinearLineStart = lineSegmentPoints[j];
                            Point collinearLineEnd = lineSegmentPoints[j + 1];

                            if (collinearLineStart == null || collinearLineEnd == null) {
                                break;
                            }
                            if (collinearLineStart.compareTo(firstCollinearPoint) == 0 && collinearLineEnd.compareTo(lastCollinearPoint) == 0) {
                                hasSameLineSegment = true;
                                break;
                            }
                        }

                        if (!hasSameLineSegment) {
                            lineSegmentPoints[++lastLineSegmentPointIndex] = firstCollinearPoint;
                            lineSegmentPoints[++lastLineSegmentPointIndex] = lastCollinearPoint;
                        }
                    }

                    lastCollinearPointIndex = 0;
                    continue;
                }

                if (lastCollinearPointIndex == 0) {
                    collinearPoints[++lastCollinearPointIndex] = point1;
                }

                collinearPoints[++lastCollinearPointIndex] = point2;
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
