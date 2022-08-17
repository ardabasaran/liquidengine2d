package com.ardabasaran.particleengine2d.utilities;

import com.ardabasaran.particleengine2d.engine.Particle;

public class ExponentialUtil {
    private static final int POWER = 20;
    private static final double FORCE_MIN = 0;

    private static double getMagnitude(double x, double timeDelta) {
        return FORCE_MIN + (1 / timeDelta) * (1 / (Utilities.pow(x, POWER)));
    }

    private static void calculateForcesToApplyToParticlesNotMovingTowards(
            Vector2D unit1, Vector2D unit2, double magnitude, double coefficient_of_restitution) {
        unit1.multiply(magnitude);
        unit1.multiply(coefficient_of_restitution);
        unit2.multiply(-1);
        unit2.multiply(magnitude);
        unit2.multiply(coefficient_of_restitution);
    }

    private static void calculateForcesToApplyToParticlesMovingTowards(
            Vector2D unit1, Vector2D unit2, double magnitude) {
        unit1.multiply(magnitude);
        unit2.multiply(-1);
        unit2.multiply(magnitude);
    }

    private static void calculateForcesToApplyToParticles(
            Vector2D unit1,
            Vector2D unit2,
            Particle firstParticle,
            Particle secondParticle,
            double magnitude,
            double coefficient_of_restitution,
            boolean reversed) {
        if (reversed) {
            if (firstParticle.isMovingTowards(secondParticle)) {
                calculateForcesToApplyToParticlesNotMovingTowards(
                        unit1, unit2, magnitude, coefficient_of_restitution);
            } else {
                calculateForcesToApplyToParticlesMovingTowards(unit1, unit2, magnitude);
            }
            unit1.multiply(-1);
            unit2.multiply(-1);
        } else {
            if (firstParticle.isMovingTowards(secondParticle)) {
                calculateForcesToApplyToParticlesMovingTowards(unit1, unit2, magnitude);
            } else {
                calculateForcesToApplyToParticlesNotMovingTowards(
                        unit1, unit2, magnitude, coefficient_of_restitution);
            }
        }
    }

    private static void applyForcesToParticles(
            Particle firstParticle,
            Particle secondParticle,
            double magnitude,
            double coefficient_of_restitution,
            boolean reversed) {
        Vector2D direction = firstParticle.getPosition().minus(secondParticle.getPosition());
        Vector2D unit1 = direction.scalarProduct(1 / direction.magnitude());
        Vector2D unit2 = new Vector2D(unit1.getX(), unit1.getY());
        calculateForcesToApplyToParticles(
                unit1,
                unit2,
                firstParticle,
                secondParticle,
                magnitude,
                coefficient_of_restitution,
                reversed);
        firstParticle.applyForce(unit1);
        secondParticle.applyForce(unit2);
    }

    public static void applyCollidingForces(
            Particle firstParticle,
            Particle secondParticle,
            double coefficient_of_restitution,
            double timeDelta) {
        double remaining = firstParticle.getPosition().distanceSquared(secondParticle.getPosition());
        double ratioOfRemaining =
                remaining / Utilities.pow(firstParticle.getRadius() + secondParticle.getRadius(), 2);
        double magnitude = getMagnitude(ratioOfRemaining, timeDelta);
        applyForcesToParticles(
                firstParticle, secondParticle, magnitude, coefficient_of_restitution, false);
    }

    public static void applyPullingForces(
            Particle firstParticle,
            Particle secondParticle,
            double coefficient_of_restitution,
            double timeDelta) {
        double currentDistance =
                firstParticle.getPosition().distanceSquared(secondParticle.getPosition());
        double ratioOfDistance =
                Utilities.pow(firstParticle.getRadius() + secondParticle.getRadius(), 2) / currentDistance;
        double magnitude = getMagnitude(ratioOfDistance, timeDelta);
        applyForcesToParticles(
                firstParticle, secondParticle, magnitude, coefficient_of_restitution, true);
    }
}
