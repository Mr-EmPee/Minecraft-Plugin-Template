package ml.empee.templateplugin.registries;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.RequiredArgsConstructor;
import ml.empee.templateplugin.config.MessageConfig;
import mr.empee.lightwire.annotations.Instance;
import mr.empee.lightwire.annotations.Singleton;

/**
 * Contains all the plugin custom items
 */

@Singleton
@RequiredArgsConstructor
public class ItemRegistry {

  @Getter
  @Instance
  private static ItemRegistry instance;

  private final JavaPlugin plugin;
  private final MessageConfig msgConfig;

  public void reload() {
    msgConfig.reload();
  }

}
