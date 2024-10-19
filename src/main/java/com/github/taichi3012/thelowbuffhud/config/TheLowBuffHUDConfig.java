package com.github.taichi3012.thelowbuffhud.config;

import java.io.File;
import java.util.List;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;

public class TheLowBuffHUDConfig {

  public static final String CATEGORY_POSITION = Configuration.CATEGORY_GENERAL + ".position";
  public static final String CATEGORY_REFERENCE_POINT = Configuration.CATEGORY_GENERAL + ".reference_point";
  public static final String CATEGORY_OFFSET = Configuration.CATEGORY_GENERAL + ".offset";

  private static Configuration config;

  public static void init(File configFile) {
    config = new Configuration(configFile)
      .setCategoryLanguageKey(CATEGORY_POSITION, "thelowbuffhud.config.positioncategory")
      .setCategoryLanguageKey(CATEGORY_REFERENCE_POINT, "thelowbuffhud.config.referencepointcategory")
      .setCategoryLanguageKey(CATEGORY_OFFSET, "thelowbuffhud.config.offsetcategory");

    sync();
  }

  public static void sync() {
    ConfigData.updateInstance(
      config.getString(
        "verticalDirection",
        CATEGORY_POSITION,
        "BOTTOM",
        "",
        new String[]{ "TOP", "MIDDLE", "BOTTOM" },
        "thelowbuffhud.config.verticaldirection"
      ),
      config.getString(
        "horizontalDirection",
        CATEGORY_POSITION,
        "RIGHT",
        "",
        new String[]{ "LEFT", "MIDDLE", "RIGHT" },
        "thelowbuffhud.config.horizontaldirection"
      ),
      config.getString(
        "verticalDirection",
        CATEGORY_REFERENCE_POINT,
        "BOTTOM",
        "",
        new String[]{ "TOP", "MIDDLE", "BOTTOM" },
        "thelowbuffhud.config.verticaldirection"
      ),
      config.getString(
        "horizontalDirection",
        CATEGORY_REFERENCE_POINT,
        "RIGHT",
        "",
        new String[]{ "LEFT", "MIDDLE", "RIGHT" },
        "thelowbuffhud.config.horizontaldirection"
      ),
      config.getInt(
        "verticalDirection",
        CATEGORY_OFFSET,
        -5,
        -1024,
        1024,
        "",
        "thelowbuffhud.config.verticaldirection"
      ),
      config.getInt(
        "horizontalDirection",
        CATEGORY_OFFSET,
        -5,
        -1024,
        1024,
        "",
        "thelowbuffhud.config.horizontaldirection"
      )
    );

    if (config.hasChanged())
      config.save();
  }

  public static List<IConfigElement> getConfigElements() {
    return new ConfigElement(config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements();
  }

}
