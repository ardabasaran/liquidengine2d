package com.ardabasaran.particleengine2d.engine.collision.resolver;

import com.ardabasaran.particleengine2d.engine.Particle;
import com.ardabasaran.particleengine2d.utilities.Utilities;
import com.ardabasaran.particleengine2d.utilities.Vector2D;

public class ExponentialCollisionResolver implements CollisionResolver {
  private double coefficient_of_restitution;
  private double timeDelta;
  private static final int POWER = 15;
  private static final double FORCE_MIN = 0;

  public ExponentialCollisionResolver(double timeDelta, double coefficient_of_restitution) {
    this.timeDelta = timeDelta;
    this.coefficient_of_restitution = coefficient_of_restitution;
  }

  private double getRemainingDistance(Particle firstParticle, Particle secondParticle) {
    return firstParticle.getPosition().distanceSquared(secondParticle.getPosition());
  }

  private double getRemainingRatio(double remaining, Particle firstParticle, Particle secondParticle) {
    return remaining / Utilities.pow(firstParticle.getRadius()+secondParticle.getRadius(), 2);
  }

  private double getMagnitude(double x) {
    return FORCE_MIN + (1/timeDelta) * (1/(Utilities.pow(x, POWER)));
  }

  private void calculateForcesToApplyToParticlesNotMovingTowards(Vector2D unit1, Vector2D unit2, double magnitude) {
    unit1.multiply(magnitude);
    unit1.multiply(coefficient_of_restitution);
    unit2.multiply(-1);
    unit2.multiply(magnitude);
    unit2.multiply(coefficient_of_restitution);
  }

  private void calculateForcesToApplyToParticlesMovingTowards(Vector2D unit1, Vector2D unit2, double magnitude) {
    unit1.multiply(magnitude);
    unit2.multiply(-1);
    unit2.multiply(magnitude);
  }

  private void calculateForcesToApplyToParticles(Vector2D unit1, Vector2D unit2, Particle firstParticle,
      Particle secondParticle, double magnitude) {
    if (firstParticle.isMovingTowards(secondParticle)) {
      calculateForcesToApplyToParticlesMovingTowards(unit1, unit2, magnitude);
    } else {
      calculateForcesToApplyToParticlesNotMovingTowards(unit1, unit2, magnitude);
    }
  }

  private void applyForcesToCollidingParticles(Particle firstParticle, Particle secondParticle,
      double magnitude) {
    Vector2D direction = firstParticle.getPosition().minus(secondParticle.getPosition());
    Vector2D unit1 = direction.scalarProduct(1/direction.magnitude());
    Vector2D unit2 = new Vector2D(unit1.getX(), unit1.getY());
    calculateForcesToApplyToParticles(unit1, unit2, firstParticle, secondParticle, magnitude);
    firstParticle.applyForce(unit1);
    secondParticle.applyForce(unit2);
  }

  @Override
  public void resolveCollision(Particle firstParticle, Particle secondParticle) {
    double remaining = getRemainingDistance(firstParticle, secondParticle);
    double x = getRemainingRatio(remaining, firstParticle, secondParticle);
    double magnitude = getMagnitude(x);
    applyForcesToCollidingParticles(firstParticle, secondParticle, magnitude);
  }
}
