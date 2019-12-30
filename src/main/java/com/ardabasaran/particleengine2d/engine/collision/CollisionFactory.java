package com.ardabasaran.particleengine2d.engine.collision;

import com.ardabasaran.particleengine2d.config.UniverseConfig;
import com.ardabasaran.particleengine2d.engine.collision.border.BorderCollisionHandler;
import com.ardabasaran.particleengine2d.engine.collision.border.DefaultBorderCollisionHandler;
import com.ardabasaran.particleengine2d.engine.collision.border.ExponentialBorderCollisionHandler;
import com.ardabasaran.particleengine2d.engine.collision.detector.CollisionDetector;
import com.ardabasaran.particleengine2d.engine.collision.detector.NoCollisionDetector;
import com.ardabasaran.particleengine2d.engine.collision.detector.PairwiseCollisionDetector;
import com.ardabasaran.particleengine2d.engine.collision.detector.SortedCollisionDetector;
import com.ardabasaran.particleengine2d.engine.collision.resolver.CollisionResolver;
import com.ardabasaran.particleengine2d.engine.collision.resolver.ElasticCollisionResolver;
import com.ardabasaran.particleengine2d.engine.collision.resolver.ExponentialCollisionResolver;
import com.ardabasaran.particleengine2d.engine.collision.resolver.NoCollisionResolver;

public class CollisionFactory {
  public static CollisionDetector createCollisionDetector(UniverseConfig config) {
    String type = config.getDetectorType();
    int numRanges = (int) Math.ceil(config.getHeight() / config.getMinimumDiameter());
    if (type.equals("sorted")) {
      return new SortedCollisionDetector(numRanges, config.getMinimumDiameter());
    } else if (type.equals("pairwise")) {
      return new PairwiseCollisionDetector();
    } else if (type.equals("no")) {
      return new NoCollisionDetector();
    } else {
      throw new IllegalArgumentException("Unidentified collision detector type:" + type);
    }
  }

  public static CollisionResolver createCollisionResolver(UniverseConfig config) {
    String type = config.getResolverType();
    double timeDelta = 1.0/config.getTicksPerSecond()/config.getUpdatesPerTick();
    if (type.equals("exponential")) {
      return new ExponentialCollisionResolver(timeDelta, config.getCoefficientOfRestitution());
    } else if (type.equals("elastic")) {
      return new ElasticCollisionResolver(config.getCoefficientOfRestitution());
    } else if (type.equals("no")) {
      return new NoCollisionResolver();
    } else {
      throw new IllegalArgumentException("Unidentified collision resolver type:" + type);
    }
  }

  public static BorderCollisionHandler createBorderCollisionHandler(UniverseConfig config) {
    String type = config.getBorderHandlerType();
    double timeDelta = 1.0/config.getTicksPerSecond()/config.getUpdatesPerTick();
    if (type.equals("default")) {
      return new DefaultBorderCollisionHandler(config.getWidth(), config.getHeight(),
          config.getCoefficientOfRestitution());
    } else if (type.equals("exponential")) {
      return new ExponentialBorderCollisionHandler(config.getWidth(), config.getHeight(), timeDelta,
          config.getCoefficientOfRestitution());
    } else {
      throw new IllegalArgumentException("Unidentified collision resolver type:" + type);
    }
  }
}
