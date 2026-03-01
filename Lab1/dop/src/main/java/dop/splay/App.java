package dop.splay;

public class App {
  public static void main(String[] args) {
    SplayTree t = new SplayTree();
    t.insert(10);
    t.insert(5);
    t.insert(20);
    t.contains(5);
    t.delete(10);
    System.out.println("InOrder = " + t.inOrder());
    System.out.println("Size    = " + t.size());
  }
}