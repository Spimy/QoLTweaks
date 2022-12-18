package dev.spimy.qoltweaks.modules.entities.petting;

import dev.spimy.qoltweaks.Modules;
import dev.spimy.qoltweaks.Permissions;
import dev.spimy.qoltweaks.QoLTweaks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class Petting implements Listener {

    private final QoLTweaks plugin;

    public Petting(QoLTweaks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPetting(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        ConfigurationSection config = plugin.configManager.getModuleConfig(Modules.ENTITIES);
        if (!config.getBoolean("petting.enabled")) return;

        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (player.getInventory().getItemInMainHand().getType() != Material.AIR || !player.isSneaking()) return;
        if (entity.getType() != EntityType.WOLF && entity.getType() != EntityType.CAT) return;

        long cooldownTimer = 20;
        Sound petSound = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

        if (entity.getType() == EntityType.WOLF) {
            if (config.getBoolean(("petting.require-permission.pet-dog"))) {
                if (!player.hasPermission(Permissions.PET_DOG.getPermissionNode())) return;
            }
            if (!((Wolf) entity).isSitting()) return;
            cooldownTimer = config.getLong("petting.cooldown.dog", cooldownTimer);
            petSound = Sound.ENTITY_WOLF_WHINE;
        }

        if (entity.getType() == EntityType.CAT) {
            if (config.getBoolean(("petting.require-permission.pet-cat"))) {
                if (!player.hasPermission(Permissions.PET_CAT.getPermissionNode())) return;
            }
            if (!((Cat) entity).isSitting()) return;
            cooldownTimer = config.getLong("petting.cooldown.cat", cooldownTimer);
            petSound = Sound.ENTITY_CAT_PURREOW;
        }

        Tameable pet = (Tameable) entity;
        PettingTimer pettingTimer = new PettingTimer(plugin, pet);

        if (!pettingTimer.canPet(cooldownTimer)) return;

        Location location = pet.getLocation();

        player.getWorld().spawnParticle(Particle.HEART, location.add(0, 0.5, 0), 1, 0, 0, 0, 0.1);
        player.getWorld().playSound(location, petSound, 1F, 0.5F + (float) Math.random() * 0.5F);
        player.swingMainHand();
        pettingTimer.setPetTime();

        if (config.getBoolean("petting.heal") && pet.getHealth() < pet.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
            double health = pet.getHealth() + Math.random();
            if (health < 20) {
                pet.setHealth(health);
            }
        }

        event.setCancelled(true);

    }

}
