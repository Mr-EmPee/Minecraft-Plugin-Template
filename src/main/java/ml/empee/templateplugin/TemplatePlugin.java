package ml.empee.templateplugin;

import ml.empee.simplemenu.SimpleMenu;
import ml.empee.templateplugin.config.CommandsConfig;
import ml.empee.templateplugin.config.MessageConfig;
import ml.empee.templateplugin.config.client.DbClient;
import ml.empee.templateplugin.controllers.commands.Command;
import ml.empee.templateplugin.utils.Utils;
import mr.empee.lightwire.Lightwire;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Boot class of this plugin.
 **/

public final class TemplatePlugin extends JavaPlugin {

  // private static final String SPIGOT_PLUGIN_ID = "";
  // private static final Integer METRICS_PLUGIN_ID = 0;

  private final Lightwire iocContainer = new Lightwire();
  private final SimpleMenu simpleMenu = new SimpleMenu();

  /**
   * Called when enabling the plugin
   */
  public void onEnable() {
    // Metrics.of(this, METRICS_PLUGIN_ID);
    // Notifier.listenForUpdates(this, SPIGOT_PLUGIN_ID);
    
    simpleMenu.init(this);
    loadEconomyProvider();

    iocContainer.addBean(this);
    iocContainer.loadBeans(getClass().getPackage());

    loadPrefix();
    registerListeners();
    registerCommands();
  }

  private void loadPrefix() {
    var msgConfig = iocContainer.getBean(MessageConfig.class);
    Utils.setPrefix(msgConfig.get("prefix"));
  }

  private void registerCommands() {
    var commandManager = iocContainer.getBean(CommandsConfig.class);
    iocContainer.getAllBeans(Command.class).forEach(c -> commandManager.register(c));
  }

  private void registerListeners() {
    iocContainer.getAllBeans(Listener.class).forEach(
        l -> getServer().getPluginManager().registerEvents(l, this)
    );
  }

  private void loadEconomyProvider() {
    var provider = getServer().getServicesManager().getRegistration(Economy.class);
    if (provider == null) {
      throw new IllegalStateException("Economy provider not found! Load an economy plugin!");
    }

    iocContainer.addBean(provider.getProvider());
  }

  public void onDisable() {
    var dbClient = iocContainer.getBean(DbClient.class);
    if (dbClient != null) {
      dbClient.closeConnections();
    }

    simpleMenu.unregister(this);
  }
}
