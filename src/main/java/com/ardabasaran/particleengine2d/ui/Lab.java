package com.ardabasaran.particleengine2d.ui;

import com.ardabasaran.particleengine2d.config.Config;
import com.ardabasaran.particleengine2d.engine.Particle;
import com.ardabasaran.particleengine2d.engine.Universe;
import com.ardabasaran.particleengine2d.utilities.Utilities;
import com.ardabasaran.particleengine2d.utilities.Vector2D;
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
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.WindowConstants;


public class Lab extends JComponent implements ActionListener {
  private Universe universe;
  private int uiWidth;
  private int uiHeight;
  private double xScale;
  private double yScale;

  private Lab(Universe universe, int uiWidth, int uiHeight) {
    this.universe = universe;
    this.uiWidth = uiWidth;
    this.uiHeight = uiHeight;
    this.xScale = ((double) this.getUiWidth()) / ((double)this.universe.getX());
    this.yScale = ((double) this.getUiHeight()) / ((double)this.universe.getY());
    initFrame();
  }

  private void initFrame() {
    JFrame frame = new JFrame("liquid-engine-2d");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(new BorderLayout());

    this.setSize(new Dimension(uiWidth, uiHeight));

    frame.setSize(new Dimension(uiWidth+100, uiHeight+100));
    frame.getContentPane().add(this);

    frame.setVisible(true);
  }

  private Vector2D scaleAndReverseParticlePosition(Particle particle) {
    double particleX = particle.getPosition().getX();
    double particleY = particle.getPosition().getY();

    double particleScaledReverseX = xScale * particleX;
    double particleScaledReverseY = yScale * (((double)this.universe.getY()) - particleY);
    double circleBoundaryX = particleScaledReverseX - xScale*particle.getRadius();
    double circleBoundaryY = particleScaledReverseY - yScale*particle.getRadius();

    return new Vector2D(circleBoundaryX, circleBoundaryY);
  }

  private Graphics2D getGraphics2D(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
        RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

    g2.setPaint(Color.BLACK);
    g2.setStroke(new BasicStroke(1.0f));
    return g2;
  }

  private void drawBorderLines(Graphics2D g2) {
    g2.drawLine(0, this.getUiHeight(), this.getUiWidth(), this.getUiHeight());
    g2.drawLine(0, 0, this.getUiWidth(), 0);
    g2.drawLine(0, 0, 0, this.getUiHeight());
    g2.drawLine(this.getUiWidth(), this.getUiHeight(), this.getUiWidth(), 0);
  }

  private void drawParticles(Graphics2D g2) {
    this.universe.getParticles().forEach(particle -> {
      Vector2D drawPosition = scaleAndReverseParticlePosition(particle);
      g2.draw(new Ellipse2D.Double(
          drawPosition.getX(),
          drawPosition.getY(),
          particle.getRadius()*2*xScale,
          particle.getRadius()*2*yScale
      ));
    });
  }

  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2 = getGraphics2D(g);
    drawBorderLines(g2);
    drawParticles(g2);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    this.repaint();
  }

  private static Config readConfig() throws IOException  {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    return mapper.readValue(new File("src/main/resources/config.yml"), Config.class);
  }

  private static void addRandomParticles(Universe universe, int numParticles, double particleRadius) {
    for (int i = 0; i < numParticles; i++) {
      Utilities.addRandomParticle(universe, particleRadius);
    }
  }

  private static void initThreads(Universe universe, Lab lab, int fps) {
    // Run the simulation in a separate thread
    Thread thread = new Thread(universe);
    thread.start();
    // Use Java Swing Timer to update FPS times a second
    Timer timer = new Timer(1000/fps, lab);
    timer.setInitialDelay(1000/fps);
    timer.start();
  }

  public static void main(String[] args) {
    Config config;
    try {
      config = readConfig();
    } catch (IOException e) {
      System.err.println("Error reading configuration file. Using default configurations.");
      System.err.println(e.getMessage());
      config = Config.getDefaultConfig();
    }

    if (config.getDebug()) {
      System.out.println(config);
    }

    Universe universe = new Universe(config.getUniverseConfig());
    addRandomParticles(universe, 2*config.getNumberOfRandomParticles()/3,
        config.getUniverseConfig().getMinimumDiameter()/2);

    for (int i = 0; i < config.getNumberOfRandomParticles()/3; i++) {
      addRandomParticles(universe, 1,
          config.getUniverseConfig().getMinimumDiameter()/2 + i/10);
    }

    Lab lab = new Lab(universe, config.getUiWidth(), config.getUiHeight());

    initThreads(universe, lab, config.getUniverseConfig().getTicksPerSecond());
  }

  private int getUiWidth() {
    return uiWidth;
  }

  private int getUiHeight() {
    return uiHeight;
  }
}