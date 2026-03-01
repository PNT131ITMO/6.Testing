package dop.splay;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SplayTree {

    public static final class Node {
        public final int key;
        public Node left;
        public Node right;
        public Node parent;

        Node(int key) {
            this.key = key;
        }
    }

    Node root;
    private int size;

    public int size() {
        return size;
    }

    public Node root() {
        return root;
    }

    public boolean insert(int key) {
        if (root == null) {
            root = new Node(key);
            size = 1;
            return true;
        }

        Node cur = root;
        Node parent = null;

        while (cur != null) {
            parent = cur;
            if (key < cur.key) cur = cur.left;
            else if (key > cur.key) cur = cur.right;
            else {
                splay(parent);
                return false;
            }
        }

        Node n = new Node(key);
        n.parent = parent;
        if (key < parent.key) parent.left = n;
        else parent.right = n;

        size++;
        splay(n);
        return true;
    }

    public boolean contains(int key) {
        Node cur = root;
        Node last = null;

        while (cur != null) {
            last = cur;
            if (key < cur.key) cur = cur.left;
            else if (key > cur.key) cur = cur.right;
            else {
                splay(cur);
                return true;
            }
        }

        if (last != null) splay(last);
        return false;
    }

    public boolean delete(int key) {
        if (root == null) return false;

        boolean found = contains(key);
        if (!found) return false;

        Node toDelete = root;
        Node left = toDelete.left;
        Node right = toDelete.right;

        if (left != null) left.parent = null;
        if (right != null) right.parent = null;

        toDelete.left = null;
        toDelete.right = null;
        toDelete.parent = null;

        if (left == null) {
            root = right;
        } else {
            root = left;
            Node max = left;
            while (max.right != null) max = max.right;
            splay(max);
            root.right = right;
            if (right != null) right.parent = root;
        }
        
        size--;
        return true;
    }

    public void rotateLeft(int key) {
        Node x = findNodeNoSplay(key);
        if (x == null) throw new IllegalArgumentException("rotateLeft: key not found!");
        if (x.right == null) throw new IllegalStateException("rotateLeft: no right child!");
        rotateLeft(x);
    }

    public void rotateRight(int key) {
        Node x = findNodeNoSplay(key);
        if (x == null) throw new IllegalArgumentException("rotateRight: key not found: " + key);
        if (x.left == null) throw new IllegalStateException("rotateRight: no left child for key: " + key);
        rotateRight(x);
    }

    public boolean canRotateLeft(int key) {
        Node x = findNodeNoSplay(key);
        return x != null && x.right != null;
    }

    public boolean canRotateRight(int key) {
        Node x = findNodeNoSplay(key);
        return x != null && x.left != null;
    }

    public List<Integer> inOrder() {
        List<Integer> out = new ArrayList<>();
        inOrderDfs(root, out);
        return out;
    }

    private static void inOrderDfs(Node n, List<Integer> out) {
        if (n == null) return;
        inOrderDfs(n.left, out);
        out.add(n.key);
        inOrderDfs(n.right, out);
    }

    private Node findNodeNoSplay(int key) {
        Node cur = root;
        while (cur != null) {
            if (key < cur.key) cur = cur.left;
            else if (key > cur.key) cur = cur.right;
            else return cur;
        }
        return null;
    }

    private void splay(Node x) {
        Objects.requireNonNull(x, "splay node must not be null");

        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;

            if (g == null) {
                if (x == p.left) rotateRight(p);
                else rotateLeft(p);
            } else if (x == p.left && p == g.left) {
                rotateRight(g);
                rotateRight(p);
            } else if (x == p.right && p == g.right) {
                rotateLeft(g);
                rotateLeft(p);
            } else if (x == p.right && p == g.left) {
                rotateLeft(p);
                rotateRight(g);
            } else {
                rotateRight(p);
                rotateLeft(g);
            }
        }
        root = x;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y == null) return;

        x.right = y.left;
        if (y.left != null) y.left.parent = x;

        y.parent = x.parent;

        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        if (y == null) return;

        x.left = y.right;
        if (y.right != null) y.right.parent = x;

        y.parent = x.parent;

        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        y.right = x;
        x.parent = y;
    }
}
