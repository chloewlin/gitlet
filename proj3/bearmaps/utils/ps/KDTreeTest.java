package bearmaps.utils.ps;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class KDTreeTest {

    @Test
    public void KDTreeTest1() {
        Random r = new Random(1);
        ArrayList<Point> points = new ArrayList<Point>();
        for (int i = 0; i < 1000; i++) {
            points.add(new Point(r.nextDouble(), r.nextDouble()));
        }
        NaivePointSet naivePointSet = new NaivePointSet(points);
        KDTree tree = new KDTree(points);

        for (int i = 0; i < 1000; i++) {
            Point randPoint = new Point(r.nextDouble(), r.nextDouble());
            Point nearestNaivePoint = naivePointSet.nearest(randPoint.getX(), randPoint.getY());
            Point nearestKdTreePoint = tree.nearest(randPoint.getX(), randPoint.getY());
            Assert.assertEquals(nearestNaivePoint, nearestKdTreePoint);
        }
    }
}