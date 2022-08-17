package com.ardabasaran.particleengine2d.engine.collision.detector;

import com.ardabasaran.particleengine2d.engine.Particle;
import com.ardabasaran.particleengine2d.utilities.ParticlePair;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PairwiseCollisionDetector implements CollisionDetector {

    @Override
    public Set<ParticlePair> detectCollisions(List<Particle> particleList) {
        Set<ParticlePair> collisions = new HashSet<>();
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
