package com.ardabasaran.liquidengine2d.engine;

import com.ardabasaran.liquidengine2d.engine.utilities.Utilities;
import com.ardabasaran.liquidengine2d.engine.utilities.Vector;
import java.util.ArrayList;
import java.util.List;

public class Universe implements Runnable {
  private int x;
  private int y;
  private List<Particle> particles;
  private List<Particle> sortedParticles;
  private final double GRAVITATIONAL_ACCELERATION;
  private final double COEFFICIENT_OF_RESTITUTION;
  private static final int POWER = 15;
  private static final double FORCE_MIN = 0;
  private boolean debug;
  private double timeDelta;
  private int updateRatio;
  private int delay;
  private int numRanges;
  private double minDiameter;

  public Universe(double timeDelta, int x, int y, int updateRatio, int delay, double minDiameter,
      double GRAVITATIONAL_ACCELERATION, double COEFFICIENT_OF_RESTITUTION, boolean debug) {
    this.timeDelta = timeDelta;
    this.x = x;
    this.y = y;
    this.updateRatio = updateRatio;
    this.delay = delay;
    this.particles = new ArrayList<>();
    this.sortedParticles = new ArrayList<>();
    this.minDiameter = minDiameter;
    this.debug = debug;
    this.GRAVITATIONAL_ACCELERATION = GRAVITATIONAL_ACCELERATION;
    this.COEFFICIENT_OF_RESTITUTION = COEFFICIENT_OF_RESTITUTION;
    this.numRanges = (int) Math.ceil(this.y / minDiameter);
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
    this.resolveCollisions();
    particles.forEach(this::applyGravity);
    particles.forEach(particle -> particle.update(this.timeDelta));
  }

  private void handleCollision(Particle particle1, Particle particle2) {
    double remaining = particle1.getPosition().distanceSquared(particle2.getPosition());
    double x = remaining / Utilities.pow(particle1.getRadius()+particle2.getRadius(), 2);
    double magnitude = FORCE_MIN + (1/timeDelta) * (1/(Utilities.pow(x, POWER)));

    Vector direction = particle1.getPosition().minus(particle2.getPosition());
    Vector unit1 = direction.scalarProduct(1/direction.magnitude());
    Vector unit2 = new Vector(unit1.getX(), unit1.getY());

    if (particle1.isMovingTowards(particle2)) {
      unit1.multiply(magnitude);
      unit2.multiply(-1);
      unit2.multiply(magnitude);
    } else {
      unit1.multiply(magnitude);
      unit1.multiply(COEFFICIENT_OF_RESTITUTION);
      unit2.multiply(-1);
      unit2.multiply(magnitude);
      unit2.multiply(COEFFICIENT_OF_RESTITUTION);
    }

    particle1.applyForce(unit1);
    particle2.applyForce(unit2);
  }

  private void handleBorderCollision(Particle particle, double x, boolean scaleDown, Vector direction) {
    double magnitude = FORCE_MIN + (1/timeDelta) * (1/(Utilities.pow(x, 2*POWER)));
    Vector unit = direction.scalarProduct(1/direction.magnitude());

    if (!scaleDown) {
      particle.applyForce(unit.scalarProduct(magnitude));
    } else {
      particle.applyForce(unit.scalarProduct(magnitude).scalarProduct(COEFFICIENT_OF_RESTITUTION));
    }
  }

  private void resolveBorderCollisions(Particle particle) {
    double posY = particle.getPosition().getY();
    double posX = particle.getPosition().getX();
    double velY = particle.getVelocity().getY();
    double velX = particle.getVelocity().getX();
    double radius = particle.getRadius();

    if (posY - radius <= 0) {
      handleBorderCollision(particle, posY / radius, velY >= 0, new Vector(0, 1));
    }
    if (posY + radius >= this.getY()) {
      handleBorderCollision(particle, (this.getY() - posY)/ radius, velY <= 0, new Vector(0, -1));
    }
    if (posX - radius <= 0) {
      handleBorderCollision(particle, posX / radius, velX >= 0, new Vector(1, 0));
    }
    if (posX + radius >= this.getX()) {
      handleBorderCollision(particle, (this.getX() - posX)/ radius, velX <= 0, new Vector(-1, 0));
    }
  }

  private void collisionSortParticles() {
    sortedParticles.sort((a, b) -> {
      int y1 = (int) Math.floor(a.getPosition().getY()/minDiameter);
      int y2 = (int) Math.floor(b.getPosition().getY()/minDiameter);
      if (y1 == y2) {
        return Double.compare(a.getPosition().getX(), b.getPosition().getX());
      } else {
        return Integer.compare(y1, y2);
      }
    });
  }

  private int[][] calculateIntervals() {
    int[][] intervals = new int[numRanges][2];

    int curr = 0;
    for(int i = 0; i < numRanges; i++) {
      intervals[i][0] = -1;
      intervals[i][1] = -1;
      while (curr < sortedParticles.size() && sortedParticles.get(curr).getPosition().getY() < i*minDiameter) {
        curr += 1;
      }
      if (curr >= sortedParticles.size()) {
        continue;
      }
      intervals[i][0] = curr;
      while (curr < sortedParticles.size() && sortedParticles.get(curr).getPosition().getY() < (i+1)*minDiameter) {
        curr += 1;
      }
      intervals[i][1] = curr - 1;
    }
    return intervals;
  }

  private void resolveHorizontalCollisions(Particle particle, int[][] intervals,
      int currentVertical, int particleIndex) {
    double xRangeLow = particle.getPosition().getX() - 2*particle.getRadius();
    double xRangeHigh = particle.getPosition().getX() + 2*particle.getRadius();
    int j = particleIndex + 1;

    while (j < sortedParticles.size() && j <= intervals[currentVertical][1] &&
        sortedParticles.get(j).getPosition().getX() <= xRangeHigh) {
      if (particle.isColliding(sortedParticles.get(j)))
        handleCollision(particle, sortedParticles.get(j));
      j += 1;
    }
  }

  private void resolveVerticalCollisions(Particle particle, int[][] intervals,
      int currentVertical, int numVerticals) {
    int j = 1;
    double xRangeLow = particle.getPosition().getX() - 2*particle.getRadius();
    double xRangeHigh = particle.getPosition().getX() + 2*particle.getRadius();
    while (j <= numVerticals) {
      if (currentVertical + j < numRanges) {
        if (intervals[currentVertical+j][0] == -1 && intervals[currentVertical+j][1] == -1) {
          j = j + 1;
          continue;
        }
        int left = Utilities.leftmostParticleIndex(
            sortedParticles,
            intervals[currentVertical+j][0],
            intervals[currentVertical+j][1],
            xRangeLow
        );
        int right = Utilities.rightmostParticleIndex(
            sortedParticles,
            intervals[currentVertical+j][0],
            intervals[currentVertical+j][1],
            xRangeHigh
        );

        for (int k = left; k <= right; k++) {
          if (particle != sortedParticles.get(k) && particle.isColliding(sortedParticles.get(k)))
            handleCollision(particle, sortedParticles.get(k));
        }
      }
      j = j + 1;
    }
  }

  private void resolveCollisions() {
    this.collisionSortParticles();
    int[][] intervals = calculateIntervals();

    for (int i = 0; i < sortedParticles.size(); i++ ) {
      Particle particle = sortedParticles.get(i);
      resolveBorderCollisions(particle);
      int currentVertical = (int) Math.floor(particle.getPosition().getY() / minDiameter);

      assert intervals[currentVertical][0] != -1;
      assert intervals[currentVertical][1] != -1;
      // Horizontal
      resolveHorizontalCollisions(particle, intervals, currentVertical, i);

      // Vertical
      int numVerticals = (int) Math.ceil(2*particle.getRadius() / minDiameter);
//      if (particle.getPosition().getY() < 0 || particle.getPosition().getY() > this.y) {
//        continue;
//      }
      resolveVerticalCollisions(particle, intervals, currentVertical, numVerticals);
    }
  }

  private void applyGravity(Particle particle) {
    if (particle.getPosition().getY() - particle.getRadius() > 0) {
      particle.applyForce(new Vector(0, -particle.getMass() * GRAVITATIONAL_ACCELERATION));
    }
  }

  @Override
  public void run() {
    while(true) {
      long startTime = System.nanoTime();
      for (int i = 0; i < this.updateRatio; i++)
        this.update();
      long endTime = System.nanoTime();
      if (this.debug)
        System.out.println((endTime - startTime)/1000000 - delay + " " + (endTime - startTime)/1000000 + " " + delay);
      if ((endTime - startTime)/1000000 < delay) {
        try {
          Thread.sleep(delay - (endTime - startTime)/1000000);
        } catch (InterruptedException ignored) {
        }
      }
    }
  }
}
