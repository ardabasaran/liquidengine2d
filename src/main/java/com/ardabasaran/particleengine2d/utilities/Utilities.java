package com.ardabasaran.particleengine2d.utilities;

import com.ardabasaran.particleengine2d.engine.Particle;
import com.ardabasaran.particleengine2d.engine.Universe;
import java.util.List;
import java.util.Random;

public class Utilities {
  private static final double EPSILON = 0.00000000001;

  public static boolean doubleEquals(double a, double b) {
    return (a - EPSILON < b) && (b < a + EPSILON);
  }

  public static double pow(double num, int pow) {
    double res = 1;
    while (pow > 0) {
      if (pow % 2 != 0)
        res *= num;
      pow >>= 1;
      num *= num;
    }
    return res;
  }

  public static void addRandomParticle(Universe universe, double radius) {
    Random gen = new Random();
    Particle particle;
    do {
      double x = gen.nextDouble()*universe.getX();
      double y = gen.nextDouble()*universe.getY();
      particle = new Particle(radius, 5.0, x, y);
      boolean cond = true;
      for(Particle p : universe.getParticles()) {
        if(p.isColliding(particle)) {
          cond = false;
        }
      }
      if (x - radius <= 0 || x + radius >= universe.getX() ||
          y - radius <= 0 || y + radius >= universe.getY()) {
        cond = false;
      }
      if(cond) {
        break;
      }
    } while(true);

    particle.setVelocity(new Vector2D(0, 0));
    universe.addParticle(particle);
  }

  public static boolean sameSign(double a, double b) {
    return ((a > 0) ? 1 : -1) == ((b > 0) ? 1 : -1);
  }

  public static int leftmostParticleIndex(List<Particle> particles, int l, int r, double val) {
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

  public static int rightmostParticleIndex(List<Particle> particles, int l, int r, double val) {
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

  public static String getTickUpdateInfo(long startTime, long endTime, long delay) {
    long milliSecondsPassed = (endTime - startTime) / 1000000;
    long toSleep = delay - milliSecondsPassed;
    return "Delay: " + delay + "\t\tMilliseconds passed: " + milliSecondsPassed + "\t\tTo sleep: " + toSleep;
  }
}