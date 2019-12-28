package com.ardabasaran.particleengine2d.engine.collision.detector;

import com.ardabasaran.particleengine2d.engine.Particle;
import com.ardabasaran.particleengine2d.engine.ParticlePair;
import com.ardabasaran.particleengine2d.utilities.Utilities;
import java.util.ArrayList;
import java.util.List;

public class SortedCollisionDetector implements CollisionDetector {
  private int numRanges;
  private double minDiameter;

  public SortedCollisionDetector(int numRanges, double minDiameter) {
    this.numRanges = numRanges;
    this.minDiameter = minDiameter;
  }

  private int[][] calculateIntervals(List<Particle> sortedParticles) {
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

  private List<ParticlePair> resolveHorizontalCollisions(List<Particle> sortedParticles, Particle particle, int[][] intervals,
      int currentVertical, int particleIndex) {
    List<ParticlePair> collisions = new ArrayList<>();
    double xRangeHigh = particle.getPosition().getX() + 2*particle.getRadius();
    int j = particleIndex + 1;

    while (j < sortedParticles.size() && j <= intervals[currentVertical][1] &&
        sortedParticles.get(j).getPosition().getX() <= xRangeHigh) {
      if (particle.isColliding(sortedParticles.get(j)))
        collisions.add(new ParticlePair(particle, sortedParticles.get(j)));
      j += 1;
    }

    double xRangeLow = particle.getPosition().getX() - 2*particle.getRadius();
    j = particleIndex - 1;

    while (j >= 0 && j >= intervals[currentVertical][0] &&
        sortedParticles.get(j).getPosition().getX() >= xRangeLow) {
      if (particle.isColliding(sortedParticles.get(j)))
        collisions.add(new ParticlePair(particle, sortedParticles.get(j)));
      j -= 1;
    }

    return collisions;
  }

  private List<ParticlePair> resolveVerticalCollisions(List<Particle> sortedParticles, Particle particle, int[][] intervals,
      int currentVertical, int numVerticals) {
    List<ParticlePair> collisions = new ArrayList<>();
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
            collisions.add(new ParticlePair(particle, sortedParticles.get(k)));
        }
      }
      j = j + 1;
    }


    j = 1;
    while (j <= numVerticals) {
      if (currentVertical - j >= 0) {
        if (intervals[currentVertical-j][0] == -1 && intervals[currentVertical-j][1] == -1) {
          j = j - 1;
          continue;
        }
        int left = Utilities.leftmostParticleIndex(
            sortedParticles,
            intervals[currentVertical-j][0],
            intervals[currentVertical-j][1],
            xRangeLow
        );
        int right = Utilities.rightmostParticleIndex(
            sortedParticles,
            intervals[currentVertical-j][0],
            intervals[currentVertical-j][1],
            xRangeHigh
        );

        for (int k = left; k <= right; k++) {
          if (particle != sortedParticles.get(k) && particle.isColliding(sortedParticles.get(k)))
            collisions.add(new ParticlePair(particle, sortedParticles.get(k)));
        }
      }
      j = j + 1;
    }
    return collisions;
  }

  private void collisionSortParticles(List<Particle> sortedParticles) {
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

  @Override
  public List<ParticlePair> detectCollisions(List<Particle> sortedParticles) {
    List<ParticlePair> collisions = new ArrayList<>();
    this.collisionSortParticles(sortedParticles);
    int[][] intervals = calculateIntervals(sortedParticles);

    for (int i = 0; i < sortedParticles.size(); i++ ) {
      Particle particle = sortedParticles.get(i);
      int currentVertical = (int) Math.floor(particle.getPosition().getY() / minDiameter);

      assert intervals[currentVertical][0] != -1;
      assert intervals[currentVertical][1] != -1;
      // Horizontal
      collisions.addAll(resolveHorizontalCollisions(sortedParticles, particle, intervals, currentVertical, i));

      // Vertical
      int numVerticals = (int) Math.ceil(2*particle.getRadius() / minDiameter);
//      if (particle.getPosition().getY() < 0 || particle.getPosition().getY() > this.y) {
//        continue;
//      }
      collisions.addAll(resolveVerticalCollisions(sortedParticles, particle, intervals, currentVertical, numVerticals));
    }

    return collisions;
  }
}
