package com.ardabasaran.particleengine2d.engine.particlegroup;

import com.ardabasaran.particleengine2d.engine.Particle;
import com.ardabasaran.particleengine2d.utilities.ParticlePair;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParticleGroup {
  private Set<Particle> particles;
  private List<ParticlePair> particlePairs;

  public ParticleGroup() {
    particlePairs = new ArrayList<>();
    particles = new HashSet<>();
  }

  List<ParticlePair> getParticlePairs() {
    return particlePairs;
  }

  public void addPair(ParticlePair pair) {
    particlePairs.add(pair);
    particles.add(pair.getFirst());
    particles.add(pair.getSecond());
  }

  public Set<Particle> getParticles() {
    return particles;
  }
}
