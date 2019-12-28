package com.ardabasaran.particleengine2d.engine.utilities;

import static org.junit.Assert.*;

import com.ardabasaran.particleengine2d.utilities.Utilities;
import com.ardabasaran.particleengine2d.utilities.Vector2D;
import org.junit.Before;
import org.junit.Test;

public class Vector2DTest {
  private Vector2D length36;
  private Vector2D length9;
  private Vector2D length25;
  private Vector2D threeFive;
  private Vector2D threeZero;
  private Vector2D oneTwo;
  private Vector2D twoOne;

  @Before
  public void setUp() {
    length36 = new Vector2D(3, 5);
    length9 = new Vector2D(3, 0);
    length25 = new Vector2D(0, 5);
    threeFive = new Vector2D(3, 5);
    threeZero = new Vector2D(3, 0);
    oneTwo = new Vector2D(1 ,2);
    twoOne = new Vector2D(2, 1);
  }

  @Test
  public void magnitude() {
    assertTrue(Utilities.doubleEquals(length36.magnitude(), Math.sqrt(34.0)));
    assertTrue(Utilities.doubleEquals(length25.magnitude(), Math.sqrt(25.0)));
    assertTrue(Utilities.doubleEquals(length9.magnitude(), Math.sqrt(9.0)));
    assertTrue(Utilities.doubleEquals(oneTwo.magnitude(), twoOne.magnitude()));
  }

  @Test
  public void equals() {
    assertTrue(length36.equals(threeFive));
    assertTrue(threeFive.equals(length36));
    assertTrue(threeFive.equals(threeFive));
    assertFalse(length36.equals(oneTwo));
    assertTrue(threeZero.equals(new Vector2D(3, 0)));
  }

  @Test
  public void multiply() {
    oneTwo.multiply(3);
    threeFive.multiply(1);
    twoOne.multiply(0);
    assertTrue(new Vector2D(3, 6).equals(oneTwo));
    assertTrue(new Vector2D(3, 5).equals(threeFive));
    assertTrue(new Vector2D(0, 0).equals(twoOne));
  }

  @Test
  public void scalarProduct() {
    assertTrue(new Vector2D(3, 6).equals(oneTwo.scalarProduct(3)));
    assertTrue(new Vector2D(3, 5).equals(threeFive.scalarProduct(1)));
    assertTrue(new Vector2D(0, 0).equals(twoOne.scalarProduct(0)));
  }

  @Test
  public void add() {
    oneTwo.add(new Vector2D(1, 2));
    twoOne.add(new Vector2D(0, 0));
    threeZero.add(new Vector2D(0.5, 0.4));
    threeFive.add(new Vector2D(-3, -7));
    assertTrue(oneTwo.equals(new Vector2D(2, 4)));
    assertTrue(twoOne.equals(new Vector2D(2, 1)));
    assertTrue(threeZero.equals(new Vector2D(3.5, 0.4)));
    assertTrue(threeFive.equals(new Vector2D(0, -2)));
  }

  @Test
  public void subtract() {
    oneTwo.subtract(new Vector2D(1, 2));
    twoOne.subtract(new Vector2D(0, 0));
    threeZero.subtract(new Vector2D(0.5, 0.4));
    threeFive.subtract(new Vector2D(-3, -7));
    assertTrue(oneTwo.equals(new Vector2D(0, 0)));
    assertTrue(twoOne.equals(new Vector2D(2, 1)));
    assertTrue(threeZero.equals(new Vector2D(2.5, -0.4)));
    assertTrue(threeFive.equals(new Vector2D(6, 12)));
  }

  @Test
  public void dot() {
    assertTrue(Utilities.doubleEquals(4, oneTwo.dot(twoOne)));
    assertTrue(Utilities.doubleEquals(11, threeFive.dot(twoOne)));
    assertTrue(Utilities.doubleEquals(9, threeZero.dot(length36)));
  }

  @Test
  public void minus() {
    assertTrue(oneTwo.minus(new Vector2D(1, 2)).equals(new Vector2D(0, 0)));
    assertTrue(twoOne.minus(new Vector2D(0, 0)).equals(new Vector2D(2, 1)));
    assertTrue(threeZero.minus(new Vector2D(0.5, 0.4)).equals(new Vector2D(2.5, -0.4)));
    assertTrue(threeFive.minus(new Vector2D(-3, -7)).equals(new Vector2D(6, 12)));
  }

  @Test
  public void plus() {
    assertTrue(oneTwo.plus(new Vector2D(1, 2)).equals(new Vector2D(2, 4)));
    assertTrue(twoOne.plus(new Vector2D(0, 0)).equals(new Vector2D(2, 1)));
    assertTrue(threeZero.plus(new Vector2D(0.5, 0.4)).equals(new Vector2D(3.5, 0.4)));
    assertTrue(threeFive.plus(new Vector2D(-3, -7)).equals(new Vector2D(0, -2)));
  }

  @Test
  public void distanceSquared() {
    assertTrue(Utilities.doubleEquals(oneTwo.distanceSquared(twoOne), 2));
    assertTrue(Utilities.doubleEquals(oneTwo.distanceSquared(threeZero), 8));
    assertTrue(Utilities.doubleEquals(threeZero.distanceSquared(threeZero), 0));
    assertTrue(Utilities.doubleEquals(threeZero.distanceSquared(threeFive), 25));
  }
}