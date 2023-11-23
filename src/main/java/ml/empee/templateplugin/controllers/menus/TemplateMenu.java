package ml.empee.templateplugin.controllers.menus;

import lombok.Getter;
import lombok.SneakyThrows;
import ml.empee.templateplugin.model.exceptions.MessageException;
import ml.empee.templateplugin.registries.ThemeRegistry;
import ml.empee.templateplugin.utils.Utils;
import org.bukkit.entity.Player;

import lombok.RequiredArgsConstructor;
import ml.empee.simplemenu.model.menus.ChestMenu;
import mr.empee.lightwire.annotations.Instance;
import mr.empee.lightwire.annotations.Singleton;

/**
 * Menu
 */

@Singleton
@RequiredArgsConstructor
public class TemplateMenu {

  @Getter
  @Instance
  private static TemplateMenu instance;

  private final ThemeRegistry themeRegistry;

  public static void open(Player player) {
    instance.create(player).open();
  }

  private Menu create(Player player) {
    return new Menu(player);
  }

  private class Menu extends ChestMenu {
    public Menu(Player player) {
      super(player, 3);
    }

    @Override
    public void onOpen() {
      //Nothing
    }

    @Override
    public String title() {
      return "";
    }

    @SneakyThrows
    public void handleException(Exception e) {
      if (e instanceof MessageException) {
        Utils.log(player, e.getMessage());
        player.closeInventory();
        return;
      }

      throw e;
    }

  }

}
