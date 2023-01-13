package dev.spimy.qoltweaks.modules.entities;

import dev.spimy.qoltweaks.modules.Module;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PreventPetDamage extends Module {

    public PreventPetDamage() {
        super();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPetHit(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (!(damager instanceof Player)) return;

        Entity victim = event.getEntity();
        if (!(victim instanceof Tameable)) return;

        if (isDisabled()) return;

        Tameable pet = (Tameable) victim;
        if (!pet.isTamed()) return;

        Player player = (Player) damager;
        if (isMissingPermission(player)) return;

        if (pet.getOwnerUniqueId() != player.getUniqueId()) return;
        event.setCancelled(true);
    }

}
