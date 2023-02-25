package ml.empee.templateplugin.utils.helpers.world;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.nbt.CompoundTag;

/** This class works from 1.17 **/

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class NbtUtils {

  private final static Method snbtToStructure;

  static {
    Class<?> compoundClazz = CompoundTag.class;
    Class<?> nbtUtils = net.minecraft.nbt.NbtUtils.class;

    snbtToStructure = Arrays.stream(nbtUtils.getMethods())
      .filter(m -> m.getParameterCount() == 1)
      .filter(m -> m.getReturnType().equals(compoundClazz))
      .filter(m -> m.getParameters()[0].getType().equals(String.class))
      .findFirst().orElseThrow( () -> new RuntimeException("Unable to find the snbtToStructure method!"));
  }

  public static Object toCompoundTag(String snbt) throws InvocationTargetException, IllegalAccessException {
    return snbtToStructure.invoke(null, snbt);
  }

}
