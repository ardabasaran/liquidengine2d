package com.ardabasaran.particleengine2d.engine.collision.border;

import com.ardabasaran.particleengine2d.engine.Particle;
import com.ardabasaran.particleengine2d.utilities.Utilities;
import com.ardabasaran.particleengine2d.utilities.Vector2D;

public class ExponentialBorderCollisionHandler implements BorderCollisionHandler {
  private double coefficient_of_restitution;
  private double timeDelta;
  private int universeWidth;
  private int universeHeight;
  private static final int POWER = 15;
  private static final double FORCE_MIN = 0;

  public ExponentialBorderCollisionHandler(int universeWidth, int universeHeight, double timeDelta,
      double coefficient_of_restitution) {
    this.universeWidth = universeWidth;
    this.universeHeight = universeHeight;
    this.timeDelta = timeDelta;
    this.coefficient_of_restitution = coefficient_of_restitution;
  }

  private void handleBorderCollision(Particle particle, double x, boolean scaleDown, Vector2D direction) {
    int POWER = 15;
    double FORCE_MIN = 0;
    double magnitude = FORCE_MIN + (1/timeDelta) * (1/(Utilities.pow(x, 2*POWER)));
    Vector2D unit = direction.scalarProduct(1/direction.magnitude());

    if (!scaleDown) {
      particle.applyForce(unit.scalarProduct(magnitude));
    } else {
      particle.applyForce(unit.scalarProduct(magnitude).scalarProduct(coefficient_of_restitution));
    }
  }

  @Override
  public void resolveCollision(Particle particle) {
    double posY = particle.getPosition().getY();
    double posX = particle.getPosition().getX();
    double velY = particle.getVelocity().getY();
    double velX = particle.getVelocity().getX();
    double radius = particle.getRadius();

    if (posY - radius <= 0) {
      handleBorderCollision(particle, posY / radius, velY >= 0, new Vector2D(0, 1));
    }
    if (posY + radius >= this.universeHeight) {
      handleBorderCollision(particle, (this.universeHeight - posY) / radius, velY <= 0,
          new Vector2D(0, -1));
    }
    if (posX - radius <= 0) {
      handleBorderCollision(particle, posX / radius, velX >= 0, new Vector2D(1, 0));
    }
    if (posX + radius >= this.universeWidth) {
      handleBorderCollision(particle, (this.universeWidth - posX) / radius, velX <= 0,
          new Vector2D(-1, 0));
    }
  }
}
