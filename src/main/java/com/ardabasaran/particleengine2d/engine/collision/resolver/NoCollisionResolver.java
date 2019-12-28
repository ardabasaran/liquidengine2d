package com.ardabasaran.particleengine2d.engine.collision.resolver;

import com.ardabasaran.particleengine2d.engine.Particle;
import com.ardabasaran.particleengine2d.engine.ParticlePair;
import java.util.List;

public class NoCollisionResolver implements CollisionResolver {
  @Override
  public void resolveCollision(Particle firstParticle, Particle secondParticle) {

  }

  @Override
  public void resolveAllCollisions(List<ParticlePair> particlePairs) {
  }
}
