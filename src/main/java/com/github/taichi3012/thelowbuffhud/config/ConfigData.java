package com.github.taichi3012.thelowbuffhud.config;

public final class ConfigData {

  public final String verticalPosition;

  public final String horizontalPosition;

  public final String verticalReferencePoint;

  public final String horizontalReferencePoint;

  public final int verticalOffset;

  public final int horizontalOffset;

  public final String style;

  private static ConfigData instance = new ConfigData(
    "BOTTOM", "RIGHT",
    "BOTTOM", "RIGHT",
    -5, -5,
    "SEPARATE_RL"
  );

  private ConfigData(String verticalPosition, String horizontalPosition, String verticalReferencePoint, String horizontalReferencePoint, int verticalOffset, int horizontalOffset, String style) {
    this.verticalPosition = verticalPosition;
    this.horizontalPosition = horizontalPosition;
    this.verticalReferencePoint = verticalReferencePoint;
    this.horizontalReferencePoint = horizontalReferencePoint;
    this.verticalOffset = verticalOffset;
    this.horizontalOffset = horizontalOffset;
    this.style = style;
  }

  static void updateInstance(String verticalPosition, String horizontalPosition, String verticalReferencePoint, String horizontalReferencePoint, int verticalOffset, int horizontalOffset, String style) {
    instance = new ConfigData(
      verticalPosition, horizontalPosition,
      verticalReferencePoint, horizontalReferencePoint,
      verticalOffset, horizontalOffset,
      style
    );
  }

  public static ConfigData getInstance() {
    return instance;
  }
}
