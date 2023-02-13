package ml.empee.templateplugin.utils.helpers.region;

import ml.empee.templateplugin.utils.helpers.Flux;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/** Represent a region defined by corners. **/

public interface Region {

  /** Get the region world. **/
  World getWorld();

  /** Check if the location is within the region corners. **/
  boolean isWithinRegion(@NotNull Location location);

  /** Check if the location is within the region corners. **/
  default boolean isWithinRegion(int x, int y, int z) {
    return isWithinRegion(new Location(getWorld(), x, y, z));
  }

  /** Produce a flux that will produce all the region blocks. **/
  Flux<Location> getRegionBlocks();
}
