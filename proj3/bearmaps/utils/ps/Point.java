package bearmaps.utils.ps;

import java.util.Objects;

public class Point {

    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Returns the squared Euclidean distance between (x1, y1) and (x2, y2).
     * @param x1 The x-coordinate of the first point.
     * @param x2 The x-coordinate of the second point.
     * @param y1 The y-coordinate of the first point.
     * @param y2 The y-coordinate of the second point.
     * @return The squared Euclidean distance between (x1, y1) and (x2, y2).
     */
    private static double distance(double x1, double x2, double y1, double y2) {
        return Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2);
    }

    /**
     * Returns the haversine distance squared between two points, assuming
     * x represents the longitude and y represents the latitude.
     */
    public static double distance(Point p1, Point p2) {
        return distance(p1.getX(), p2.getX(), p1.getY(), p2.getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 &&
                Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("Point x: %.10f, y: %.10f", x, y);
    }
}
