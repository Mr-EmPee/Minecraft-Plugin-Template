package ml.empee.templateplugin.registries.themes;

import com.cryptomorin.xseries.XMaterial;
import lombok.RequiredArgsConstructor;
import ml.empee.itembuilder.ItemBuilder;
import ml.empee.templateplugin.config.MessageConfig;
import mr.empee.lightwire.annotations.Singleton;
import org.bukkit.inventory.ItemStack;

@Singleton
@RequiredArgsConstructor
public class Theme {

  protected final MessageConfig msgConfig;

  public ItemStack background() {
    return ItemBuilder
        .from(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem())
        .setName(" ")
        .build();
  }

}
