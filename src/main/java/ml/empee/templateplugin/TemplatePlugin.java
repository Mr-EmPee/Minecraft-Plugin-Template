package ml.empee.templateplugin;

import lombok.Getter;
import ml.empee.ioc.SimpleIoC;
import ml.empee.templateplugin.config.DatabaseConfig;
import org.bukkit.plugin.java.JavaPlugin;

/** Boot class of this plugin. **/

public final class TemplatePlugin extends JavaPlugin {

  public static final String PREFIX = "  &5Temp &8»&r ";
  private static final String SPIGOT_PLUGIN_ID = "";
  private static final Integer METRICS_PLUGIN_ID = 0;

  private final DatabaseConfig databaseConfig = new DatabaseConfig(this);

  @Getter
  private final SimpleIoC iocContainer = new SimpleIoC(this);

  public void onEnable() {
    iocContainer.addBean(databaseConfig);
    iocContainer.initialize("relocations");

    //Metrics.of(this, METRICS_PLUGIN_ID);
    //Notifier.listenForUpdates(this, SPIGOT_PLUGIN_ID);
  }

  public void onDisable() {
    iocContainer.removeAllBeans();
    databaseConfig.closeConnection();
  }
}
