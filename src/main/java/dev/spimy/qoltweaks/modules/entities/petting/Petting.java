package dev.spimy.qoltweaks.modules.entities.petting;

import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.modules.Module;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;

@SuppressWarnings("unused")
public class Petting extends Module {

    private final String PET_DOG_PERM_KEY = "dog";
    private final String PET_CAT_PERM_KEY = "cat";
    private final QoLTweaks plugin = QoLTweaks.getInstance();

    public Petting() {
        super(
            new HashMap<>() {{
                put("cooldown.dog", 60);
                put("cooldown.cat", 60);
                put("heal", true);
                put("deaggro", true);
            }}
        );
        addPermission(PET_DOG_PERM_KEY, "pet-dog");
        addPermission(PET_CAT_PERM_KEY, "pet-cat");
    }

    @EventHandler
    public void onPetting(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (isDisabled()) return;

        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (player.getInventory().getItemInMainHand().getType() != Material.AIR || !player.isSneaking()) return;

        long cooldownTimer = 60;
        Sound petSound;

        if (entity.getType() == EntityType.WOLF) {
            if (isMissingPermission(player, PET_DOG_PERM_KEY) || isMissingDefaultPermission(player)) return;
            if (!((Wolf) entity).isSitting()) return;
            cooldownTimer = getConfigManager().getConfig().getLong("cooldown.dog", cooldownTimer);
            petSound = Sound.ENTITY_WOLF_WHINE;
        } else if (entity.getType() == EntityType.CAT) {
            if (isMissingPermission(player, PET_CAT_PERM_KEY) || isMissingDefaultPermission(player)) return;
            if (!((Cat) entity).isSitting()) return;
            cooldownTimer = getConfigManager().getConfig().getLong("cooldown.cat", cooldownTimer);
            petSound = Sound.ENTITY_CAT_PURREOW;
        } else return;

        Tameable pet = (Tameable) entity;
        PettingTimer pettingTimer = new PettingTimer(plugin, pet);

        if (!pettingTimer.canPet(cooldownTimer)) return;

        Location location = pet.getLocation();

        player.getWorld().spawnParticle(Particle.HEART, location.add(0, 0.5, 0), 1, 0, 0, 0, 0.1);
        player.getWorld().playSound(location, petSound, 1F, 0.5F + (float) Math.random() * 0.5F);
        player.swingMainHand();
        pettingTimer.setPetTime();


        if (getConfigManager().getConfig().getBoolean("heal")) {
            final AttributeInstance petMaxHealth = pet.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (petMaxHealth == null) {
                plugin.getLogger().warning("Unable to heal pet using petting function.");
                return;
            }

            if (pet.getHealth() < petMaxHealth.getValue()) {
                double health = pet.getHealth() + Math.random();
                if (health < 20) {
                    pet.setHealth(health);
                }
            }
        }

        if (getConfigManager().getConfig().getBoolean("deaggro")) {
            Entity target = pet.getTarget();
            if (target != null) {
                if (target.getType() == EntityType.PLAYER) {
                    pet.setTarget(null);
                }
            }
        }

        event.setCancelled(true);

    }

}
