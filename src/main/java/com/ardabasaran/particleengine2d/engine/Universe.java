package com.ardabasaran.particleengine2d.engine;

import com.ardabasaran.particleengine2d.config.UniverseConfig;
import com.ardabasaran.particleengine2d.engine.collision.CollisionFactory;
import com.ardabasaran.particleengine2d.engine.collision.resolver.CollisionResolver;
import com.ardabasaran.particleengine2d.engine.collision.detector.CollisionDetector;
import com.ardabasaran.particleengine2d.utilities.Utilities;
import com.ardabasaran.particleengine2d.utilities.Vector2D;
import java.util.ArrayList;
import java.util.List;

public class Universe implements Runnable {
  private int x;
  private int y;
  private List<Particle> particles;
  private List<Particle> sortedParticles;
  private double GRAVITATIONAL_ACCELERATION;
  private double COEFFICIENT_OF_RESTITUTION;
  private boolean debug;
  private double timeDelta;
  private int numUpdatesPerTick;
  private int delay;
  private CollisionResolver collisionResolver;
  private CollisionDetector collisionDetector;

  public Universe(UniverseConfig config) {
    initFields(1.0/config.getTicksPerSecond()/config.getUpdatesPerTick(),
        config.getWidth(), config.getHeight(), config.getUpdatesPerTick(),
        1000/config.getTicksPerSecond(), config.getGravitationalAcceleration(),
        config.getCoefficientOfRestitution(), config.getDebug());
    initCollisionResolverAndDetector(config);
  }

  private void initFields(double timeDelta, int x, int y, int numUpdatesPerTick, int delay,
      double GRAVITATIONAL_ACCELERATION, double COEFFICIENT_OF_RESTITUTION, boolean debug) {
    this.timeDelta = timeDelta;
    this.x = x;
    this.y = y;
    this.numUpdatesPerTick = numUpdatesPerTick;
    this.delay = delay;
    this.particles = new ArrayList<>();
    this.sortedParticles = new ArrayList<>();
    this.debug = debug;
    this.GRAVITATIONAL_ACCELERATION = GRAVITATIONAL_ACCELERATION;
    this.COEFFICIENT_OF_RESTITUTION = COEFFICIENT_OF_RESTITUTION;
  }

  private void initCollisionResolverAndDetector(UniverseConfig config) {
    this.collisionResolver = CollisionFactory.createCollisionResolver(config);
    this.collisionDetector = CollisionFactory.createCollisionDetector(config);
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

  private void update() {
    for (int i = 0; i < this.numUpdatesPerTick; i++) {
      List<ParticlePair> collisions = collisionDetector.detectCollisions(sortedParticles);
      collisionResolver.resolveAllCollisions(collisions);
      resolveBorderCollisions();
      particles.forEach(this::applyGravity);
      particles.forEach(particle -> particle.update(this.timeDelta));
    }
  }

  private void handleBorderCollision(Particle particle, double x, boolean scaleDown, Vector2D direction) {
    int POWER = 15;
    double FORCE_MIN = 0;
    double magnitude = FORCE_MIN + (1/timeDelta) * (1/(Utilities.pow(x, 2*POWER)));
    Vector2D unit = direction.scalarProduct(1/direction.magnitude());

    if (!scaleDown) {
      particle.applyForce(unit.scalarProduct(magnitude));
    } else {
      particle.applyForce(unit.scalarProduct(magnitude).scalarProduct(COEFFICIENT_OF_RESTITUTION));
    }
  }

  private void resolveBorderCollisions() {
    for(Particle particle : this.sortedParticles) {
      double posY = particle.getPosition().getY();
      double posX = particle.getPosition().getX();
      double velY = particle.getVelocity().getY();
      double velX = particle.getVelocity().getX();
      double radius = particle.getRadius();

      if (posY - radius <= 0) {
        handleBorderCollision(particle, posY / radius, velY >= 0, new Vector2D(0, 1));
      }
      if (posY + radius >= this.getY()) {
        handleBorderCollision(particle, (this.getY() - posY) / radius, velY <= 0,
            new Vector2D(0, -1));
      }
      if (posX - radius <= 0) {
        handleBorderCollision(particle, posX / radius, velX >= 0, new Vector2D(1, 0));
      }
      if (posX + radius >= this.getX()) {
        handleBorderCollision(particle, (this.getX() - posX) / radius, velX <= 0,
            new Vector2D(-1, 0));
      }
    }
  }

  private void applyGravity(Particle particle) {
    if (particle.getPosition().getY() - particle.getRadius() > 0) {
      particle.applyForce(new Vector2D(0, -particle.getMass() * GRAVITATIONAL_ACCELERATION));
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
