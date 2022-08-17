package com.ardabasaran.particleengine2d.engine.collision.resolver;

import com.ardabasaran.particleengine2d.engine.Particle;
import com.ardabasaran.particleengine2d.utilities.Vector2D;

public class ElasticCollisionResolver implements CollisionResolver {
    private double coefficient_of_restitution;

    public ElasticCollisionResolver(double coefficient_of_restitution) {
        this.coefficient_of_restitution = coefficient_of_restitution;
    }

    @Override
    public void resolveCollision(Particle firstParticle, Particle secondParticle) {
        Vector2D normal = firstParticle.getPosition().minus(secondParticle.getPosition());
        normal = normal.scalarProduct(1 / normal.magnitude());
        Vector2D orthogonal = new Vector2D(-normal.getY(), normal.getX());

        Vector2D firstParticleVelocity = firstParticle.getVelocity();
        Vector2D secondParticleVelocity = secondParticle.getVelocity();

        if (firstParticle.isMovingTowards(secondParticle)) {
            Vector2D firstParticleVelocityNormal =
                    normal.scalarProduct(normal.dot(firstParticleVelocity));
            Vector2D secondParticleVelocityNormal =
                    normal.scalarProduct(normal.dot(secondParticleVelocity));

            Vector2D firstParticleVelocityOrthogonal =
                    orthogonal.scalarProduct(orthogonal.dot(firstParticleVelocity));
            Vector2D secondParticleVelocityOrthogonal =
                    orthogonal.scalarProduct(orthogonal.dot(secondParticleVelocity));

            double totalMass = firstParticle.getMass() + secondParticle.getMass();
            Vector2D firstParticleVelocityNormalNew =
                    (firstParticleVelocityNormal
                                    .scalarProduct(firstParticle.getMass() - secondParticle.getMass())
                                    .plus(secondParticleVelocityNormal.scalarProduct(2 * secondParticle.getMass())))
                            .scalarProduct(1 / totalMass);

            Vector2D secondParticleVelocityNormalNew =
                    (secondParticleVelocityNormal
                                    .scalarProduct(secondParticle.getMass() - firstParticle.getMass())
                                    .plus(firstParticleVelocityNormal.scalarProduct(2 * firstParticle.getMass())))
                            .scalarProduct(1 / totalMass);
            firstParticleVelocityNormalNew.multiply(coefficient_of_restitution);
            secondParticleVelocityNormalNew.multiply(coefficient_of_restitution);
            firstParticle.setVelocity(
                    firstParticleVelocityOrthogonal.plus(firstParticleVelocityNormalNew));
            secondParticle.setVelocity(
                    secondParticleVelocityOrthogonal.plus(secondParticleVelocityNormalNew));
        }
    }
}
