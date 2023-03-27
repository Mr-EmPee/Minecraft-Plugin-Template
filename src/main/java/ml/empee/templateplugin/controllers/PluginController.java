package ml.empee.templateplugin.controllers;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandMethod;
import lombok.RequiredArgsConstructor;
import ml.empee.ioc.Bean;
import ml.empee.templateplugin.config.CommandsConfig;
import ml.empee.templateplugin.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class PluginController implements Bean {

  private final CommandsConfig commandsConfig;
  private final JavaPlugin plugin;

  @Override
  public void onStart() {
    commandsConfig.register(this);
  }

  @CommandMethod("echo")
  public void sendEcho(
    Player sender
  ) {
    Shulker shulker = sender.getLocation().getNearbyEntitiesByType(Shulker.class, 2).stream()
        .findAny().orElse(null);

    if(shulker == null) {
      return;
    }

    Bukkit.getScheduler().runTaskTimer(plugin,
      () -> {
        Vector vector = new Vector(0, 0, 0.1);
        shulker.teleport(shulker.getLocation().add(vector));
      }, 0, 1
    );
  }

}
