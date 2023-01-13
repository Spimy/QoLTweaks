package dev.spimy.qoltweaks.modules.entities;

import dev.spimy.qoltweaks.config.RemovableConfigPaths;
import dev.spimy.qoltweaks.modules.Module;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class AntiEndermanGrief extends Module {

    public AntiEndermanGrief() {
        super(
            new RemovableConfigPaths[]{
                RemovableConfigPaths.REQUIRE_PERMISSION
            }
        );
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (event.getEntity().getType() != EntityType.ENDERMAN) return;
        if (isDisabled()) return;
        event.setCancelled(true);
    }

}
