package dev.spimy.qoltweaks.modules.farming;

import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.modules.Module;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class HoeHarvest extends Module {

    public HoeHarvest() {
        super(
            new HashMap<>() {{
                put("range.wooden", 1);
                put("range.stone", 1);
                put("range.iron", 2);
                put("range.diamond", 2);
                put("range.netherite", 2);
                put(
                    "harvestable-materials",
                    new String[]{
                        "GRASS",
                        "TALL_GRASS",
                        "FLOWERS",
                        "CROPS"
                    }
                );
                put("require-sneaking", true);
            }}
        );
    }

    private int getRange(String itemType) {
        switch (itemType) {
            case "WOODEN_HOE":
                return getConfig().getInt("range.wooden");
            case "STONE_HOE":
                return getConfig().getInt("range.stone");
            case "IRON_HOE":
                return getConfig().getInt("range.iron");
            case "DIAMOND_HOE":
                return getConfig().getInt("range.diamond");
            case "NETHERITE_HOE":
                return getConfig().getInt("range.netherite");
            default:
                return 1;
        }
    }

    @EventHandler
    public void onHarvest(BlockBreakEvent event) {
        Player player = event.getPlayer();

        ItemStack item = player.getInventory().getItemInMainHand();
        Location brokenBlock = event.getBlock().getLocation();

        if (matchesHarvestable(event.getBlock().getType())) {
            String itemType = item.getType().toString();
            if (!itemType.endsWith("_HOE")) return;
            if (!player.isSneaking() && getConfig().getBoolean("require-sneaking")) return;
            if (isDisabled()) return;
            if (isMissingPermission(player)) return;

            int range = getRange(itemType);
            for (int x = brokenBlock.getBlockX() - range; x <= brokenBlock.getBlockX() + range; x++) {
                for (int z = brokenBlock.getBlockZ() - range; z <= brokenBlock.getBlockZ() + range; z++) {
                    Location location = new Location(brokenBlock.getWorld(), x, brokenBlock.getBlockY(), z);
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
            QoLTweaks.getInstance().getServer().getPluginManager().callEvent(damageEvent);

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
        return matchString(mat.toString(), getConfig().getStringList("harvestable-materials")) || matchTag(mat, getConfig().getStringList("harvestable-materials"));
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
