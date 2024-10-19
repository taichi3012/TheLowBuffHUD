package com.github.taichi3012.thelowbuffhud.config;

public final class ConfigData {

  public final String verticalPosition;

  public final String horizontalPosition;

  public final String verticalReferencePoint;

  public final String horizontalReferencePoint;

  public final int verticalOffset;

  public final int horizontalOffset;

  private static ConfigData instance = new ConfigData(
    "BOTTOM", "RIGHT",
    "BOTTOM", "RIGHT",
    -5, -5
  );

  private ConfigData(String verticalPosition, String horizontalPosition, String verticalReferencePoint, String horizontalReferencePoint, int verticalOffset, int horizontalOffset) {
    this.verticalPosition = verticalPosition;
    this.horizontalPosition = horizontalPosition;
    this.verticalReferencePoint = verticalReferencePoint;
    this.horizontalReferencePoint = horizontalReferencePoint;
    this.verticalOffset = verticalOffset;
    this.horizontalOffset = horizontalOffset;
  }

  static void updateInstance(String verticalPosition, String horizontalPosition, String verticalReferencePoint, String horizontalReferencePoint, int verticalOffset, int horizontalOffset) {
    instance = new ConfigData(
      verticalPosition, horizontalPosition,
      verticalReferencePoint, horizontalReferencePoint,
      verticalOffset, horizontalOffset
    );
  }

  public static ConfigData getInstance() {
    return instance;
  }
}
