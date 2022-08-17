package com.ardabasaran.particleengine2d.engine.particlegroup;

import com.ardabasaran.particleengine2d.config.UniverseConfig;

public class GroupHandlerFactory {
    public static ParticleGroupHandler createParticleGroupHandler(UniverseConfig config) {
        String type = config.getParticleGroupHandlerType();
        double timeDelta = 1.0 / config.getTicksPerSecond() / config.getUpdatesPerTick();
        if (type.equals("exponential")) {
            return new ExponentialParticleGroupHandler(timeDelta, config.getCoefficientOfRestitution());
        } else if (type.equals("no")) {
            return new NoParticleGroupHandler();
        } else {
            throw new IllegalArgumentException("Unidentified particle group handler type:" + type);
        }
    }
}
