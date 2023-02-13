package ml.empee.templateplugin.utils.helpers.cache;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Link a temporary data to a player. **/

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlayerData<T> {

  private final UUID uuid;
  @Nullable
  private final Player player;
  private T data;

  /** Build an instance. **/
  public static <T> PlayerData<T> of(@NotNull Player player, T data) {
    return new PlayerData<>(player.getUniqueId(), player, data);
  }

  /** Build an instance. **/
  public static <T> PlayerData<T> of(@NotNull UUID uuid, T data) {
    return new PlayerData<>(uuid, null, data);
  }

}
