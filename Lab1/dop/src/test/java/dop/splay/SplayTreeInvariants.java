package dop.splay;

import java.util.IdentityHashMap;
import java.util.List;

public final class SplayTreeInvariants {

  public static void assertAll(SplayTree t) {
    SplayTree.Node root = t.root();

    if (root != null && root.parent != null) {
      throw new AssertionError("Invariant failed: root.parent must be null");
    }

    IdentityHashMap<SplayTree.Node, Boolean> seen = new IdentityHashMap<>();
    int counted = dfs(root, null, seen);

    if (counted != t.size()) {
      throw new AssertionError("Invariant failed: size mismatch. counted=" + counted + " but size=" + t.size());
    }

    List<Integer> inorder = t.inOrder();
    for (int i = 1; i < inorder.size(); i++) {
      if (inorder.get(i - 1) >= inorder.get(i)) {
        throw new AssertionError("Invariant failed: inorder not strictly increasing: " + inorder);
      }
    }
  }

  private static int dfs(SplayTree.Node n, SplayTree.Node expectedParent,
                         IdentityHashMap<SplayTree.Node, Boolean> seen) {
    if (n == null) return 0;

    if (seen.put(n, Boolean.TRUE) != null) {
      throw new AssertionError("Invariant failed: cycle detected at key=" + n.key);
    }

    if (n.parent != expectedParent) {
      throw new AssertionError("Invariant failed: bad parent pointer at key=" + n.key);
    }

    if (n.left != null && n.left.key >= n.key) {
      throw new AssertionError("Invariant failed: left.key >= node.key at key=" + n.key);
    }
    if (n.right != null && n.right.key <= n.key) {
      throw new AssertionError("Invariant failed: right.key <= node.key at key=" + n.key);
    }

    int left = dfs(n.left, n, seen);
    int right = dfs(n.right, n, seen);
    return 1 + left + right;
  }

  private SplayTreeInvariants() {}
}