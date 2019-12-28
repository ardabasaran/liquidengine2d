package com.ardabasaran.particleengine2d.engine.collision.resolver;

import com.ardabasaran.particleengine2d.engine.Particle;
import com.ardabasaran.particleengine2d.engine.ParticlePair;
import java.util.List;

public interface CollisionResolver {
  void resolveCollision(Particle firstParticle, Particle secondParticle);

  default void resolveAllCollisions(List<ParticlePair> particlePairs) {
    for (ParticlePair particlePair : particlePairs) {
      resolveCollision(particlePair.getFirst(), particlePair.getSecond());
    }
  }
}
