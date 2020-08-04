package bearmaps.utils.ps;

import java.util.List;

public class NaivePointSet implements PointSet {
    List<Point> points;
    public NaivePointSet(List<Point> points) {
        this.points = points;
    }

    @Override
    public Point nearest(double x, double y) {
        double nearestDist = Double.MAX_VALUE;
        Point nearestPoint = null;
        Point target = new Point(x, y);
        for (Point p : points) {
            double distBetween = Point.distance(p, target);
            if (distBetween < nearestDist) {
                nearestDist = distBetween;
                nearestPoint = p;
            }
        }
        return nearestPoint;
    }

    public static void main(String[] args) {
        Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3));
        Point ret = nn.nearest(3.0, 4.0); // returns p2
        System.out.println(ret.getX()); // evaluates to 3.3
        System.out.println(ret.getY()); // evaluates to 4.4
    }
}
