package dev.spimy.qoltweaks.modules.entities;

import dev.spimy.qoltweaks.Modules;
import dev.spimy.qoltweaks.Permissions;
import dev.spimy.qoltweaks.QoLTweaks;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NametagShear implements Listener {

    private final QoLTweaks plugin;

    public NametagShear(QoLTweaks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNameTagShear(PlayerInteractAtEntityEvent event) {
        if (event.isCancelled()) return;

        ConfigurationSection config = plugin.configManager.getModuleConfig(Modules.ENTITIES);
        if (!config.getBoolean("nametag-shear.enabled")) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getHand());
        Entity entity = event.getRightClicked();

        if (!(entity instanceof LivingEntity)) return;
        if (item.getType() != Material.SHEARS) return;
        if (!player.isSneaking()) return;

        if (config.getBoolean(("nametag-shear.require-permission"))) {
            if (!player.hasPermission(Permissions.NAMETAG_SHEAR.getPermissionNode())) return;
        }

        event.setCancelled(true);

        ItemStack nameTag = new ItemStack(Material.NAME_TAG);
        ItemMeta nameTagMeta = nameTag.getItemMeta();

        nameTagMeta.displayName(entity.customName());
        nameTag.setItemMeta(nameTagMeta);

        if (event.getHand() == EquipmentSlot.HAND) {
            player.swingMainHand();
        } else {
            player.swingOffHand();
        }

        player.getWorld().dropItemNaturally(entity.getLocation(), nameTag);
        player.getWorld().playSound(entity.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1, 1);

        if (entity instanceof ArmorStand){
            entity.setCustomNameVisible(false);
        }

        entity.customName(null);
    }

}
