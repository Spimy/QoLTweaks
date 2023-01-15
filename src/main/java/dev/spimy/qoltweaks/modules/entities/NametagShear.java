package dev.spimy.qoltweaks.modules.entities;

import dev.spimy.qoltweaks.modules.Module;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NametagShear extends Module {

    public NametagShear() {
        super(false);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNameTagShear(PlayerInteractAtEntityEvent event) {
        if (event.isCancelled()) return;
        if (isDisabled()) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getHand());
        Entity entity = event.getRightClicked();

        if (!(entity instanceof LivingEntity)) return;
        if (item == null) return;
        if (item.getType() != Material.SHEARS) return;
        if (!player.isSneaking()) return;

        if (isMissingDefaultPermission(player)) return;

        event.setCancelled(true);

        ItemStack nameTag = new ItemStack(Material.NAME_TAG);
        ItemMeta nameTagMeta = nameTag.getItemMeta();

        if (nameTagMeta != null) nameTagMeta.setDisplayName(entity.getCustomName());
        nameTag.setItemMeta(nameTagMeta);

        if (event.getHand() == EquipmentSlot.HAND) {
            player.swingMainHand();
        } else {
            player.swingOffHand();
        }

        player.getWorld().dropItemNaturally(entity.getLocation(), nameTag);
        player.getWorld().playSound(entity.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1, 1);

        if (entity instanceof ArmorStand) {
            entity.setCustomNameVisible(false);
        }

        entity.setCustomName(null);
    }

}
