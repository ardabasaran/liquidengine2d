package com.ardabasaran.particleengine2d.config;

public class Config {
  private int uiWidth;
  private int uiHeight;
  private int numberOfRandomParticles;
  private boolean debug;
  private UniverseConfig universeConfig;

  public Config() {}

  public Config(int uiWidth, int uiHeight, int numberOfRandomParticles, boolean debug,
      UniverseConfig universeConfig) {
    this.uiWidth = uiWidth;
    this.uiHeight = uiHeight;
    this.numberOfRandomParticles = numberOfRandomParticles;
    this.debug = debug;
    this.universeConfig = universeConfig;
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

  public int getNumberOfRandomParticles() {
    return numberOfRandomParticles;
  }

  public void setNumberOfRandomParticles(int numberOfRandomParticles) {
    this.numberOfRandomParticles = numberOfRandomParticles;
  }

  public static Config getDefaultConfig() {
    return new Config(700, 700, 50, true, UniverseConfig.getDefaultConfig());
  }

  public String toString() {
    return "Configurations:\n" +
        "\tUI width: " + uiWidth + "\n" +
        "\tUI height: " + uiHeight + "\n" +
        "\tNumber of random particles: " + numberOfRandomParticles + "\n" +
        "\tDEBUG: " + debug + "\n" +
        universeConfig.toString();
  }

  public boolean getDebug() {
    return debug;
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  public UniverseConfig getUniverseConfig() {
    return universeConfig;
  }

  public void setUniverseConfig(UniverseConfig universeConfig) {
    this.universeConfig = universeConfig;
  }
}
