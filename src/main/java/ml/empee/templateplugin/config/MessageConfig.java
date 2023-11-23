package ml.empee.templateplugin.config;

import lombok.Getter;
import ml.empee.templateplugin.utils.Utils;
import mr.empee.lightwire.annotations.Instance;
import mr.empee.lightwire.annotations.Singleton;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Handle messages
 */

@Singleton
public class MessageConfig extends AbstractConfig {

  @Getter
  @Instance
  private static MessageConfig instance;

  public MessageConfig(JavaPlugin plugin) {
    super(plugin, "messages.yml", 1);
  }

  @Override
  protected void update(int from) {

  }

  public String get(String key) {
    return get(key, null);
  }

  public String get(String key, @Nullable Map<String, Object> placeholders) {
    var text = config.getString(key);
    if (text == null) {
      throw new IllegalArgumentException("Missing message key: " + key);
    }

    if (placeholders != null) {
      text = Utils.replacePlaceholders(text, placeholders);
    }

    return Utils.applyColors(Utils.applyFormatting(text));
  }

  public List<String> getBlock(String key, @Nullable Map<String, Object> placeholders) {
    return Arrays.asList(get(key, placeholders).split("\n"));
  }

  public List<String> getBlock(String key) {
    return Arrays.asList(get(key, null).split("\n"));
  }

}