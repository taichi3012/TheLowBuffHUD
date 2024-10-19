package com.github.taichi3012.thelowbuffhud.util;

import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.world.World;

import com.github.taichi3012.thelowbuffhud.TheLowBuffHUDMod;

public class TheLowUtil {

  private static final String THELOW_SIDEBAR_DISPLAY_NAME = "§b===== The Low =====";
  public static boolean isPlayingTheLow() {
    World world = TheLowBuffHUDMod.mc.theWorld;
    if (world == null)
      return false;

    Scoreboard scoreboard = world.getScoreboard();
    if (scoreboard == null)
      return false;

    ScoreObjective displaySlot = scoreboard.getObjectiveInDisplaySlot(1);
    if (displaySlot == null)
      return false;

    return THELOW_SIDEBAR_DISPLAY_NAME.equals(displaySlot.getDisplayName());
  }

}
