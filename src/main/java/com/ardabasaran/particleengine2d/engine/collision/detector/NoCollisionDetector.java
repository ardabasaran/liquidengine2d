package com.ardabasaran.particleengine2d.engine.collision.detector;

import com.ardabasaran.particleengine2d.engine.Particle;
import com.ardabasaran.particleengine2d.utilities.ParticlePair;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NoCollisionDetector implements CollisionDetector {

  @Override
  public Set<ParticlePair> detectCollisions(List<Particle> particleList) {
    return new HashSet<>();
  }
}
