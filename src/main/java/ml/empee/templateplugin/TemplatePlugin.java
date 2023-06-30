package ml.empee.templateplugin;

import lombok.Getter;
import ml.empee.ioc.SimpleIoC;
import ml.empee.templateplugin.config.LangConfig;
import ml.empee.templateplugin.utils.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Boot class of this plugin.
 **/

public final class TemplatePlugin extends JavaPlugin {

  //private static final String SPIGOT_PLUGIN_ID = "";
  //private static final Integer METRICS_PLUGIN_ID = 0;

  @Getter
  private final SimpleIoC iocContainer = new SimpleIoC(this);

  /**
   * Called when enabling the plugin
   */
  public void onEnable() {
    //Metrics.of(this, METRICS_PLUGIN_ID);
    //Notifier.listenForUpdates(this, SPIGOT_PLUGIN_ID);

    iocContainer.initialize("relocations");
    Logger.setPrefix(iocContainer.getBean(LangConfig.class).translate("prefix"));
  }

  public void onDisable() {
    iocContainer.removeAllBeans(true);
  }
}
