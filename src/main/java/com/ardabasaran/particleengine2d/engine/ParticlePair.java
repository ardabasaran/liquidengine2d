package com.ardabasaran.particleengine2d.engine;

public class ParticlePair {
  private Particle first;
  private Particle second;

  public ParticlePair(Particle first, Particle second) {
    this.first = first;
    this.second = second;
  }

  public Particle getSecond() {
    return second;
  }

  public void setSecond(Particle second) {
    this.second = second;
  }

  public Particle getFirst() {
    return first;
  }

  public void setFirst(Particle first) {
    this.first = first;
  }
}
