package com.ardabasaran.particleengine2d.engine.collision.detector;

import com.ardabasaran.particleengine2d.engine.Particle;
import com.ardabasaran.particleengine2d.utilities.ParticlePair;
import java.util.List;
import java.util.Set;

public interface CollisionDetector {
    Set<ParticlePair> detectCollisions(List<Particle> particleList);
}
