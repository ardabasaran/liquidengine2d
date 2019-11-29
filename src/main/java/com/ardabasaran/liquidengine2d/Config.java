package com.ardabasaran.liquidengine2d;

public class Config {
  private int universeWidth;
  private int universeHeight;
  private int uiWidth;
  private int uiHeight;
  private double minimumDiameter;
  private int fps;
  private int updatesPerFrame;
  private int numberOfRandomParticles;
  private double radiusOfRandomParticles;
  private double gravitationalAcceleration;
  private double coefficientOfRestitution;
  private boolean debug;

  public Config() {}

  public Config(int universeWidth, int universeHeight, int uiWidth, int uiHeight,
      double minimumDiameter, int fps, int updatesPerFrame, int numberOfRandomParticles,
      double radiusOfRandomParticles, double gravitationalAcceleration, double coefficientOfRestitution,
      boolean debug) {
    this.universeWidth = universeWidth;
    this.universeHeight = universeHeight;
    this.uiWidth = uiWidth;
    this.uiHeight = uiHeight;
    this.minimumDiameter = minimumDiameter;
    this.fps = fps;
    this.updatesPerFrame = updatesPerFrame;
    this.numberOfRandomParticles = numberOfRandomParticles;
    this.radiusOfRandomParticles = radiusOfRandomParticles;
    this.gravitationalAcceleration = gravitationalAcceleration;
    this.coefficientOfRestitution = coefficientOfRestitution;
    this.debug = debug;
  }

  public int getUniverseWidth() {
    return universeWidth;
  }

  public void setUniverseWidth(int universeWidth) {
    this.universeWidth = universeWidth;
  }

  public int getUniverseHeight() {
    return universeHeight;
  }

  public void setUniverseHeight(int universeHeight) {
    this.universeHeight = universeHeight;
  }

  public int getUiWidth() {
    return uiWidth;
  }

  public void setUiWidth(int uiWidth) {
    this.uiWidth = uiWidth;
  }

  public int getUiHeight() {
    return uiHeight;
  }

  public void setUiHeight(int uiHeight) {
    this.uiHeight = uiHeight;
  }

  public double getMinimumDiameter() {
    return minimumDiameter;
  }

  public void setMinimumDiameter(double minimumDiameter) {
    this.minimumDiameter = minimumDiameter;
  }

  public int getFps() {
    return fps;
  }

  public void setFps(int fps) {
    this.fps = fps;
  }

  public int getUpdatesPerFrame() {
    return updatesPerFrame;
  }

  public void setUpdatesPerFrame(int updatesPerFrame) {
    this.updatesPerFrame = updatesPerFrame;
  }

  public int getNumberOfRandomParticles() {
    return numberOfRandomParticles;
  }

  public void setNumberOfRandomParticles(int numberOfRandomParticles) {
    this.numberOfRandomParticles = numberOfRandomParticles;
  }

  public double getRadiusOfRandomParticles() {
    return radiusOfRandomParticles;
  }

  public void setRadiusOfRandomParticles(double radiusOfRandomParticles) {
    this.radiusOfRandomParticles = radiusOfRandomParticles;
  }

  public static Config getDefaultConfig() {
    return new Config(1000, 1000, 500, 500,
    20, 60, 200, 10,
    10, 9806.65, 0.9, true);
  }

  public String toString() {
    return "Configurations:\n" +
        "\tUniverse width: " + universeWidth + "\n" +
        "\tUniverse height: " + universeHeight + "\n" +
        "\tGravitational acceleration: " + String.format("%.3f", gravitationalAcceleration) + "\n" +
        "\tCoefficient of restitution: " + String.format("%.3f", coefficientOfRestitution) + "\n" +
        "\tUI width: " + uiWidth + "\n" +
        "\tUI height: " + uiHeight + "\n" +
        "\tMinimum diameter: " + String.format("%.3f", minimumDiameter) + "\n" +
        "\tFPS: " + fps + "\n" +
        "\tUpdates per frame: " + updatesPerFrame + "\n" +
        "\tNumber of random particles: " + numberOfRandomParticles + "\n" +
        "\tRadius of random particles: " + String.format("%.3f", radiusOfRandomParticles) + "\n" +
        "\tDEBUG: " + debug + "\n";
  }

  public double getGravitationalAcceleration() {
    return gravitationalAcceleration;
  }

  public void setGravitationalAcceleration(double gravitationalAcceleration) {
    this.gravitationalAcceleration = gravitationalAcceleration;
  }

  public double getCoefficientOfRestitution() {
    return coefficientOfRestitution;
  }

  public void setCoefficientOfRestitution(double coefficientOfRestitution) {
    this.coefficientOfRestitution = coefficientOfRestitution;
  }

  public boolean getDebug() {
    return debug;
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }
}
