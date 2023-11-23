package ml.empee.templateplugin.registries;

import ml.empee.templateplugin.config.MessageConfig;
import mr.empee.lightwire.annotations.Singleton;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class ThemeRegistry {

  private final Map<String, ItemStack> registry = new HashMap<>();

  private final MessageConfig msgConfig;

  public ThemeRegistry(MessageConfig msgConfig) {
    this.msgConfig = msgConfig;

    registerDefaultTheme();
  }

  private void registerDefaultTheme() {

  }

  public ItemStack getItem(Type type, String id) {
    var item = registry.get(type.toId(id));
    return item.clone();
  }

  public void reload() {

  }

  public enum Type {
    DEFAULT;

    private String toId(String id) {
      return String.join("-", name(), id);
    }
  }

}
