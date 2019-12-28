package com.ardabasaran.particleengine2d.engine.collision.detector;

import com.ardabasaran.particleengine2d.engine.Particle;
import com.ardabasaran.particleengine2d.engine.ParticlePair;
import java.util.List;

public interface CollisionDetector {
  List<ParticlePair> detectCollisions(List<Particle> particleList);
}
