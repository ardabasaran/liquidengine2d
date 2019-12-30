package com.ardabasaran.particleengine2d.config;

public class UniverseConfig {
  private int width;
  private int height;
  private int ticksPerSecond;
  private double minimumDiameter;
  private int updatesPerTick;
  private double gravitationalAcceleration;
  private double coefficientOfRestitution;
  private boolean debug;
  private String detectorType;
  private String resolverType;
  private String borderHandlerType;
  private static final String DEFAULT_DETECTOR_TYPE = "sorted";
  private static final String DEFAULT_RESOLVER_TYPE = "exponential";
  private static final String DEFAULT_BORDER_HANDLER_TYPE = "default";

  public UniverseConfig() {}

  public UniverseConfig(int width, int height, int ticksPerSecond, double minimumDiameter,
      int updatesPerTick, double gravitationalAcceleration, double coefficientOfRestitution,
      boolean debug, String detectorType, String resolverType, String borderHandlerType) {
    this.width = width;
    this.height = height;
    this.ticksPerSecond = ticksPerSecond;
    this.minimumDiameter = minimumDiameter;
    this.updatesPerTick = updatesPerTick;
    this.gravitationalAcceleration = gravitationalAcceleration;
    this.coefficientOfRestitution = coefficientOfRestitution;
    this.debug = debug;
    this.detectorType = detectorType;
    this.resolverType = resolverType;
    this.borderHandlerType = borderHandlerType;
  }

  public String getBorderHandlerType() {
    return borderHandlerType;
  }

  public void setBorderHandlerType(String borderHandlerType) {
    this.borderHandlerType = borderHandlerType;
  }

  public String getDetectorType() {
    return detectorType;
  }

  public void setDetectorType(String detectorType) {
    this.detectorType = detectorType;
  }

  public String getResolverType() {
    return resolverType;
  }

  public void setResolverType(String resolverType) {
    this.resolverType = resolverType;
  }

  public int getTicksPerSecond() {
    return ticksPerSecond;
  }

  public void setTicksPerSecond(int ticksPerSecond) {
    this.ticksPerSecond = ticksPerSecond;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public double getMinimumDiameter() {
    return minimumDiameter;
  }

  public void setMinimumDiameter(double minimumDiameter) {
    this.minimumDiameter = minimumDiameter;
  }

  public int getUpdatesPerTick() {
    return updatesPerTick;
  }

  public void setUpdatesPerTick(int updatesPerTick) {
    this.updatesPerTick = updatesPerTick;
  }

  public static UniverseConfig getDefaultConfig() {
    return new UniverseConfig(2000, 2000, 60, 20, 200,
        9806.65, 0.9, true, DEFAULT_DETECTOR_TYPE,
        DEFAULT_RESOLVER_TYPE, DEFAULT_BORDER_HANDLER_TYPE);
  }

  public String toString() {
    return "Universe Config:\n" +
        "\t\tWidth: " + width + "\n" +
        "\t\tHeight: " + height + "\n" +
        "\t\tGravitational acceleration: " + String.format("%.3f", gravitationalAcceleration) + "\n" +
        "\t\tCoefficient of restitution: " + String.format("%.3f", coefficientOfRestitution) + "\n" +
        "\t\tMinimum diameter: " + String.format("%.3f", minimumDiameter) + "\n" +
        "\t\tUpdates per frame: " + updatesPerTick + "\n" +
        "\t\tDEBUG: " + debug + "\n" +
        "\t\tDetector type: " + detectorType + "\n" +
        "\t\tResolver type: " + resolverType + "\n" +
        "\t\tBorder handler type: " + borderHandlerType + "\n";
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
