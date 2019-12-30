package com.ardabasaran.particleengine2d.engine.collision.border;

import com.ardabasaran.particleengine2d.engine.Particle;
import java.util.List;

public interface BorderCollisionHandler {
  void resolveCollision(Particle particle);

  default void resolveAllCollisions(List<Particle> particles) {
    for (Particle particle : particles) {
      resolveCollision(particle);
    }
  }
}
