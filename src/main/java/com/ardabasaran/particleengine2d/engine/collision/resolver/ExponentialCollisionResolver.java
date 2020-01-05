package com.ardabasaran.particleengine2d.engine.collision.resolver;

import com.ardabasaran.particleengine2d.engine.Particle;
import com.ardabasaran.particleengine2d.utilities.ExponentialUtil;

public class ExponentialCollisionResolver implements CollisionResolver {
  private double coefficient_of_restitution;
  private double timeDelta;

  public ExponentialCollisionResolver(double timeDelta, double coefficient_of_restitution) {
    this.timeDelta = timeDelta;
    this.coefficient_of_restitution = coefficient_of_restitution;
  }

  @Override
  public void resolveCollision(Particle firstParticle, Particle secondParticle) {
    ExponentialUtil.applyCollidingForces(firstParticle, secondParticle,
        coefficient_of_restitution, timeDelta);
  }
}
