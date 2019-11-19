package com.ardabasaran.liquidengine2d.engine;

import com.ardabasaran.liquidengine2d.engine.utilities.Vector;

public class Particle {
  private double radius;
  private double mass;
  private Vector position;
  private Vector velocity;
  private Vector force;

  public Particle(double radius, double mass, double x, double y) {
    this.radius = radius;
    this.mass = mass;
    this.position = new Vector(x, y);
    this.force = new Vector(0, 0);
    this.velocity = new Vector(0, 0);
  }

  public Vector getForce() {
    return force;
  }

  public void update(double delta) {
    force.multiply(1/mass);
    force.multiply(delta);
    this.velocity.add(force);
    this.position.add(velocity.scalarProduct(delta));
    this.force.setX(0);
    this.force.setY(0);
  }

  public void applyForce(Vector force) {
    this.force.add(force);
  }

  public double getRadius() {
    return radius;
  }

  public double getMass() {
    return mass;
  }

  public Vector getPosition() {
    return this.position;
  }

  public Vector getVelocity() {
    return this.velocity;
  }

  public void setVelocity(Vector velocity) {
    this.velocity = velocity;
  }

  public boolean isColliding(Particle other) {
    double horizontalDistance = this.position.getX() - other.position.getX();
    double verticalDistance = this.position.getY() - other.position.getY();
    return (
        (this.radius + other.radius)*(this.radius + other.radius)
            >= horizontalDistance*horizontalDistance + verticalDistance*verticalDistance
    );
  }

  public boolean isMovingTowards(Particle other) {
    double distNow = this.getPosition().distanceSquared(other.getPosition());
    double distThen = this.getPosition().plus(this.getVelocity().scalarProduct(0.0001))
        .distanceSquared(other.getPosition().plus(other.getVelocity().scalarProduct(0.0001)));
    return distNow > distThen;
  }
}
