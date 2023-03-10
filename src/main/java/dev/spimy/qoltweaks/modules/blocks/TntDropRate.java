package dev.spimy.qoltweaks.modules.blocks;

import dev.spimy.qoltweaks.config.RemovableConfigPaths;
import dev.spimy.qoltweaks.modules.Module;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.HashMap;


@SuppressWarnings("unused")
public class TntDropRate extends Module {

    public TntDropRate() {
        super(
            new HashMap<>() {{
                put("rate", 1.0f);
            }},
            new RemovableConfigPaths[]{RemovableConfigPaths.REQUIRE_PERMISSION});
    }

    @EventHandler
    public void onTNTExplode(final EntityExplodeEvent event) {
        if (isDisabled()) return;
        if (event.getEntityType() == EntityType.PRIMED_TNT) {
            event.setYield((float) getConfigManager().getConfig().getDouble("rate"));
        }
    }

}
