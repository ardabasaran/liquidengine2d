package com.ardabasaran.liquidengine2d.ui;

import com.ardabasaran.liquidengine2d.Config;
import com.ardabasaran.liquidengine2d.engine.Particle;
import com.ardabasaran.liquidengine2d.engine.Universe;
import com.ardabasaran.liquidengine2d.engine.utilities.Utilities;
import com.ardabasaran.liquidengine2d.engine.utilities.Vector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.WindowConstants;


public class Lab extends JComponent implements ActionListener {
  private Universe universe;
  private int uiWidth;
  private int uiHeight;

  private Lab(Universe universe, int x, int y) {
    this.universe = universe;
    this.uiWidth = x;
    this.uiHeight = y;
    JFrame frame = new JFrame("liquid-engine-2d");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(new BorderLayout());

    this.setSize(new Dimension(x, y));

    frame.setSize(new Dimension(x+100, y+100));
    frame.getContentPane().add(this);

    frame.setVisible(true);
  }

  private Vector scaleAndReversePosition(Particle particle, double xScale, double yScale) {
    double particleX = particle.getPosition().getX();
    double particleY = particle.getPosition().getY();

    double scaledX = xScale * particleX;
    double scaledY = yScale * (((double)this.universe.getY()) - particleY);

    return new Vector(scaledX - xScale*particle.getRadius(), scaledY - yScale*particle.getRadius());
  }

  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
        RenderingHints.VALUE_FRACTIONALMETRICS_ON);

    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);


    g2.setPaint(Color.BLACK);
    g2.setStroke(new BasicStroke(1.0f));

    // Draw border lines
    g2.drawLine(0, this.getUiHeight(), this.getUiWidth(), this.getUiHeight());
    g2.drawLine(0, 0, this.getUiWidth(), 0);
    g2.drawLine(0, 0, 0, this.getUiHeight());
    g2.drawLine(this.getUiWidth(), this.getUiHeight(), this.getUiWidth(), 0);

    double xScale = ((double) this.getUiWidth()) / ((double)this.universe.getX());
    double yScale = ((double) this.getUiHeight()) / ((double)this.universe.getY());

    // Draw each particle
    this.universe.getParticles().forEach(particle -> {
      Vector drawPosition = scaleAndReversePosition(particle, xScale, yScale);
      g2.draw(new Ellipse2D.Double(
          drawPosition.getX(),
          drawPosition.getY(),
          particle.getRadius()*2*xScale,
          particle.getRadius()*2*yScale
      ));
    });
  }

  public static void main(String[] args) {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    Config config;

    try {
      config = mapper.readValue(new File("src/main/resources/config.yml"), Config.class);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      return;
    }

    System.out.println(config);

    Universe universe = new Universe(1.0/config.getFps()/config.getUpdatesPerFrame(),
        config.getUniverseWidth(), config.getUniverseHeight(), config.getUpdatesPerFrame(),
        1000/config.getFps(), config.getMinimumDiameter());

    IntStream.rangeClosed(1, config.getNumberOfRandomParticles())
        .forEach(i -> Utilities.addRandomParticle(universe, config.getRadiusOfRandomParticles()));

    Lab lab = new Lab(universe, config.getUiWidth(), config.getUiHeight());

    // Run the simulation in a separate thread
    Thread thread = new Thread(universe);

    // Use Java Swing Timer to update FPS times a second
    Timer timer = new Timer(1000/config.getFps(), lab);
    timer.setInitialDelay(1000/config.getFps());
    timer.start();
    thread.start();
  }

  private int getUiWidth() {
    return uiWidth;
  }

  private int getUiHeight() {
    return uiHeight;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    this.repaint();
  }
}