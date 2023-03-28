package ml.empee.templateplugin;

import lombok.Getter;
import ml.empee.ioc.SimpleIoC;
import ml.empee.templateplugin.utils.Logger;
import ml.empee.templateplugin.utils.Translator;
import org.bukkit.plugin.java.JavaPlugin;

/** Boot class of this plugin. **/

public final class TemplatePlugin extends JavaPlugin {

  //private static final String SPIGOT_PLUGIN_ID = "";
  //private static final Integer METRICS_PLUGIN_ID = 0;

  @Getter
  private final SimpleIoC iocContainer = new SimpleIoC(this);

  public void onEnable() {
    Translator.init(this);
    Logger.setPrefix(Translator.translate("prefix"));

    //Metrics.of(this, METRICS_PLUGIN_ID);
    //Notifier.listenForUpdates(this, SPIGOT_PLUGIN_ID);

    iocContainer.initialize("relocations");
  }

  public void onDisable() {
    iocContainer.removeAllBeans(true);
  }
}
