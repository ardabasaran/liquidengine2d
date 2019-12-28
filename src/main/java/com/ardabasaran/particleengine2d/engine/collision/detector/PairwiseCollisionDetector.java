package com.ardabasaran.particleengine2d.engine.collision.detector;

import com.ardabasaran.particleengine2d.engine.Particle;
import com.ardabasaran.particleengine2d.engine.ParticlePair;
import java.util.ArrayList;
import java.util.List;

public class PairwiseCollisionDetector implements CollisionDetector {

  @Override
  public List<ParticlePair> detectCollisions(List<Particle> particleList) {
    List<ParticlePair> collisions = new ArrayList<>();
    for (int i = 0; i < particleList.size(); i++) {
      Particle particle = particleList.get(i);
      for (int j = i + 1; j < particleList.size(); j++) {
        Particle otherParticle = particleList.get(j);
        if (particle.isColliding(otherParticle)) {
          collisions.add(new ParticlePair(particle, otherParticle));
        }
      }
    }
    return collisions;
  }
}
