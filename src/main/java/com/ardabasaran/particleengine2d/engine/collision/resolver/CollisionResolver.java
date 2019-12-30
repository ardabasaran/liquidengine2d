package com.ardabasaran.particleengine2d.engine.collision.resolver;

import com.ardabasaran.particleengine2d.engine.Particle;
import com.ardabasaran.particleengine2d.utilities.ParticlePair;
import java.util.List;
import java.util.Set;

public interface CollisionResolver {
  void resolveCollision(Particle firstParticle, Particle secondParticle);

  default void resolveAllCollisions(Set<ParticlePair> particlePairs) {
    for (ParticlePair particlePair : particlePairs) {
      resolveCollision(particlePair.getFirst(), particlePair.getSecond());
    }
  }
}
