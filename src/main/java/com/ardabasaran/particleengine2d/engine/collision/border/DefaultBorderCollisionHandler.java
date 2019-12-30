package com.ardabasaran.particleengine2d.engine.collision.border;

import com.ardabasaran.particleengine2d.engine.Particle;
import com.ardabasaran.particleengine2d.utilities.Vector2D;
import java.util.List;

public class DefaultBorderCollisionHandler implements BorderCollisionHandler {
  private double coefficient_of_restitution;
  private int universeHeight;
  private int universeWidth;

  public DefaultBorderCollisionHandler(int universeWidth, int universeHeight, double coefficient_of_restitution) {
    this.universeHeight = universeHeight;
    this.universeWidth = universeWidth;
    this.coefficient_of_restitution = coefficient_of_restitution;
  }

  @Override
  public void resolveCollision(Particle particle) {
    double posY = particle.getPosition().getY();
    double posX = particle.getPosition().getX();
    double velY = particle.getVelocity().getY();
    double velX = particle.getVelocity().getX();
    double radius = particle.getRadius();

    if (posY - radius <= 0) {
      velY = Math.abs(velY)*coefficient_of_restitution;
    }
    if (posY + radius >= this.universeHeight) {
      velY = -Math.abs(velY)*coefficient_of_restitution;
    }
    if (posX - radius <= 0) {
      velX = Math.abs(velX)*coefficient_of_restitution;
    }
    if (posX + radius >= this.universeWidth) {
      velX = -Math.abs(velX)*coefficient_of_restitution;
    }
    particle.setVelocity(new Vector2D(velX, velY));
  }

  public void resolveAllCollisions(List<Particle> particles) {
    for (Particle particle : particles) {
      resolveCollision(particle);
    }
  }
}
