package dev.spimy.qoltweaks.modules.entities;

import dev.spimy.qoltweaks.Modules;
import dev.spimy.qoltweaks.Permissions;
import dev.spimy.qoltweaks.QoLTweaks;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PreventPetDamage implements Listener {

    private final QoLTweaks plugin;

    public PreventPetDamage(QoLTweaks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPetHit(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (!(damager instanceof Player)) return;

        Entity victim = event.getEntity();
        if (!(victim instanceof Tameable)) return;

        ConfigurationSection config = plugin.configManager.getModuleConfig(Modules.ENTITIES);
        if (!config.getBoolean("prevent-pet-damage.enabled")) return;

        Tameable pet = (Tameable) victim;
        if (!pet.isTamed()) return;

        Player player = (Player) damager;
        if (config.getBoolean(("prevent-pet-damage.require-permission"))) {
            if (!player.hasPermission(Permissions.PREVENT_PET_DAMAGE.getPermissionNode())) return;
        }

        if (pet.getOwnerUniqueId() != player.getUniqueId()) return;
        event.setCancelled(true);
    }

}
