package ml.empee.templateplugin.utils;

import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

/**
 * This class allow you to easily log messages.
 **/

@UtilityClass
public class Utils {

  private static final String HEX_PREFIX = "&#";
  private static final Pattern HEX_COLOR = Pattern.compile(HEX_PREFIX + "[a-zA-z0-9]{6}");

  @Getter
  @Setter
  private String prefix;

  @Getter
  @Setter
  private boolean debug;

  @Setter
  private java.util.logging.Logger consoleLogger = JavaPlugin.getProvidingPlugin(Utils.class).getLogger();

  public String applyFormatting(String text) {
    text = text.replace("\t", "    ");
    if (text.endsWith("\n")) {
      text += " ";
    }

    return text;
  }

  public String applyColors(String text) {
    Matcher matcher = HEX_COLOR.matcher(text);
    while (matcher.find()) {
      String group = matcher.group().substring(HEX_PREFIX.length());
      StringBuilder hex = new StringBuilder("&x");
      for (char code : group.toLowerCase().toCharArray()) {
        hex.append("&").append(code);
      }

      text = text.replace(HEX_PREFIX + group, hex.toString());
    }

    return ChatColor.translateAlternateColorCodes('&', text);
  }

  public String replacePlaceholders(String text, Map<String, Object> placeholders) {
    for (var entry : placeholders.entrySet()) {
      text = text.replace("{" + entry.getKey() + "}", entry.getValue().toString());
    }

    return text;
  }

  /**
   * Send a formatted message to the player
   */
  public void log(CommandSender sender, String message) {
    log(sender, message, null);
  }

  /**
   * Send a formatted message to the player
   */
  public void log(CommandSender sender, String message, @Nullable Map<String, Object> placeholders) {
    message = prefix + message;

    if (placeholders != null) {
      message = replacePlaceholders(message, placeholders);
    }

    sender.sendMessage(
        applyColors(applyFormatting(message))
    );
  }

  /**
   * Log a debug message to a player.
   **/
  public void debug(CommandSender player, String message, Object... args) {
    if (debug) {
      log(player, String.format(message, args), null);
    }
  }

  /**
   * Log to the console a debug message.
   **/
  public void debug(String message, Object... args) {
    if (debug) {
      consoleLogger.info(String.format(Locale.ROOT, message, args));
    }
  }

  /**
   * Log to the console an info message.
   **/
  public void info(String message, Object... args) {
    if (consoleLogger.isLoggable(Level.INFO)) {
      consoleLogger.info(String.format(Locale.ROOT, message, args));
    }
  }

  /**
   * Log to the console a warning message.
   **/
  public void warning(String message, Object... args) {
    if (consoleLogger.isLoggable(Level.WARNING)) {
      consoleLogger.warning(String.format(Locale.ROOT, message, args));
    }
  }

  /**
   * Log to the console an error message.
   **/
  public void error(String message, Object... args) {
    if (consoleLogger.isLoggable(Level.SEVERE)) {
      consoleLogger.severe(String.format(Locale.ROOT, message, args));
    }
  }
}
