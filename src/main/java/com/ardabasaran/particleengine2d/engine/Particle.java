package com.ardabasaran.particleengine2d.engine;

import com.ardabasaran.particleengine2d.utilities.Vector2D;

public class Particle {
  private double radius;
  private double mass;
  private Vector2D position;
  private Vector2D velocity;
  private Vector2D force;

  public Particle(double radius, double mass, double x, double y) {
    this.radius = radius;
    this.mass = mass;
    this.position = new Vector2D(x, y);
    this.force = new Vector2D(0, 0);
    this.velocity = new Vector2D(0, 0);
  }

  void update(double delta) {
    force.multiply(1/mass);
    force.multiply(delta);
    this.velocity.add(force);
    this.position.add(velocity.scalarProduct(delta));
    this.force.setX(0);
    this.force.setY(0);
  }

  public void applyForce(Vector2D force) {
    this.force.add(force);
  }

  public double getRadius() {
    return radius;
  }

  public double getMass() {
    return mass;
  }

  public Vector2D getPosition() {
    return this.position;
  }

  public Vector2D getVelocity() {
    return this.velocity;
  }

  public void setVelocity(Vector2D velocity) {
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
    double velXdiff = 0.0001*(this.getVelocity().getX() - other.getVelocity().getX());
    double velYdiff = 0.0001*(this.getVelocity().getY() - other.getVelocity().getY());
    double posXdiff = this.getPosition().getX() - other.getPosition().getX();
    double posYdiff = this.getPosition().getY() - other.getPosition().getY();

    return velXdiff*velXdiff + velYdiff * velYdiff + 2*posXdiff*velXdiff + 2*posYdiff*velYdiff < 0;
  }
}
