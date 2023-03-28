package ml.empee.templateplugin.utils;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

public class Translator {

  private static Translator instance;

  public static void init(JavaPlugin plugin) {
    instance = new Translator(plugin);
  }
  public static Translator getInstance() {
    if(instance == null) {
      throw new IllegalStateException("The translator hasn't been initialize!");
    }

    return instance;
  }

  private final JavaPlugin plugin;
  private final ResourceBundle defaultBundle;

  public Translator(JavaPlugin plugin) {
    this.plugin = plugin;
    this.defaultBundle = getResourceBundle(Locale.getDefault());
  }

  @SneakyThrows
  private ResourceBundle getResourceBundle(Locale locale) {
    File messageFile = new File(plugin.getDataFolder(), "messages.properties");
    if(!messageFile.exists()) {
      messageFile.getParentFile().mkdirs();
      FileUtils.copyInputStreamToFile(plugin.getResource("messages.properties"), messageFile);
    }

    URL url = messageFile.getParentFile().toURI().toURL();
    return ResourceBundle.getBundle("messages", locale, new URLClassLoader(new URL[]{url}));
  }

  private static String[] parseBlock(String input) {
    if(input.endsWith("\n")) {
      input += " ";
    }

    return input.split("\n");
  }

  public static String translate(String key) {
    return getInstance().defaultBundle.getString(key);
  }
  public static String translate(String key, Locale locale) {
    return getInstance().getResourceBundle(locale).getString(key);
  }

  public static String[] translateBlock(String key) {
    return parseBlock(translate(key));
  }
  public static String[] translateBlock(String key, Locale locale) {
    return parseBlock(translate(key, locale));
  }

}
