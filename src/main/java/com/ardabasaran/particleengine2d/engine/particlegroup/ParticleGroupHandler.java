package com.ardabasaran.particleengine2d.engine.particlegroup;

import com.ardabasaran.particleengine2d.utilities.ParticlePair;
import java.util.List;

public interface ParticleGroupHandler {
    default void resolveParticleGroup(ParticleGroup particleGroup) {
        List<ParticlePair> particlePairs = particleGroup.getParticlePairs();
        particlePairs.forEach(this::resolveParticlePair);
    }

    default void resolve(List<ParticleGroup> particleGroups) {
        particleGroups.forEach(this::resolveParticleGroup);
    }

    void resolveParticlePair(ParticlePair particlePair);
}
