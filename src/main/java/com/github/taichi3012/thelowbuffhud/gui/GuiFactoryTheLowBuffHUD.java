package com.github.taichi3012.thelowbuffhud.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

import com.github.taichi3012.thelowbuffhud.TheLowBuffHUDMod;
import com.github.taichi3012.thelowbuffhud.config.TheLowBuffHUDConfig;

public class GuiFactoryTheLowBuffHUD implements IModGuiFactory {

  @Override
  public void initialize(Minecraft minecraftInstance) {
  }

  @Override
  public Class<? extends GuiScreen> mainConfigGuiClass() {
    return GuiConfigTheLowDurabilityDisplay.class;
  }

  @Override
  public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
    return null;
  }

  @Override
  public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
    return null;
  }

  public static class GuiConfigTheLowDurabilityDisplay extends GuiConfig {
    public GuiConfigTheLowDurabilityDisplay(GuiScreen parent) {
      super(
        parent,
        TheLowBuffHUDConfig.getConfigElements(),
        TheLowBuffHUDMod.MOD_ID,
        false,
        false,
        TheLowBuffHUDMod.MOD_NAME
      );
    }
  }

}
