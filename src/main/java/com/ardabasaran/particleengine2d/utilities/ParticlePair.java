package com.ardabasaran.particleengine2d.utilities;

import com.ardabasaran.particleengine2d.engine.Particle;

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

    @Override
    public int hashCode() {
        return first.hashCode() * second.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (getClass() != other.getClass()) {
            return false;
        }
        ParticlePair otherParticlePair = (ParticlePair) other;
        boolean cond1 =
                this.first == otherParticlePair.first && this.second == otherParticlePair.second;
        boolean cond2 =
                this.first == otherParticlePair.second && this.second == otherParticlePair.first;
        return cond1 || cond2;
    }
}
