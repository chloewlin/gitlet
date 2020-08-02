package bearmaps.utils.ps;

import java.util.List;

public class KDTree {
    private KDTreeNode root;

    public KDTree(List<Point> points) {
        this.root = new KDTreeNode(points.get(0));
        for (Point p : points) {
            KDTreeNode current = root;
            boolean isX = true;
            while (current != null) {
                if (current.equals(p)) {
                    break;
                }
                if (isX) {
                    if (p.getX() >= current.p.getX()) {
                        if (current.right == null) {
                            current.right = new KDTreeNode(p, !isX);
                            break;
                        } else {
                            current = current.right;
                        }
                    } else {
                        if (current.left == null) {
                            current.left = new KDTreeNode(p, !isX);
                            break;
                        } else {
                            current = current.left;
                        }
                    }
                } else {
                    if (p.getY() >= current.p.getY()) {
                        if (current.right == null) {
                            current.right = new KDTreeNode(p, !isX);
                            break;
                        } else {
                            current = current.right;
                        }
                    } else {
                        if (current.left == null) {
                            current.left = new KDTreeNode(p, !isX);
                            break;
                        } else {
                            current = current.left;
                        }
                    }
                }
                isX = !isX;
            }

        }
    }

    public Point nearest(double x, double y) {
        return new Point(1, 1);
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

        private void setLeft(KDTreeNode left) {
            this.left = left;
        }

        private void setRight(KDTreeNode right) {
            this.right = right;
        }
    }
}
