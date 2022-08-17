package com.ardabasaran.particleengine2d.utilities;

public class Vector2D {
    public double x;
    public double y;

    public Vector2D(double x, double y) {
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
        return Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY());
    }

    public boolean equals(Vector2D other) {
        return Utilities.doubleEquals(this.getX(), other.getX())
                && Utilities.doubleEquals(this.getY(), other.getY());
    }

    public void multiply(double c) {
        this.x = this.x * c;
        this.y = this.y * c;
    }

    public Vector2D scalarProduct(double c) {
        return new Vector2D(this.x * c, this.y * c);
    }

    public void add(Vector2D other) {
        this.x += other.x;
        this.y += other.y;
    }

    public void subtract(Vector2D other) {
        this.x -= other.x;
        this.y -= other.y;
    }

    public double dot(Vector2D other) {
        return this.x * other.x + this.y * other.y;
    }

    public Vector2D minus(Vector2D other) {
        return new Vector2D(this.x - other.getX(), this.y - other.getY());
    }

    public Vector2D plus(Vector2D other) {
        return new Vector2D(this.x + other.getX(), this.y + other.getY());
    }

    public double distanceSquared(Vector2D other) {
        double a = this.getX() - other.getX();
        double b = this.getY() - other.getY();
        return a * a + b * b;
    }

    public String toString() {
        return "Vector2D(x:"
                + String.format("%.3f", this.getX())
                + " y:"
                + String.format("%.3f", this.getY())
                + ")";
    }
}
