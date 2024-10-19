package com.github.taichi3012.thelowbuffhud.gui;

import java.util.ArrayList;
import java.util.List;

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

  public GuiTheLowBuffStatus(Minecraft mc) {
    this.mc = mc;
  }

  public void render() {
    BuffCache cache = CacheProvider.getBuffCache();
    if (cache.empty)
      return;

    List<String> lines = new ArrayList<>();
    cache.effects.forEach((name, v) -> {
      long duration = v.getDuration();
      if (duration >= 0)
        lines.add(name + " " + formatBuffLevel(name, v.level) + " " + formatDuration(duration));
    });

    if (lines.isEmpty())
      return;

    GlStateManager.pushMatrix();

    ConfigData config = ConfigData.getInstance();
    ScaledResolution resolution = new ScaledResolution(this.mc);
    FontRenderer fontRenderer = this.mc.fontRendererObj;

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

    int padding = 3;
    int width = lines.stream().mapToInt(fontRenderer::getStringWidth).max().orElse(0);
    int height = lines.size() * (fontRenderer.FONT_HEIGHT + padding) - padding;

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

    for (String l : lines) {
      fontRenderer.drawStringWithShadow(l, 0f, 0f, 0xffffffff);
      GlStateManager.translate(0f, fontRenderer.FONT_HEIGHT + padding, 0f);
    }
    GlStateManager.popMatrix();
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

}
