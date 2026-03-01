package dop.splay;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public final class World {
  public final SplayTree sut = new SplayTree();
  public final TreeSet<Integer> model = new TreeSet<>();

  public void assertAgreement() {
    List<Integer> expected = new ArrayList<>(model);
    List<Integer> actual = sut.inOrder();
    if (!actual.equals(expected)) {
      throw new AssertionError("Content mismatch.\nSUT   =" + actual + "\nMODEL =" + expected);
    }

    SplayTreeInvariants.assertAll(sut);
  }
}