package com.github.taichi3012.thelowbuffhud.util;

import net.minecraft.client.resources.I18n;

public class MinecraftUtil {

  public static String formatRomanNumerals(long num) {
    return (1 <= num && num <= 10) ? I18n.format("enchantment.level." + num) : Long.toString(num);
  }

}
