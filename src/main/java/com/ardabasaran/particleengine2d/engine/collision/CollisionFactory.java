package com.ardabasaran.particleengine2d.engine.collision;

import com.ardabasaran.particleengine2d.config.UniverseConfig;
import com.ardabasaran.particleengine2d.engine.collision.border.BorderCollisionHandler;
import com.ardabasaran.particleengine2d.engine.collision.border.DefaultBorderCollisionHandler;
import com.ardabasaran.particleengine2d.engine.collision.border.ExponentialBorderCollisionHandler;
import com.ardabasaran.particleengine2d.engine.collision.detector.CollisionDetector;
import com.ardabasaran.particleengine2d.engine.collision.detector.CollisionDetectorType;
import com.ardabasaran.particleengine2d.engine.collision.detector.NoCollisionDetector;
import com.ardabasaran.particleengine2d.engine.collision.detector.PairwiseCollisionDetector;
import com.ardabasaran.particleengine2d.engine.collision.detector.SortedCollisionDetector;
import com.ardabasaran.particleengine2d.engine.collision.resolver.CollisionResolver;
import com.ardabasaran.particleengine2d.engine.collision.resolver.CollisionResolverType;
import com.ardabasaran.particleengine2d.engine.collision.resolver.ElasticCollisionResolver;
import com.ardabasaran.particleengine2d.engine.collision.resolver.ExponentialCollisionResolver;
import com.ardabasaran.particleengine2d.engine.collision.resolver.NoCollisionResolver;

public class CollisionFactory {
    public static CollisionDetector createCollisionDetector(UniverseConfig config) {
        CollisionDetectorType type = config.getDetectorType();
        int numRanges = (int) Math.ceil(config.getHeight() / config.getMinimumDiameter());
        return switch (type) {
            case NO_COLLISION -> new NoCollisionDetector();
            case PAIRWISE -> new PairwiseCollisionDetector();
            case SORTED -> new SortedCollisionDetector(numRanges, config.getMinimumDiameter());
        };
    }

    public static CollisionResolver createCollisionResolver(UniverseConfig config) {
        CollisionResolverType type = config.getResolverType();
        double timeDelta = 1.0 / config.getTicksPerSecond() / config.getUpdatesPerTick();
        return switch (type) {
            case NO_COLLISION -> new NoCollisionResolver();
            case ELASTIC -> new ElasticCollisionResolver(config.getCoefficientOfRestitution());
            case EXPONENTIAL -> new ExponentialCollisionResolver(timeDelta, config.getCoefficientOfRestitution());
        };
    }

    public static BorderCollisionHandler createBorderCollisionHandler(UniverseConfig config) {
        String type = config.getBorderHandlerType();
        double timeDelta = 1.0 / config.getTicksPerSecond() / config.getUpdatesPerTick();
        if (type.equals("default")) {
            return new DefaultBorderCollisionHandler(
                    config.getWidth(), config.getHeight(), config.getCoefficientOfRestitution());
        } else if (type.equals("exponential")) {
            return new ExponentialBorderCollisionHandler(
                    config.getWidth(), config.getHeight(), timeDelta, config.getCoefficientOfRestitution());
        } else {
            throw new IllegalArgumentException("Unidentified collision resolver type:" + type);
        }
    }
}
