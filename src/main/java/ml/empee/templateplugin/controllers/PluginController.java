package ml.empee.templateplugin.controllers;

import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import lombok.RequiredArgsConstructor;
import ml.empee.ioc.Bean;
import ml.empee.templateplugin.config.CommandsConfig;
import ml.empee.templateplugin.config.LangConfig;
import ml.empee.templateplugin.constants.Permissions;
import ml.empee.templateplugin.utils.Logger;
import org.bukkit.command.CommandSender;

/**
 * Plugin related commands
 */

@RequiredArgsConstructor
public class PluginController implements Bean {

  private final CommandsConfig commandsConfig;
  private final LangConfig langConfig;

  @Override
  public void onStart() {
    commandsConfig.register(this);
  }

  @CommandMethod("dm reload")
  @CommandPermission(Permissions.ADMIN)
  public void reload(CommandSender sender) {
    langConfig.reload();

    Logger.log(sender, "&7The plugin has been reloaded");
  }

}
