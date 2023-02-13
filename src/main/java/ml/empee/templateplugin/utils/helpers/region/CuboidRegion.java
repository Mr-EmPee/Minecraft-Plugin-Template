package ml.empee.templateplugin.utils.helpers.region;

import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ml.empee.templateplugin.utils.LocationUtils;
import ml.empee.templateplugin.utils.helpers.Flux;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * Represent a cuboid region.
 **/

@Getter
@Setter
@NoArgsConstructor
public class CuboidRegion implements Region {

  private Location firstCorner;
  private Location secondCorner;
  private transient World world;

  /**
   * Constructor used for instantiating the region.
   **/
  public CuboidRegion(@NotNull Location firstCorner, @NotNull Location secondCorner) {
    setCorners(firstCorner, secondCorner);
  }

  private void validateCorners() {
    if (!Objects.equals(firstCorner.getWorld(), secondCorner.getWorld())) {
      throw new IllegalArgumentException("Two corners of a region must be in the same dimension");
    }

    Location[] corners = LocationUtils.sortLocations(firstCorner, secondCorner);
    this.firstCorner = corners[0];
    this.secondCorner = corners[1];
  }

  /**
   * Get the minimum corner of this Region.
   *
   * @return The corner with the lowest X, Y, and Z values
   */
  @NotNull
  public Location getFirstCorner() {
    return firstCorner.clone();
  }

  /**
   * Get the greatest corner of this Region.
   *
   * @return The corner with the highest X, Y, and Z values
   */
  @NotNull
  public Location getSecondCorner() {
    return secondCorner.clone();
  }

  /**
   * Set the corners of this region.
   **/
  public void setCorners(@NotNull Location firstCorner, @NotNull Location secondCorner) {
    this.firstCorner = firstCorner;
    this.secondCorner = secondCorner;
    validateCorners();
  }

  /**
   * {@inheritDoc}
   **/
  public boolean isWithinRegion(@NotNull Location location) {
    if (!Objects.equals(location.getWorld(), getWorld())) {
      return false;
    }

    return isWithinRegion(location.getBlockX(), location.getBlockY(), location.getBlockZ());
  }

  /**
   * {@inheritDoc}
   **/
  public boolean isWithinRegion(int x, int y, int z) {
    return x >= firstCorner.getBlockX() && x <= secondCorner.getBlockX()
        && y >= firstCorner.getBlockY() && y <= secondCorner.getBlockY()
        && z >= firstCorner.getBlockZ() && z <= secondCorner.getBlockZ();
  }

  /**
   * {@inheritDoc}
   **/
  public Flux<Location> getRegionBlocks() {
    return new Flux<Location>() {
      @Override
      public void produceItems() {
        for (int x = firstCorner.getBlockX(); x <= secondCorner.getBlockX(); x++) {
          for (int y = firstCorner.getBlockY(); y <= secondCorner.getBlockY(); y++) {
            for (int z = firstCorner.getBlockZ(); z <= secondCorner.getBlockZ(); z++) {
              supply(new Location(getWorld(), x, y, z));
            }
          }
        }
      }
    };
  }

}
