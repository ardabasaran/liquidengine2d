package com.ardabasaran.particleengine2d.ui;

import com.ardabasaran.particleengine2d.engine.Particle;
import com.ardabasaran.particleengine2d.engine.Universe;
import com.ardabasaran.particleengine2d.utilities.Utilities;
import com.ardabasaran.particleengine2d.utilities.Vector2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseListener, MouseMotionListener {
  private double xScale;
  private double yScale;
  private double labY;
  private Particle currParticle;
  private Universe universe;

  MouseHandler(Universe universe, double xScale, double yScale, double labY) {
    this.universe = universe;
    this.xScale = xScale;
    this.yScale = yScale;
    this.labY = labY;
  }

  @Override
  public void mousePressed(MouseEvent e) {
    Point point = e.getPoint();
    Vector2D reversed = getReversed(point);
    currParticle = Utilities.findParticle(reversed, universe.getParticles());
  }

  private Vector2D getReversed(Point point) {
    double currX = point.getX();
    double currY = point.getY();
    double reversedX = currX / xScale;
    double reversedY = (labY - currY) / yScale;
    return new Vector2D(reversedX, reversedY);
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    Vector2D reversed = getReversed(e.getPoint());
    if (currParticle != null) {
      Vector2D f = new Vector2D(reversed.getX() - currParticle.getPosition().getX(),
          reversed.getY() - currParticle.getPosition().getY());
      f.multiply(10);
      currParticle.setVelocity(f);
    }
  }

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}

  @Override
  public void mouseMoved(MouseEvent e) {}

  @Override
  public void mouseClicked(MouseEvent e) {}

  @Override
  public void mouseReleased(MouseEvent e) {}
}
