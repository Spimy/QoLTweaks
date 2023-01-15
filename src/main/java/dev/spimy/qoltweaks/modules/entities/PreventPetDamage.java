package dev.spimy.qoltweaks.modules.entities;

import dev.spimy.qoltweaks.modules.Module;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PreventPetDamage extends Module {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPetHit(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (!(damager instanceof Player player)) return;

        Entity victim = event.getEntity();
        if (!(victim instanceof Tameable pet)) return;

        if (isDisabled()) return;
        if (!pet.isTamed()) return;
        if (isMissingDefaultPermission(player)) return;

        if (pet.getOwner() == null) return;
        if (pet.getOwner().getUniqueId() != player.getUniqueId()) return;
        event.setCancelled(true);
    }

}
