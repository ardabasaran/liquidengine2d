package com.ardabasaran.particleengine2d.engine.particlegroup;

import com.ardabasaran.particleengine2d.utilities.ExponentialUtil;
import com.ardabasaran.particleengine2d.utilities.ParticlePair;

public class ExponentialParticleGroupHandler implements ParticleGroupHandler {
  private double coefficient_of_restitution;
  private double timeDelta;

  ExponentialParticleGroupHandler(double timeDelta, double coefficient_of_restitution) {
    this.timeDelta = timeDelta;
    this.coefficient_of_restitution = coefficient_of_restitution;
  }

  @Override
  public void resolveParticlePair(ParticlePair particlePair) {
    ExponentialUtil.applyPullingForces(particlePair.getFirst(), particlePair.getSecond(),
        coefficient_of_restitution, timeDelta);
  }
}

