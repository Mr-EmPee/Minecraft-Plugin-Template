package ml.empee.templateplugin;

import lombok.Getter;
import ml.empee.commandsManager.CommandManager;
import ml.empee.commandsManager.command.CommandExecutor;
import ml.empee.ioc.SimpleIoC;
import org.bukkit.plugin.java.JavaPlugin;

/** Boot class of this plugin. **/

public final class TemplatePlugin extends JavaPlugin {

  public static final String PREFIX = "  &5Temp &8»&r ";
  private static final String SPIGOT_PLUGIN_ID = "";
  private static final Integer METRICS_PLUGIN_ID = 0;

  private final CommandManager commandManager = new CommandManager(this);

  @Getter
  private final SimpleIoC iocContainer = new SimpleIoC(this);

  static {
    CommandExecutor.setPrefix(PREFIX);
  }

  /** Invoked when the plugin is being enabled. **/
  public void onEnable() {
    iocContainer.addBean(commandManager);
    iocContainer.initialize("relocations");

    //Metrics.of(this, METRICS_PLUGIN_ID);
    //Notifier.listenForUpdates(this, SPIGOT_PLUGIN_ID);
  }

  /** Invoked when disabling the plugin. **/
  public void onDisable() {
    commandManager.unregisterCommands();
    iocContainer.removeAllBeans();
  }
}
