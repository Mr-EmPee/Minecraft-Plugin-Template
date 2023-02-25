package ml.empee.templateplugin.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

/** Set of utilities method used to work with locations. **/

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LocationUtils {

  /** Check if 2 locations represent the same block. **/
  public static boolean isSameBlock(Location fromLoc, Location toLoc) {
    if (toLoc == null) {
      return false;
    }

    toLoc = toLoc.getBlock().getLocation();
    fromLoc = fromLoc.getBlock().getLocation();
    if (toLoc.equals(fromLoc)) {
      return false;
    }

    return true;
  }

  /** Get all the blocks within a radius. **/
  public static List<Location> getBlocksWithin(Location location, int radius) {
    int maxX = location.getBlockX() + radius;
    int maxY = location.getBlockY() + radius;
    int maxZ = location.getBlockZ() + radius;
    int minX = location.getBlockX() - radius;
    int minY = location.getBlockY() - radius;
    int minZ = location.getBlockZ() - radius;

    List<Location> locations = new ArrayList<>();
    for (int y = minY; y <= maxY; y++) {
      for (int x = minX; x <= maxX; x++) {
        for (int z = minZ; z <= maxZ; z++) {
          locations.add(new Location(location.getWorld(), x, y, z));
        }
      }
    }

    return locations;
  }

  /**
   * Get the major distance between the the x,y,z axis of the two locations.
   * <br><br>
   * <b>Example:</b> <br>
   * <ul>
   * <li>Location 1: 0, -20, 0</li>
   * <li>Location 2: 10, 0, -13</li>
   * </ul>
   * this method will return 20
   */
  public static int getGreatestAxisDistance(Location location1, Location location2) {
    int x = location1.getBlockX() - location2.getBlockX();
    int y = location1.getBlockY() - location2.getBlockY();
    int z = location1.getBlockZ() - location2.getBlockZ();

    return Math.max(Math.max(Math.abs(x), Math.abs(y)), Math.abs(z));
  }

  /** Get a list of all the adjacent blocks. **/
  public static List<Location> getAdjacentBlocks(Location center) {
    center = center.getBlock().getLocation();

    return Arrays.asList(
        center,
        center.clone().add(1, 0, 0),
        center.clone().add(0, 1, 0),
        center.clone().add(0, 0, 1),
        center.clone().add(-1, 0, 0),
        center.clone().add(0, -1, 0),
        center.clone().add(0, 0, -1)
    );
  }

  /** Find the greatest and the smallest corner. **/
  public static Location[] sortLocations(Location first, Location second) {
    if (first.getWorld() != second.getWorld()) {
      throw new IllegalArgumentException("Locations must be in the same world");
    }

    double minX = Math.min(first.getX(), second.getX());
    double maxX = Math.max(first.getX(), second.getX());
    double minY = Math.min(first.getY(), second.getY());
    double maxY = Math.max(first.getY(), second.getY());
    double minZ = Math.min(first.getZ(), second.getZ());
    double maxZ = Math.max(first.getZ(), second.getZ());

    return new Location[]{
        new Location(first.getWorld(), minX, minY, minZ),
        new Location(second.getWorld(), maxX, maxY, maxZ)
    };
  }

  /** Get the blocks between the starting location and the ending location. **/
  public static List<Location> getBlocksBetween(Location start, Location end) {
    Location[] sortedLocations = sortLocations(start, end);
    start = sortedLocations[0];
    end = sortedLocations[1];

    ArrayList<Location> locations = new ArrayList<>();
    for (int x = start.getBlockX(); x <= end.getBlockX(); x++) {
      for (int y = start.getBlockY(); y <= end.getBlockY(); y++) {
        for (int z = start.getBlockZ(); z <= end.getBlockZ(); z++) {
          locations.add(new Location(start.getWorld(), x, y, z));
        }
      }
    }

    return locations;
  }

  /** Get the blocks between the starting location and the ending location. **/
  public static List<Location> getBlocksBetween(Location start, Vector velocity) {
    return getBlocksBetween(start, start.clone().add(velocity));
  }

  /**
   * Calculate the face of the block nearest to the target
   * @param target
   * @param block
   */
  public static BlockFace getFaceInFront(Block target, Block block) {
    int dx = target.getX() - block.getX();
    int dy = target.getY() - block.getY();
    int dz = target.getZ() - block.getZ();

    int absDx = Math.abs(dx);
    int absDy = Math.abs(dy);
    int absDz = Math.abs(dz);

    if (absDx >= absDy && absDx >= absDz) {
      if (dx > 0) return BlockFace.EAST;
      else if (dx < 0) return BlockFace.WEST;
    } else if (absDy >= absDx && absDy >= absDz) {
      if (dy > 0) return BlockFace.UP;
      else if (dy < 0) return BlockFace.DOWN;
    } else {
      if (dz > 0) return BlockFace.SOUTH;
      else if (dz < 0) return BlockFace.NORTH;
    }

    return BlockFace.SELF;
  }

}
