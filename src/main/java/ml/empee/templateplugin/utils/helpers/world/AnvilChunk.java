package ml.empee.templateplugin.utils.helpers.world;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ml.empee.templateplugin.utils.PaperUtils;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.Location;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_19_R2.block.CraftBlockEntityState;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.InflaterInputStream;

@RequiredArgsConstructor
public class AnvilChunk {

  private static final int SECTOR_SIZE = 4096;

  private final Map<Location, NbtMap> tileBlockStates;
  private final List<AnvilSection> sections;
  @Getter
  private final int chunkX;
  @Getter
  private final int chunkZ;

  @Nullable
  @SneakyThrows
  public static AnvilChunk deserialize(Path regionFolder, int x, int z) {
    if(!Files.isDirectory(regionFolder)) {
      return null;
    }

    File regionFile = regionFolder.resolve("r." + (x >> 5) + "." + (z >> 5) + ".mca").toFile();
    if(!regionFile.exists()) {
      return null;
    }

    RandomAccessFile file = new RandomAccessFile(regionFile, "r");

    //Traverse the chunk pointer table
    file.seek(4 * ((x & 31) + (z & 31) * 32));
    int chunkOffset = file.read() << 16;
    chunkOffset |= (file.read() & 0xFF) << 8;
    chunkOffset |= file.read() & 0xFF;

    int chunkSize = file.readByte();
    if (chunkSize == 0) {
      return null;
    }

    file.seek((long) SECTOR_SIZE * chunkOffset);
    return deserializeChunk(file, x, z);
  }

  @SneakyThrows
  private static AnvilChunk deserializeChunk(RandomAccessFile file, int x, int z) {
    int chunkLength = file.readInt();
    byte compressionType = file.readByte();

    if (compressionType != 2) {
      throw new IllegalArgumentException("Only Zlib compression is supported!");
    }

    try (
        NBTInputStream chunkStream = org.cloudburstmc.nbt.NbtUtils.createReader(
            new BufferedInputStream(new InflaterInputStream(new FileInputStream(file.getFD())))
        )
    ) {
      return parseNbtChunk((NbtMap) chunkStream.readTag(), x, z);
    }
  }

  private static AnvilChunk parseNbtChunk(NbtMap chunk, int x, int z) {
    Map<Location, NbtMap> tileStates = new HashMap<>();
    for (NbtMap tileBlockState : chunk.getList("block_entities", NbtType.COMPOUND)) {
      Location tileLocation = new Location(null,
        tileBlockState.getInt("x"),
        tileBlockState.getInt("y"),
        tileBlockState.getInt("z")
      );

      tileStates.put(tileLocation, tileBlockState);
    }

    List<AnvilSection> sections = new ArrayList<>();
    for (NbtMap section : chunk.getList("sections", NbtType.COMPOUND)) {
      NbtMap blocksStates = section.getCompound("block_states");

      sections.add(
          new AnvilSection(
              section.getByte("Y"),
              blocksStates.getLongArray("data"),
              blocksStates.getList("palette", NbtType.COMPOUND).toArray(new NbtMap[0])
          )
      );
    }

    return new AnvilChunk(tileStates, sections, x, z);
  }

  /**
   * Retrieve a clean copy of the restore-point data of the block
   * @param x 0-15
   * @param y world minHeight (inclusive) - world maxHeight (exclusive)
   * @param z 0-15
   * @return null if the y-coordinate is invalid
   */
  @Nullable
  public BlockData getBlockRelativelyAt(int x, int y, int z) {
    return getSection(y >> 4).map(               //Keep the last 4 bits y rel coord
        anvilSection -> anvilSection.getBlockRelativelyAt(x, y & 0xF, z)
    ).orElse(null);
  }


  /**
   * Apply the chunk saved tile state to the target
   */
  @SneakyThrows
  public boolean applyTileStateTo(TileState tileState) {
    Location location = tileState.getLocation();
    location.setWorld(null);

    NbtMap state = tileBlockStates.get(location);
    if(state == null) {
      return false;
    }

    CompoundTag nmsTag = (CompoundTag) NbtUtils.toCompoundTag(state.toString());
    CraftBlockEntityState<?> craftState = (CraftBlockEntityState<?>) tileState;

    craftState.getTileEntity().load(nmsTag);
    if(PaperUtils.isRunningPaper && !craftState.snapshotDisabled) {
      craftState.refreshSnapshot();
    }

    tileState.update(true);
    return true;
  }

  private Optional<AnvilSection> getSection(int y) {
    return sections.stream().filter(
        s -> s.getY() == y
    ).findFirst();
  }

}
