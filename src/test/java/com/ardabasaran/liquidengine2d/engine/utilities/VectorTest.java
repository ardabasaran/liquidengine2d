package com.ardabasaran.liquidengine2d.engine.utilities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class VectorTest {
  private Vector length36;
  private Vector length9;
  private Vector length25;
  private Vector threeFive;
  private Vector threeZero;
  private Vector oneTwo;
  private Vector twoOne;

  @Before
  public void setUp() {
    length36 = new Vector(3, 5);
    length9 = new Vector(3, 0);
    length25 = new Vector(0, 5);
    threeFive = new Vector(3, 5);
    threeZero = new Vector(3, 0);
    oneTwo = new Vector(1 ,2);
    twoOne = new Vector(2, 1);
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
    assertTrue(threeZero.equals(new Vector(3, 0)));
  }

  @Test
  public void multiply() {
    oneTwo.multiply(3);
    threeFive.multiply(1);
    twoOne.multiply(0);
    assertTrue(new Vector(3, 6).equals(oneTwo));
    assertTrue(new Vector(3, 5).equals(threeFive));
    assertTrue(new Vector(0, 0).equals(twoOne));
  }

  @Test
  public void scalarProduct() {
    assertTrue(new Vector(3, 6).equals(oneTwo.scalarProduct(3)));
    assertTrue(new Vector(3, 5).equals(threeFive.scalarProduct(1)));
    assertTrue(new Vector(0, 0).equals(twoOne.scalarProduct(0)));
  }

  @Test
  public void add() {
    oneTwo.add(new Vector(1, 2));
    twoOne.add(new Vector(0, 0));
    threeZero.add(new Vector(0.5, 0.4));
    threeFive.add(new Vector(-3, -7));
    assertTrue(oneTwo.equals(new Vector(2, 4)));
    assertTrue(twoOne.equals(new Vector(2, 1)));
    assertTrue(threeZero.equals(new Vector(3.5, 0.4)));
    assertTrue(threeFive.equals(new Vector(0, -2)));
  }

  @Test
  public void subtract() {
    oneTwo.subtract(new Vector(1, 2));
    twoOne.subtract(new Vector(0, 0));
    threeZero.subtract(new Vector(0.5, 0.4));
    threeFive.subtract(new Vector(-3, -7));
    assertTrue(oneTwo.equals(new Vector(0, 0)));
    assertTrue(twoOne.equals(new Vector(2, 1)));
    assertTrue(threeZero.equals(new Vector(2.5, -0.4)));
    assertTrue(threeFive.equals(new Vector(6, 12)));
  }

  @Test
  public void dot() {
    assertTrue(Utilities.doubleEquals(4, oneTwo.dot(twoOne)));
    assertTrue(Utilities.doubleEquals(11, threeFive.dot(twoOne)));
    assertTrue(Utilities.doubleEquals(9, threeZero.dot(length36)));
  }

  @Test
  public void minus() {
    assertTrue(oneTwo.minus(new Vector(1, 2)).equals(new Vector(0, 0)));
    assertTrue(twoOne.minus(new Vector(0, 0)).equals(new Vector(2, 1)));
    assertTrue(threeZero.minus(new Vector(0.5, 0.4)).equals(new Vector(2.5, -0.4)));
    assertTrue(threeFive.minus(new Vector(-3, -7)).equals(new Vector(6, 12)));
  }

  @Test
  public void plus() {
    assertTrue(oneTwo.plus(new Vector(1, 2)).equals(new Vector(2, 4)));
    assertTrue(twoOne.plus(new Vector(0, 0)).equals(new Vector(2, 1)));
    assertTrue(threeZero.plus(new Vector(0.5, 0.4)).equals(new Vector(3.5, 0.4)));
    assertTrue(threeFive.plus(new Vector(-3, -7)).equals(new Vector(0, -2)));
  }

  @Test
  public void distanceSquared() {
    assertTrue(Utilities.doubleEquals(oneTwo.distanceSquared(twoOne), 2));
    assertTrue(Utilities.doubleEquals(oneTwo.distanceSquared(threeZero), 8));
    assertTrue(Utilities.doubleEquals(threeZero.distanceSquared(threeZero), 0));
    assertTrue(Utilities.doubleEquals(threeZero.distanceSquared(threeFive), 25));
  }
}