package ml.empee.templateplugin.utils.helpers.world;

import java.nio.file.Files;
import java.nio.file.Path;
import org.jetbrains.annotations.Nullable;

public class AnvilWorld {

  private final Path worldFolder;
  private final Path regionFolder;

  public AnvilWorld(Path worldFolder) {
    this.worldFolder = worldFolder;

    if (Files.notExists(worldFolder) || !Files.isDirectory(worldFolder)) {
      throw new IllegalArgumentException("Level path " + worldFolder + " did not exist or was not a directory!");
    }

    regionFolder = worldFolder.resolve("region");
    if (Files.notExists(regionFolder) || !Files.isDirectory(regionFolder)) {
      throw new IllegalArgumentException("Region path " + regionFolder + " did not exist or was not a directory!");
    }
  }

  /**
   * The requested chunk or null if it not exists
   */
  @Nullable
  public AnvilChunk getChunk(int x, int z) {
    return AnvilChunk.deserialize(regionFolder, x, z);
  }

}
