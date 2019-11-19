package com.ardabasaran.liquidengine2d.engine.utilities;

public class Vector {
  public double x;
  public double y;

  public Vector(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double magnitude() {
    return Math.sqrt(this.getX()*this.getX() + this.getY()*this.getY());
  }

  public boolean equals(Vector other) {
    return Utilities.doubleEquals(this.getX(), other.getX()) &&
        Utilities.doubleEquals(this.getY(), other.getY());
  }

  public void multiply(double c) {
    this.x = this.x * c;
    this.y = this.y * c;
  }

  public Vector scalarProduct(double c) {
    return new Vector(this.x * c, this.y * c);
  }

  public void add(Vector other) {
    this.x += other.x;
    this.y += other.y;
  }

  public void subtract(Vector other) {
    this.x -= other.x;
    this.y -= other.y;
  }

  public double dot(Vector other) {
    return this.x * other.x + this.y * other.y;
  }

  public Vector minus(Vector other) {
    return new Vector(this.x - other.getX(), this.y - other.getY());
  }

  public Vector plus(Vector other) {
    return new Vector(this.x + other.getX(), this.y + other.getY());
  }

  public double distanceSquared(Vector other) {
    double a = this.getX() - other.getX();
    double b = this.getY() - other.getY();
    return a*a + b*b;
  }

  public String toString() {
    return "Vector(x:" +  String.format("%.3f", this.getX()) + " y:"
        + String.format("%.3f", this.getY()) + ")";
  }
}
