package ml.empee.templateplugin.utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collection;

//TODO: Fix bug for interaction holograms! Entities misplaced
public class HologramUtils {

  private static final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(HologramUtils.class);
  private static final NamespacedKey idKey = new NamespacedKey(plugin, "id");

  /**
   * @throws IllegalArgumentException if the hologram has more then 10 lines
   */
  public static void create(Location location, String... text) {
    create(location, Arrays.asList(text));
  }

  /**
   * @throws IllegalArgumentException if the hologram has more then 10 lines
   */
  public static void create(Location location, Collection<String> text) {
    if(text.size() > 10) {
      throw new IllegalArgumentException("Can't create an hologram with more then 10 lines!");
    }

    String id = getId(location);
    for (String content : text) {
      if(content.isBlank()) {
        location.subtract(0, 0.3, 0);
        continue;
      }

      ArmorStand line = (ArmorStand) location.getWorld().spawnEntity(
        location.subtract(0, 0.3, 0), EntityType.ARMOR_STAND
      );

      ArmorStand line_hitbox = (ArmorStand) location.getWorld().spawnEntity(
        location, EntityType.ARMOR_STAND
      ) ;

      line.getPersistentDataContainer().set(idKey, PersistentDataType.STRING, id);
      line_hitbox.getPersistentDataContainer().set(idKey, PersistentDataType.STRING, id);

      line.setCustomName(ChatColor.translateAlternateColorCodes('&', content));
      line.setCustomNameVisible(true);
      line.setMarker(true);

      line_hitbox.setPersistent(true);
      line.setPersistent(true);
      line_hitbox.setSmall(true);
      line.setSmall(true);
      line_hitbox.setBasePlate(false);
      line.setBasePlate(false);
      line_hitbox.setGravity(false);
      line.setGravity(false);
      line_hitbox.setVisible(false);
      line.setVisible(false);
    }
  }

  private static String getId(Location location) {
    return location.getBlockX() + "X" + location.getBlockY() + "Y" + location.getBlockZ() + "Z";
  }

  public static void delete(Location location) {
    for (Entity entity : location.getWorld().getNearbyEntities(location, 1, 5, 1)) {
      if(isHologram(location, entity)) {
        entity.remove();
      }
    }
  }

  public static boolean isHologram(Location location, Entity entity) {
    if(entity.getType() != EntityType.ARMOR_STAND) {
      return false;
    }

    return getId(location).equals(
      entity.getPersistentDataContainer().get(idKey, PersistentDataType.STRING)
    );
  }
  public static boolean isHologram(Entity entity) {
    if(entity.getType() != EntityType.ARMOR_STAND) {
      return false;
    }

    return entity.getPersistentDataContainer().has(idKey, PersistentDataType.STRING);
  }

}
