package dop.splay;

public final class SplayTreeMetrics {

    public static Snapshot snapshot(SplayTree tree) {
        SplayTree.Node root = tree.root();
        int size = tree.size();
        int height = height(root);
        int leftHeight = root == null ? 0 : height(root.left);
        int rightHeight = root == null ? 0 : height(root.right);
        int rootBalance = leftHeight - rightHeight;
        int internalPathLength = sumDepths(root, 0);
        double avgDepth = size == 0 ? 0.0 : (double) internalPathLength / size;

        return new Snapshot(size, height, leftHeight, rightHeight, rootBalance, avgDepth);
    }

    private static int height(SplayTree.Node node) {
        if (node == null) {
            return 0;
        }
        int left = height(node.left);
        int right = height(node.right);
        return 1 + Math.max(left, right);
    }

    private static int sumDepths(SplayTree.Node node, int depth) {
        if (node == null) {
            return 0;
        }
        return depth
            + sumDepths(node.left, depth + 1)
            + sumDepths(node.right, depth + 1);
    }

    public static final class Snapshot {
        public final int size;
        public final int height;
        public final int leftHeight;
        public final int rightHeight;
        public final int rootBalance;
        public final double avgDepth;

        Snapshot(int size, int height, int leftHeight, int rightHeight,
                 int rootBalance, double avgDepth) {
            this.size = size;
            this.height = height;
            this.leftHeight = leftHeight;
            this.rightHeight = rightHeight;
            this.rootBalance = rootBalance;
            this.avgDepth = avgDepth;
        }

        public String toArtifactText() {
            return String.format(
                "size=%d%nheight=%d%nleftHeight=%d%nrightHeight=%d%nrootBalance=%d%navgDepth=%.3f",
                size, height, leftHeight, rightHeight, rootBalance, avgDepth
            );
        }
    }

    private SplayTreeMetrics() {}
}