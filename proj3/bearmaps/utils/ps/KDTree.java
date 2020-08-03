package bearmaps.utils.ps;

import java.util.*;

public class KDTree {
    private KDTreeNode root;

    public KDTree(List<Point> points) {
        this.root = new KDTreeNode(points.get(0));
        for (Point p : points) {
            KDTreeNode current = root;
            boolean isX = true;
//            System.out.println("Trying to insert: " + p);
            while (current != null) {
                if (current.p.equals(p)) {
//                    System.out.println("duplicate found");
                    break;
                }
//                System.out.println("current at: " + current.p);
                if (isX) {
                    if (p.getX() >= current.p.getX()) {
                        if (current.right == null) {
//                            System.out.println("inserting right: " + p);
                            current.right = new KDTreeNode(p, !isX);
                            break;
                        } else {
//                            System.out.println("going right to: " + current.right.p);
                            current = current.right;
                        }
                    } else {
                        if (current.left == null) {
//                            System.out.println("inserting left: " + p);
                            current.left = new KDTreeNode(p, !isX);
                            break;
                        } else {
//                            System.out.println("going left to: " + current.left.p);
                            current = current.left;
                        }
                    }
                } else {
                    if (p.getY() >= current.p.getY()) {
                        if (current.right == null) {
//                            System.out.println("inserting right: " + p);
                            current.right = new KDTreeNode(p, !isX);
                            break;
                        } else {
//                            System.out.println("going right to: " + current.right.p);
                            current = current.right;
                        }
                    } else {
                        if (current.left == null) {
//                            System.out.println("inserting left: " + p);
                            current.left = new KDTreeNode(p, !isX);
                            break;
                        } else {
//                            System.out.println("going left to: " + current.left.p);
                            current = current.left;
                        }
                    }
                }
                isX = !isX;
            }
        }
    }
//    @Override
//    public String toString() {
//        Queue<KDTreeNode> queue = new LinkedList<KDTreeNode>();
//        queue.offer(root);
//        String output = "";
//        while (!queue.isEmpty()) {
//            KDTreeNode curr = queue.poll();
//            output += curr.p + " ";
//            if (curr.left != null) {
//                queue.offer(curr.left);
//            }
//            if (curr.right != null) {
//                queue.offer(curr.right);
//            }
//        }
//    }
    private Point findNearestPoint(KDTreeNode curr, Point target, double currBestDist) {
        if (curr == null) {
            return null;
        }
//        System.out.println("curr point: " + curr.p);
//        System.out.println("right child: " + curr.right.p);
//        System.out.println("left child: " + curr.left.p);
        if (curr.left == null && curr.right == null) {
            return curr.p;
        }
        boolean traverseRight = (curr.isX && target.getX() >= curr.p.getX())
                || (!curr.isX && target.getY() >= curr.p.getY());
//        System.out.println("traverseRight: " + traverseRight);
        double currDist = Point.distance(curr.p, target);
        currBestDist = Math.min(currDist, currBestDist);
        Point nearestGoodPoint = findNearestPoint(traverseRight ? curr.right : curr.left, target, currBestDist);
//        System.out.println("nearest gud point: " + nearestGoodPoint);
        double nearestGoodPointDist;
        if (nearestGoodPoint == null) {
            nearestGoodPoint = curr.p;
            nearestGoodPointDist = Point.distance(curr.p, target);
        } else {
            nearestGoodPointDist = Point.distance(nearestGoodPoint, target);

            if (nearestGoodPointDist > Point.distance(curr.p, target)) {
                nearestGoodPoint = curr.p;
                nearestGoodPointDist = Point.distance(curr.p, target);
            }
        }

        double splitAxisDistToTarget = curr.splitAxisDistToPoint(target);
//        splitAxisDistToTarget = Math.floor(splitAxisDistToTarget * 100) / 100;
//        nearestGoodPointDist = Math.floor(nearestGoodPointDist * 100) / 100;
//        System.out.println(curr.p + " splitAxisDist: " + splitAxisDistToTarget);
//        System.out.println(curr.p + " nearestPointDist: " + nearestGoodPointDist);

        boolean traverseBad = (splitAxisDistToTarget < nearestGoodPointDist)
                && (splitAxisDistToTarget < currBestDist);
        //Double.compare(splitAxisDistToTarget, nearestGoodPointDist) < 0;
//        if (traverseBad) {
//            System.out.println("traverseBad: " + (traverseRight ? curr.left.p : curr.right.p));
//        }
        Point nearestBadPoint = traverseBad ? findNearestPoint(traverseRight ? curr.left :
                curr.right, target, currBestDist) : null;

        if (nearestBadPoint == null) {
            return nearestGoodPoint;
        }
        double nearestDistBadSidePoint = Point.distance(nearestBadPoint, target);

        return nearestDistBadSidePoint < nearestGoodPointDist ? nearestBadPoint : nearestGoodPoint;
    }
    public Point nearest(double x, double y) {
        Point target = new Point(x, y);
        return findNearestPoint(root, target, Double.MAX_VALUE);
    }
    private class KDTreeNode {
        private Point p;
        private KDTreeNode left = null;
        private KDTreeNode right = null;
        private boolean isX;

        private KDTreeNode(Point p) {
            this(p, true);
        }
        private KDTreeNode(Point p, boolean isX) {
            this.p = p;
            this.isX = isX;
        }
        private double splitAxisDistToPoint(Point target) {
            if (isX) {
                Point bestCasePoint = new Point(p.getX(), target.getY());
                return Point.distance(bestCasePoint, target);
            }
            Point bestCasePoint = new Point(target.getX(), p.getY());
            return Point.distance(bestCasePoint, target);
        }

        private void setLeft(KDTreeNode left) {
            this.left = left;
        }

        private void setRight(KDTreeNode right) {
            this.right = right;
        }
    }

    public static void main(String[] args) {
//        Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
//        Point p2 = new Point(3.3, 4.4);
//        Point p3 = new Point(-2.9, 4.2);
//
//        KDTree nn = new KDTree(List.of(p1, p2, p3));
//        Point ret = nn.nearest(3.0, 4.0); // returns p2
//        System.out.println(ret.getX()); // evaluates to 3.3
//        System.out.println(ret.getY()); // evaluates to 4.4

//        Point A = new Point(0, 2);
//        Point B = new Point(4, 2);
//        Point C = new Point(-3, 1);
//        KDTree tree = new KDTree(List.of(A, B, C));
//        Point nearest = tree.nearest(1, 1); // return A
//        System.out.println(nearest.getX()); // 0
//        System.out.println(nearest.getY()); // 2

//        Point d = new Point(5, 6);
//        Point a = new Point(1, 5);
//        Point c = new Point(2, 2);
//        Point b = new Point(4, 9);
//        Point f = new Point(7, 3);
//        Point g = new Point(9, 1);
//        Point e = new Point(8, 7);
//        Point h = new Point(6, 2);
//        KDTree tree2 = new KDTree(List.of(d, a, f, c, b, g, e, h));
//        Point nearest2 = tree2.nearest(3, 6);
//        System.out.println(nearest2.getX());
//        System.out.println(nearest2.getY());

//        Point d = new Point(5, 2);
//        Point a = new Point(0, 0);
//        Point c = new Point(3, -3);
//        KDTree tree3 = new KDTree(List.of(d, a, c));
//        Point nearest3 = tree3.nearest(4, 2);
//        System.out.println(nearest3.getX());
//        System.out.println(nearest3.getY());
    }

}
