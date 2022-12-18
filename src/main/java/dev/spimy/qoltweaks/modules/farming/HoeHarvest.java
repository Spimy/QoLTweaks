package dev.spimy.qoltweaks.modules.farming;

import dev.spimy.qoltweaks.Modules;
import dev.spimy.qoltweaks.Permissions;
import dev.spimy.qoltweaks.QoLTweaks;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;

public class HoeHarvest implements Listener {

    private final QoLTweaks plugin;

    public HoeHarvest(QoLTweaks plugin) {
        this.plugin = plugin;
    }

    private int getRange(String itemType) {
        ConfigurationSection config = plugin.configManager.getModuleConfig(Modules.FARMING);

        switch (itemType) {
            case "WOODEN_HOE":
                return config.getInt("hoe-harvest.range.wooden");
            case "STONE_HOE":
                return config.getInt("hoe-harvest.range.stone");
            case "IRON_HOE":
                return config.getInt("hoe-harvest.range.iron");
            case "DIAMOND_HOE":
                return config.getInt("hoe-harvest.range.diamond");
            case "NETHERITE_HOE":
                return config.getInt("hoe-harvest.range.netherite");
            default:
                return 1;
        }
    }

    @EventHandler
    public void onHarvest(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ConfigurationSection config = plugin.configManager.getModuleConfig(Modules.FARMING);

        if (!player.isSneaking() && config.getBoolean("hoe-harvest.require-sneaking")) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        Location breakenBlock = event.getBlock().getLocation();

        if (matchesHarvestable(event.getBlock().getType())) {
            String itemType = item.getType().toString();
            if (!itemType.endsWith("_HOE")) return;

            if (!config.getBoolean("hoe-harvest.enabled")) return;
            if (config.getBoolean(("hoe-harvest.require-permission"))) {
                if (!player.hasPermission(Permissions.HOE_HARVEST.getPermissionNode())) return;
            }

            int range = getRange(itemType);
            for (int x = breakenBlock.getBlockX() - range; x <= breakenBlock.getBlockX() + range; x++) {
                for (int z = breakenBlock.getBlockZ() - range; z <= breakenBlock.getBlockZ() + range; z++) {
                    Location location = new Location(breakenBlock.getWorld(), x, breakenBlock.getBlockY(), z);
                    Material blockType = location.getBlock().getType();

                    if (matchesHarvestable(blockType)) {
                        location.getBlock().breakNaturally(item);
                        if (player.getGameMode() == GameMode.CREATIVE) continue;
                        damageItem(1, item, player);
                    }
                }
            }
        }
    }

    private void damageItem(int amount, ItemStack item, Player player) {
        ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof Damageable) || amount < 0) return;

        int m = item.getEnchantmentLevel(Enchantment.DURABILITY);
        int k = 0;

        Random random = new Random();
        for (int l = 0; m > 0 && l < amount; l++) {
            if (random.nextInt(m + 1) > 0) {
                k++;
            }
        }
        amount -= k;

        if (player != null) {
            PlayerItemDamageEvent damageEvent = new PlayerItemDamageEvent(player, item, amount);
            plugin.getServer().getPluginManager().callEvent(damageEvent);

            if (amount != damageEvent.getDamage() || damageEvent.isCancelled()) {
                damageEvent.getPlayer().updateInventory();
            } else if (damageEvent.isCancelled()) return;

            amount = damageEvent.getDamage();
        }
        if (amount <= 0) return;

        Damageable damageable = (Damageable) meta;
        damageable.setDamage(damageable.getDamage() + amount);
        item.setItemMeta(meta);
    }

    private boolean matchesHarvestable(Material mat) {
        ConfigurationSection config = plugin.configManager.getModuleConfig(Modules.FARMING);
        return matchString(mat.toString(), config.getStringList("hoe-harvest.harvestable-materials")) || matchTag(mat, config.getStringList("hoe-harvest.harvestable-materials"));
    }

    private boolean matchString(String str, List<String> matcher) {
        for (String s : matcher) {
            if (s.startsWith("^") && str.startsWith(s.replace("^", ""))) {
                return true;
            }

            if (s.endsWith("$") && str.endsWith(s.replace("$", ""))) {
                return true;
            }

            if (str.equals(s)) return true;
        }
        return false;
    }

    private boolean matchTag(Material mat, List<String> matcher) {
        for (String s : matcher) {
            Tag<Material> tag = Bukkit.getTag("blocks", NamespacedKey.minecraft(s.toLowerCase()), Material.class);
            if (tag != null && tag.getValues().contains(mat)) return true;
        }
        return false;
    }

}
