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
  private static final double GRAVITATIONAL_ACCELERATION = 9806.65;
  private static final double COEFFICIENT_OF_RESTITUTION = 0.9;
  private static final int POWER = 15;
  private static final double FORCE_MIN = 0;
  private double timeDelta;
  private int updateRatio;
  private int delay;
  private double minDiameter;

  public Universe(double timeDelta, int x, int y, int updateRatio, int delay, double minDiameter) {
    this.timeDelta = timeDelta;
    this.x = x;
    this.y = y;
    this.updateRatio = updateRatio;
    this.delay = delay;
    this.particles = new ArrayList<>();
    this.sortedParticles = new ArrayList<>();
    this.minDiameter = minDiameter;
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

  public void update() {
    this.resolveCollisions(particles);
    particles.forEach(Universe::applyForces);
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

  private int findLeft(List<Particle> particles, int l, int r, double val) {
    if (l == -1 && r == -1) {
      return -1;
    }
    while (r-1 > l) {
      int mid = (l + r) / 2;
      if (particles.get(mid).getPosition().getX() < val) {
        l = mid + 1;
      } else {
        r = mid;
      }
    }
    return l;
  }

  private int findRight(List<Particle> particles, int l, int r, double val) {
    if (l == -1 && r == -1) {
      return -1;
    }
    while (r-1 > l) {
      int mid = (l + r) / 2;
      if (particles.get(mid).getPosition().getX() > val) {
        r = mid - 1;
      } else {
        l = mid;
      }
    }
    return r;
  }

  private void resolveCollisions(List<Particle> particles) {
    sortedParticles.sort((a, b) -> {
      int y1 = (int) Math.floor(a.getPosition().getY()/minDiameter);
      int y2 = (int) Math.floor(b.getPosition().getY()/minDiameter);
      if (y1 == y2) {
        return Double.compare(a.getPosition().getX(), b.getPosition().getX());
      } else {
        return Integer.compare(y1, y2);
      }
    });


    int numRanges = (int) Math.ceil(this.y / minDiameter);

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

    for (int i = 0; i < sortedParticles.size(); i++ ) {
      Particle particle = sortedParticles.get(i);
//      if (particle.getPosition().getX() < 0 || particle.getPosition().getX() > this.x ||
//          particle.getPosition().getY() < 0 || particle.getPosition().getY() > this.y) {
//        continue;
//      }
      int currentVertical = (int) Math.floor(particle.getPosition().getY() / minDiameter);

      assert intervals[currentVertical][0] != -1;
      assert intervals[currentVertical][1] != -1;
      // Horizontal
      double xRangeLow = particle.getPosition().getX() - 2*particle.getRadius();
      double xRangeHigh = particle.getPosition().getX() + 2*particle.getRadius();
      int j = i + 1;

      while (j < sortedParticles.size() && j <= intervals[currentVertical][1] && sortedParticles.get(j).getPosition().getX() <= xRangeHigh) {
        if (particle.isColliding(sortedParticles.get(j)))
          handleCollision(particle, sortedParticles.get(j));
        j += 1;
      }

      // Vertical
      int numVerticals = (int) Math.ceil(2*particle.getRadius() / minDiameter);
      if (particle.getPosition().getY() < 0 || particle.getPosition().getY() > this.y) {
        continue;
      }
      j = 1;
      while (j <= numVerticals) {
        if (currentVertical + j < numRanges) {
          if (intervals[currentVertical+j][0] == -1 && intervals[currentVertical+j][1] == -1) {
            j = j + 1;
            continue;
          }
          int left = findLeft(
              sortedParticles,
              intervals[currentVertical+j][0],
              intervals[currentVertical+j][1],
              xRangeLow
          );

          int right = findRight(
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

    particles.forEach(particle -> {
      if (particle.getPosition().getY() - particle.getRadius() <= 0) {
        double remaining = particle.getPosition().getY();
        double x = remaining / (particle.getRadius());
        double magnitude = FORCE_MIN + (1/timeDelta) * (1/(Utilities.pow(x, 2*POWER)));
        Vector direction = new Vector(0, 1);
        Vector unit = direction.scalarProduct(1/direction.magnitude());

        if (particle.getVelocity().getY() < 0) {
          particle.applyForce(unit.scalarProduct(magnitude));
        } else {
          particle.applyForce(unit.scalarProduct(magnitude).scalarProduct(COEFFICIENT_OF_RESTITUTION));
        }
      }

      if (particle.getPosition().getY() + particle.getRadius() >= this.getY()) {
        double remaining = this.getY() - particle.getPosition().getY();
        double x = remaining / (particle.getRadius());
        double magnitude = FORCE_MIN + (1/timeDelta) * (1/(Utilities.pow(x, 2*POWER)));
        Vector direction = new Vector(0, -1);
        Vector unit = direction.scalarProduct(1/direction.magnitude());

        if (particle.getVelocity().getY() > 0) {
          particle.applyForce(unit.scalarProduct(magnitude));
        } else {
          particle
              .applyForce(unit.scalarProduct(magnitude).scalarProduct(COEFFICIENT_OF_RESTITUTION));
        }
      }

      if (particle.getPosition().getX() - particle.getRadius() <= 0) {
        double remaining = particle.getPosition().getX();
        double x = remaining / (particle.getRadius());
        double magnitude = FORCE_MIN + (1/timeDelta) * (1/(Utilities.pow(x, 2*POWER)));
        Vector direction = new Vector(1, 0);
        Vector unit = direction.scalarProduct(1/direction.magnitude());

        if (particle.getVelocity().getX() < 0) {
          particle.applyForce(unit.scalarProduct(magnitude));
        } else {
          particle.applyForce(unit.scalarProduct(magnitude).scalarProduct(COEFFICIENT_OF_RESTITUTION));
        }
      }

      if (particle.getPosition().getX() + particle.getRadius() >= this.getX()) {
        double remaining = this.getX() - particle.getPosition().getX();
        double x = remaining / (particle.getRadius());
        double magnitude = FORCE_MIN + (1/timeDelta) * (1/(Utilities.pow(x, 2*POWER)));
        Vector direction = new Vector(-1, 0);
        Vector unit = direction.scalarProduct(1/direction.magnitude());

        if (particle.getVelocity().getX() > 0) {
          particle.applyForce(unit.scalarProduct(magnitude));
        } else {
          particle.applyForce(unit.scalarProduct(magnitude).scalarProduct(COEFFICIENT_OF_RESTITUTION));
        }
      }
    });
  }

  private static void applyForces(Particle particle) {
    applyGravity(particle);
    applyFriction(particle);
    applyPressure(particle);
  }

  private static void applyGravity(Particle particle) {
    if (particle.getPosition().getY() - particle.getRadius() > 0) {
      particle.applyForce(new Vector(0, -particle.getMass() * GRAVITATIONAL_ACCELERATION));
    }
    return;
  }

  private static void applyFriction(Particle particle) {
    return;
  }

  private static void applyPressure(Particle particle) {
    return;
  }

  @Override
  public void run() {
    while(true) {
      long startTime = System.nanoTime();
      for (int i = 0; i < this.updateRatio; i++)
        this.update();
      long endTime = System.nanoTime();
      //System.out.println((endTime - startTime)/1000000 - delay + " " + (endTime - startTime)/1000000 + " " + delay);
      if ((endTime - startTime)/1000000 < delay) {
        try {
          Thread.sleep(delay - (endTime - startTime)/1000000);
        } catch (InterruptedException ignored) {
        }
      }
    }
  }
}
