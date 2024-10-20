package com.github.taichi3012.thelowbuffhud.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.math.DoubleMath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;

import com.github.taichi3012.thelowbuffhud.config.ConfigData;
import com.github.taichi3012.thelowbuffhud.thelowapi.CacheProvider;
import com.github.taichi3012.thelowbuffhud.thelowapi.cache.BuffCache;
import com.github.taichi3012.thelowbuffhud.util.MinecraftUtil;

public class GuiTheLowBuffStatus extends Gui {

  private final Minecraft mc;

  private static final int VERTICAL_MARGIN = 3;

  public GuiTheLowBuffStatus(Minecraft mc) {
    this.mc = mc;
  }

  public void render() {
    BuffCache cache = CacheProvider.getBuffCache();
    if (cache.empty)
      return;

    ConfigData config = ConfigData.getInstance();
    if (config.style.startsWith("SEPARATE_"))
      renderSeparateStyle(config, cache.effects);
    else
      renderNotSeparateStyle(config, cache.effects);
  }

  private void renderSeparateStyle(ConfigData config, Map<String, BuffCache.Value> effects) {
    FontRenderer fontRenderer = this.mc.fontRendererObj;
    int titlePanelWidth = 0;
    int durationPanelWidth = 0;
    List<Line> lines = new ArrayList<>();
    for (Map.Entry<String, BuffCache.Value> entry : effects.entrySet()) {
      BuffCache.Value value = entry.getValue();
      long duration = value.getDuration();
      if (duration < 0)
        continue;

      String name = entry.getKey();
      Line line = new Line(name + formatBuffLevel(name, value.level), formatDuration(duration), fontRenderer);
      lines.add(line);
      titlePanelWidth = Math.max(line.titleWidth, titlePanelWidth);
      durationPanelWidth = Math.max(line.durationWidth, durationPanelWidth);
    }

    if (lines.isEmpty())
      return;

    final int spaceWidth = fontRenderer.getStringWidth("  ");
    int width = titlePanelWidth + spaceWidth + durationPanelWidth;
    GlStateManager.pushMatrix();
    translate(config, getHeight(lines.size(), fontRenderer.FONT_HEIGHT), width);

    String[] side = config.style.substring(9, 11).split("");
    boolean isTitleLeftSide = side[0].equals("L");
    boolean isDurationLeftSide = side[1].equals("L");

    for (Line l : lines) {
      if (isTitleLeftSide)
        fontRenderer.drawStringWithShadow(l.titleText, 0f, 0f, 0xffffffff);
      else
        fontRenderer.drawStringWithShadow(l.titleText, titlePanelWidth - l.titleWidth, 0f, 0xffffffff);

      if (isDurationLeftSide)
        fontRenderer.drawStringWithShadow(l.durationText, titlePanelWidth + spaceWidth, 0f, 0xffffffff);
      else
        fontRenderer.drawStringWithShadow(l.durationText, width - l.durationWidth, 0f, 0xffffffff);

      translateNextLine(fontRenderer.FONT_HEIGHT);
    }

    GlStateManager.popMatrix();
  }

  private void renderNotSeparateStyle(ConfigData config, Map<String, BuffCache.Value> effects) {
    FontRenderer fontRenderer = this.mc.fontRendererObj;
    final int spaceWidth = fontRenderer.getStringWidth("  ");
    int width = 0;
    List<Line> lines = new ArrayList<>();
    for (Map.Entry<String, BuffCache.Value> entry : effects.entrySet()) {
      BuffCache.Value value = entry.getValue();
      long duration = value.getDuration();
      if (duration < 0)
        continue;

      String name = entry.getKey();
      Line line = new Line(name + formatBuffLevel(name, value.level), formatDuration(duration), fontRenderer);
      lines.add(line);
      width = Math.max(line.titleWidth + spaceWidth + line.durationWidth, width);
    }

    if (lines.isEmpty())
      return;

    GlStateManager.pushMatrix();
    translate(config, getHeight(lines.size(), fontRenderer.FONT_HEIGHT), width);

    switch (config.style) {
      case "LEFT":
        for (Line l : lines) {
          GlStateManager.pushMatrix();
          GlStateManager.translate(fontRenderer.drawStringWithShadow(l.titleText, 0f, 0f, 0xffffffff) + spaceWidth, 0f, 0f);
          fontRenderer.drawStringWithShadow(l.durationText, 0f, 0f, 0xffffffff);
          GlStateManager.popMatrix();
          translateNextLine(fontRenderer.FONT_HEIGHT);
        }
        break;
      case "MIDDLE":
        for (Line l : lines) {
          int sideMargin = (width - (l.titleWidth + spaceWidth + l.durationWidth)) / 2;
          GlStateManager.pushMatrix();
          GlStateManager.translate(sideMargin, 0f, 0f);
          GlStateManager.translate(fontRenderer.drawStringWithShadow(l.titleText, 0f, 0f, 0xffffffff) + spaceWidth, 0f, 0f);
          fontRenderer.drawStringWithShadow(l.durationText, 0f, 0f, 0xffffffff);
          GlStateManager.popMatrix();
          translateNextLine(fontRenderer.FONT_HEIGHT);
        }
        break;
      case"RIGHT":
        for (Line l : lines) {
          GlStateManager.pushMatrix();
          GlStateManager.translate(width - (l.titleWidth + spaceWidth + l.durationWidth), 0f, 0f);
          GlStateManager.translate(fontRenderer.drawStringWithShadow(l.titleText, 0f, 0f, 0xffffffff) + spaceWidth, 0f, 0f);
          fontRenderer.drawStringWithShadow(l.durationText, 0f, 0f, 0xffffffff);
          GlStateManager.popMatrix();
          translateNextLine(fontRenderer.FONT_HEIGHT);
        }
        break;
    }

    GlStateManager.popMatrix();
  }

  private static int getHeight(int line, int fontHeight) {
    return line * (fontHeight + VERTICAL_MARGIN) - VERTICAL_MARGIN;
  }

  private static String formatBuffLevel(String name, double level) {
    switch (name) {
      case "取得経験値増加":
      case "ExpBlock経験値増加":
        return String.format("+%.0f%%", level);
      default:
        return DoubleMath.isMathematicalInteger(level) ? MinecraftUtil.formatRomanNumerals(Math.round(level))
          : Double.toString(level);
    }
  }

  private static final String[] DURATION_FORMAT = new String[]{ "d'd'HH'h'mm'm'ss's'", "H'h'mm'm'ss's'", "m'm'ss's'", "s's'" };
  private static String formatDuration(long duration) {
    String format =
      duration >= DateUtils.MILLIS_PER_DAY ? DURATION_FORMAT[0] :
      duration >= DateUtils.MILLIS_PER_HOUR ? DURATION_FORMAT[1] :
      duration >= DateUtils.MILLIS_PER_MINUTE ? DURATION_FORMAT[2] :
      DURATION_FORMAT[3];

    return DurationFormatUtils.formatDuration(duration, format);
  }

  private void translate(ConfigData config, int height, int width) {
    ScaledResolution resolution = new ScaledResolution(this.mc);
    switch (config.verticalPosition) {
      case "MIDDLE":
        GlStateManager.translate(0f, resolution.getScaledHeight() / 2f, 0f);
        break;
      case "BOTTOM":
        GlStateManager.translate(0f, resolution.getScaledHeight(), 0f);
        break;
    }

    switch (config.horizontalPosition) {
      case "MIDDLE":
        GlStateManager.translate(resolution.getScaledWidth() / 2f, 0f, 0f);
        break;
      case "RIGHT":
        GlStateManager.translate(resolution.getScaledWidth(), 0f, 0f);
        break;
    }

    switch (config.verticalReferencePoint) {
      case "MIDDLE":
        GlStateManager.translate(0f, -height / 2f, 0f);
        break;
      case "BOTTOM":
        GlStateManager.translate(0f, -height, 0f);
        break;
    }

    switch (config.horizontalReferencePoint) {
      case "MIDDLE":
        GlStateManager.translate(-width / 2f, 0f, 0f);
        break;
      case "RIGHT":
        GlStateManager.translate(-width, 0f, 0f);
        break;
    }

    GlStateManager.translate(config.horizontalOffset, config.verticalOffset, 0f);
  }

  private static void translateNextLine(int fontHeight) {
    GlStateManager.translate(0f, fontHeight + VERTICAL_MARGIN, 0f);
  }

  private static class Line {
    private final String titleText;
    private final String durationText;
    private final int titleWidth;
    private final int durationWidth;


    private Line(String titleText, String durationText, FontRenderer fontRenderer) {
      this.titleText = titleText;
      this.durationText = durationText;
      this.titleWidth = fontRenderer.getStringWidth(titleText);
      this.durationWidth = fontRenderer.getStringWidth(durationText);
    }
  }

}
