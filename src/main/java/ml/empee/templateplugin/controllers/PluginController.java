package ml.empee.templateplugin.controllers;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandMethod;
import lombok.RequiredArgsConstructor;
import ml.empee.ioc.Bean;
import ml.empee.templateplugin.config.CommandsConfig;
import ml.empee.templateplugin.utils.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class PluginController implements Bean {

  private final CommandsConfig commandsConfig;

  @Override
  public void onStart() {
    commandsConfig.register(this);
  }

  @CommandMethod("echo <text> [target]")
  public void sendEcho(
    CommandSender sender,
    @Argument String text,
    @Argument @Nullable Player target
  ) {
    if (target != null) {
      sender = target;
    }

    Logger.log(sender, text);
  }

}
