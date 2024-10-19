package com.github.taichi3012.thelowbuffhud;

import java.util.Random;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.logging.log4j.Logger;

import com.github.taichi3012.thelowbuffhud.config.TheLowBuffHUDConfig;
import com.github.taichi3012.thelowbuffhud.gui.GuiTheLowBuffStatus;
import com.github.taichi3012.thelowbuffhud.thelowapi.TheLowAPIHandler;
import com.github.taichi3012.thelowbuffhud.util.TaskScheduler;
import com.github.taichi3012.thelowbuffhud.util.TheLowUtil;

@Mod(
  modid = TheLowBuffHUDMod.MOD_ID,
  name = TheLowBuffHUDMod.MOD_NAME,
  useMetadata = true,
  clientSideOnly = true,
  guiFactory = "com.github.taichi3012.thelowbuffhud.gui.GuiFactoryTheLowBuffHUD"
)
public class TheLowBuffHUDMod {

  public static final String MOD_ID = "thelowbuffhud";
  public static final String MOD_NAME = "TheLow Buff HUD";

  public static Minecraft mc;

  public static Logger logger;

  private static int requestTaskHash;

  GuiTheLowBuffStatus gui;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    logger = event.getModLog();
    TheLowBuffHUDConfig.init(event.getSuggestedConfigurationFile());
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.register(new TaskScheduler());
    mc = Minecraft.getMinecraft();
    gui = new GuiTheLowBuffStatus(Minecraft.getMinecraft());
  }

  @SubscribeEvent
  public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
    if (event.type != RenderGameOverlayEvent.ElementType.ALL)
      return;

    gui.render();
  }

  private static final String CONNECTED_MESSAGE = "§r§aインベントリ・座標情報のロードに成功しました。§r";
  private static final Pattern BUFF_MESSAGE = Pattern.compile("§r§a.+を\\d+秒間付与した。§r");
  @SubscribeEvent(receiveCanceled = true)
  public void onChatReceived(ClientChatReceivedEvent event) {
    String message = event.message.getFormattedText();

    if (message.startsWith("§r$api ")) {
      event.setCanceled(true);
      TheLowAPIHandler.processResponse(message.substring(7, message.length() - 2));
      return;
    }

    if (CONNECTED_MESSAGE.equals(message)) {
      int waitTick = 20 * (30 + new Random().nextInt(30));
      TaskScheduler.cancelTask(requestTaskHash);
      requestTaskHash = TaskScheduler.scheduleTask(
        TheLowBuffHUDMod::requestBuffData,
        waitTick,
        20 * 180,
        TaskScheduler.ENDLESS_LOOP
      );
      return;
    }

    if (BUFF_MESSAGE.matcher(message).matches() && TheLowUtil.isPlayingTheLow()) {
      TaskScheduler.cancelTask(requestTaskHash);
      requestTaskHash = TaskScheduler.scheduleTask(
        TheLowBuffHUDMod::requestBuffData,
        10,
        20 * 180,
        TaskScheduler.ENDLESS_LOOP
      );
    }

  }

  private static void requestBuffData() {
    if (TheLowUtil.isPlayingTheLow())
      sendChatText("/thelow_api buff");
  }

  @SubscribeEvent
  public void onPlayerLoggedOut(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
    TaskScheduler.cancelTask(requestTaskHash);
  }

  @SubscribeEvent
  public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.modID.equals(TheLowBuffHUDMod.MOD_ID))
      TheLowBuffHUDConfig.sync();
  }

  public static void sendChatText(String text) {
    mc.thePlayer.sendChatMessage(text);
  }

  public static void printMessage(String text) {
    mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(text));
  }

}
