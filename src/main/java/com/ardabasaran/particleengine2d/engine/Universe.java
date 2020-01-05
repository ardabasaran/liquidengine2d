package com.ardabasaran.particleengine2d.engine;

import com.ardabasaran.particleengine2d.config.UniverseConfig;
import com.ardabasaran.particleengine2d.engine.collision.CollisionFactory;
import com.ardabasaran.particleengine2d.engine.collision.border.BorderCollisionHandler;
import com.ardabasaran.particleengine2d.engine.collision.resolver.CollisionResolver;
import com.ardabasaran.particleengine2d.engine.collision.detector.CollisionDetector;
import com.ardabasaran.particleengine2d.engine.particlegroup.GroupHandlerFactory;
import com.ardabasaran.particleengine2d.engine.particlegroup.ParticleGroup;
import com.ardabasaran.particleengine2d.engine.particlegroup.ParticleGroupHandler;
import com.ardabasaran.particleengine2d.utilities.ParticlePair;
import com.ardabasaran.particleengine2d.utilities.Utilities;
import com.ardabasaran.particleengine2d.utilities.Vector2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Universe implements Runnable {
  private int x;
  private int y;
  private List<Particle> particles;
  private List<Particle> sortedParticles;
  private double gravitationalAcceleration;
  private boolean debug;
  private double timeDelta;
  private int numUpdatesPerTick;
  private int delay;
  private CollisionResolver collisionResolver;
  private CollisionDetector collisionDetector;
  private BorderCollisionHandler borderCollisionHandler;
  private ParticleGroupHandler particleGroupHandler;
  private List<ParticleGroup> particleGroups;

  public Universe(UniverseConfig config) {
    initFields(1.0/config.getTicksPerSecond()/config.getUpdatesPerTick(),
        config.getWidth(), config.getHeight(), config.getUpdatesPerTick(),
        1000/config.getTicksPerSecond(), config.getGravitationalAcceleration(),
        config.getDebug());
    initCollisionComponents(config);
    initParticleGroupComponents(config);
  }

  private void initFields(double timeDelta, int x, int y, int numUpdatesPerTick, int delay,
      double gravitationalAcceleration, boolean debug) {
    this.timeDelta = timeDelta;
    this.x = x;
    this.y = y;
    this.numUpdatesPerTick = numUpdatesPerTick;
    this.delay = delay;
    this.particles = new ArrayList<>();
    this.sortedParticles = new ArrayList<>();
    this.debug = debug;
    this.gravitationalAcceleration = gravitationalAcceleration;
  }

  private void initCollisionComponents(UniverseConfig config) {
    this.collisionResolver = CollisionFactory.createCollisionResolver(config);
    this.collisionDetector = CollisionFactory.createCollisionDetector(config);
    this.borderCollisionHandler = CollisionFactory.createBorderCollisionHandler(config);
  }

  private void initParticleGroupComponents(UniverseConfig config) {
    this.particleGroups = new ArrayList<>();
    this.particleGroupHandler = GroupHandlerFactory.createParticleGroupHandler(config);
  }

  public List<Particle> getParticles() {
    return this.particles;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public void addParticle(Particle particle) {
    particles.add(particle);
    sortedParticles.add(particle);
  }

  public void addParticleGroup(ParticleGroup group) {
    particleGroups.add(group);
    group.getParticles().forEach(this::addParticle);
  }

  private void update() {
    for (int i = 0; i < this.numUpdatesPerTick; i++) {
      Set<ParticlePair> collisions = collisionDetector.detectCollisions(sortedParticles);
      collisionResolver.resolveAllCollisions(collisions);
      borderCollisionHandler.resolveAllCollisions(sortedParticles);
      particleGroupHandler.resolve(particleGroups);
      particles.forEach(this::applyGravity);
      particles.forEach(particle -> particle.update(this.timeDelta));
    }
  }

  private void applyGravity(Particle particle) {
    if (particle.getPosition().getY() - particle.getRadius() > 0) {
      particle.applyForce(new Vector2D(0, -particle.getMass() * gravitationalAcceleration));
    }
  }

  private void sleepIfUpdatedTooQuick(long startTime, long endTime) {
    if (this.debug) {
      System.out.println(Utilities.getTickUpdateInfo(startTime, endTime, delay));
    }
    if ((endTime - startTime)/1000000 < delay) {
      try {
        Thread.sleep(delay - (endTime - startTime)/1000000);
      } catch (InterruptedException ignored) {
      }
    }
  }

  @Override
  public void run() {
    while(true) {
      long startTime = System.nanoTime();
      this.update();
      long endTime = System.nanoTime();
      sleepIfUpdatedTooQuick(startTime, endTime);
    }
  }
}
