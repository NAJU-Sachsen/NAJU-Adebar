package de.naju.adebar.util;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PairUnitTest {

  @Test
  public void testCompareToLess() {
    Pair<Integer, Integer> p11 = new Pair<Integer, Integer>(1, 2);
    Pair<Integer, Integer> p12 = new Pair<Integer, Integer>(3, 4);
    assertTrue(String.format("p11 %s should be less than p12 %s", p11, p12),
        p11.compareTo(p12) < 0);

    Pair<Integer, Integer> p21 = new Pair<Integer, Integer>(1, 4);
    Pair<Integer, Integer> p22 = new Pair<Integer, Integer>(2, 3);
    assertTrue(String.format("p21 %s should be less than p22 %s", p21, p22),
        p21.compareTo(p22) < 0);

    Pair<Integer, Integer> p31 = new Pair<Integer, Integer>(1, 2);
    Pair<Integer, Integer> p32 = new Pair<Integer, Integer>(1, 3);
    assertTrue(String.format("p31 %s should be less than p32 %s", p31, p32),
        p31.compareTo(p32) < 0);
  }

  @Test
  public void testCompareEqual() {
    Pair<Integer, Integer> p1 = new Pair<Integer, Integer>(1, 2);
    Pair<Integer, Integer> p2 = new Pair<Integer, Integer>(1, 2);

    assertTrue(String.format("p1 %s should be equal to p2 %s", p1, p2), p1.compareTo(p2) == 0);
  }

  @Test
  public void testCompareGreater() {
    Pair<Integer, Integer> p11 = new Pair<Integer, Integer>(3, 4);
    Pair<Integer, Integer> p12 = new Pair<Integer, Integer>(1, 2);
    assertTrue(String.format("p11 %s should be greater than p12 %s", p11, p12),
        p11.compareTo(p12) > 0);

    Pair<Integer, Integer> p21 = new Pair<Integer, Integer>(2, 3);
    Pair<Integer, Integer> p22 = new Pair<Integer, Integer>(1, 4);
    assertTrue(String.format("p21 %s should be greater than p22 %s", p21, p22),
        p21.compareTo(p22) > 0);

    Pair<Integer, Integer> p31 = new Pair<Integer, Integer>(1, 3);
    Pair<Integer, Integer> p32 = new Pair<Integer, Integer>(1, 2);
    assertTrue(String.format("p31 %s should be greater than p32 %s", p31, p32),
        p31.compareTo(p32) > 0);
  }

}
