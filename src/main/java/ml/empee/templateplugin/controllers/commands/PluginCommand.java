package ml.empee.templateplugin.controllers.commands;

import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import lombok.RequiredArgsConstructor;
import ml.empee.templateplugin.registries.PermissionRegistry;
import ml.empee.templateplugin.utils.Utils;
import mr.empee.lightwire.annotations.Singleton;
import org.bukkit.command.CommandSender;

/**
 * Plugin related commands
 */

@Singleton
@RequiredArgsConstructor
public class PluginCommand implements Command {

  @CommandMethod("demo reload")
  @CommandPermission(PermissionRegistry.ADMIN)
  public void reload(CommandSender sender) {
    Utils.log(sender, "&7The plugin has been reloaded");
  }

}
