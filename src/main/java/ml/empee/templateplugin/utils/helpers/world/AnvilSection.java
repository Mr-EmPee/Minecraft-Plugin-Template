package ml.empee.templateplugin.utils.helpers.world;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.cloudburstmc.nbt.NbtMap;
import org.jetbrains.annotations.Nullable;

public class AnvilSection {

  private final long[] blocksRawData;
  private final NbtMap[] blocksPalette;

  private int bitsUsedForPaletteIndex;
  //Total group in which every element of blocksRawData is divided
  private int availableDataGroup;

  @Getter
  private final int y;

  public AnvilSection(int y, @Nullable long[] blocksRawData, NbtMap[] blocksPalette) {
    this.blocksRawData = blocksRawData;
    this.blocksPalette = blocksPalette;
    this.y = y;

    bitsUsedForPaletteIndex = fastComputeCeilLog2Of(blocksPalette.length);
    if(bitsUsedForPaletteIndex < 4) {
      bitsUsedForPaletteIndex = 4;
    }

    availableDataGroup = 64 / bitsUsedForPaletteIndex;
  }

  private static int fastComputeCeilLog2Of(int n) {
    return n > 0 ? 32 - Integer.numberOfLeadingZeros(n - 1) : 0;
  }

  /**
   * Get a block using its relative coordinates
   * @param x 0-15
   * @param y 0-15
   * @param z 0-15
   */
  public BlockData getBlockRelativelyAt(int x, int y, int z) {
    if(blocksRawData == null || blocksRawData.length == 0) {
      return parseBlockData(blocksPalette[0]);
    }

    int groupNumber = computePosition(x, y, z);
    int index = groupNumber / availableDataGroup;

    //Offset starting from the lowest bit
    int offset = (groupNumber % availableDataGroup) * bitsUsedForPaletteIndex;
    int mask = -1 >>> 32 - bitsUsedForPaletteIndex;

    int paletteIndex = ((int) (blocksRawData[index] >>> offset)) & mask;
    return parseBlockData(blocksPalette[paletteIndex]);
  }

  /**
   * Transform a 3D relative position to an index
   */
  private static int computePosition(int x, int y, int z) {
    return (y << 8) | (z << 4) | x;
  }

  private static BlockData parseBlockData(NbtMap block) {
    StringBuilder rawData = new StringBuilder(block.getString("Name"));
    NbtMap properties = block.getCompound("Properties");

    if(properties != null && !properties.isEmpty()) {
      rawData.append("[");
      properties.forEach((key, value) -> rawData.append(key).append("=").append(value).append(","));
      rawData.setCharAt(rawData.length() - 1, ']');
    }

    return Bukkit.createBlockData(rawData.toString());
  }

}
