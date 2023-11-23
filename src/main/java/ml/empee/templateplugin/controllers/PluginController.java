package ml.empee.templateplugin.controllers;

import lombok.RequiredArgsConstructor;
import ml.empee.templateplugin.config.MessageConfig;
import ml.empee.templateplugin.registries.ItemRegistry;
import ml.empee.templateplugin.registries.ThemeRegistry;
import ml.empee.templateplugin.utils.Utils;
import mr.empee.lightwire.annotations.Singleton;
import org.bukkit.command.CommandSender;

@Singleton
@RequiredArgsConstructor
public class PluginController {

  private final ItemRegistry itemRegistry;
  private final ThemeRegistry themeRegistry;
  private final MessageConfig messageConfig;

  public void reload(CommandSender sender) {
    messageConfig.reload();
    itemRegistry.reload();
    themeRegistry.reload();

    Utils.log(sender, "&7The plugin has been reloaded");
  }

}
