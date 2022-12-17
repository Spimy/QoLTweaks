package dev.spimy.qoltweaks.modules.enchanting;

import dev.spimy.qoltweaks.Modules;
import dev.spimy.qoltweaks.Permissions;
import dev.spimy.qoltweaks.QoLTweaks;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class BookExtract implements Listener {

    private final QoLTweaks plugin;

    public BookExtract(QoLTweaks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGrindstone(InventoryClickEvent event) {
        ConfigurationSection config = plugin.configManager.getModuleConfig(Modules.ENCHANTING);
        if (!config.getBoolean("book-extract.enabled")) return;

        if (!(event.getView().getTopInventory() instanceof GrindstoneInventory)) return;

        GrindstoneInventory inventory = (GrindstoneInventory) event.getView().getTopInventory();
        ItemStack cursor = event.getCursor();
        ItemStack clickedItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (config.getBoolean(("book-extract.require-permission"))) {
            if (!player.hasPermission(Permissions.BOOKEXTRACT.getPermissionNode())) return;
        }

        if (cursor == null) return;

        if (event.isShiftClick()) {
            if (clickedItem == null || clickedItem.getType() != Material.BOOK) return;
            event.setCancelled(true);

            if (event.getSlot() == 1) {
                player.getInventory().addItem(new ItemStack(Material.BOOK));
                inventory.setItem(1, new ItemStack(Material.AIR));
            } else {
                clickedItem.setAmount(clickedItem.getAmount() - 1);
                inventory.setItem(1, new ItemStack(Material.BOOK));
            }
        } else {
            if (event.getSlot() == 1 && cursor.getType() == Material.BOOK && inventory.getItem(1) == null) {
                event.setCancelled(true);
                cursor.setAmount(cursor.getAmount() - 1);
                inventory.setItem(1, new ItemStack(Material.BOOK));
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack extractFrom = inventory.getItem(0);
                ItemStack book = inventory.getItem(1);
                ItemStack extractedEnchants = inventory.getItem(2);

                if (extractFrom != null && book != null && extractedEnchants == null) {
                    if (book.getType() == Material.BOOK && book.getAmount() == 1 && !extractFrom.getEnchantments().isEmpty() && extractFrom.getType() != Material.ENCHANTED_BOOK) {
                        ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK);
                        EnchantmentStorageMeta enchantMeta = (EnchantmentStorageMeta) enchantedBook.getItemMeta();

                        for (Enchantment enchant : extractFrom.getEnchantments().keySet()) {
                            enchantMeta.addStoredEnchant(enchant, extractFrom.getEnchantmentLevel(enchant), false);
                            enchantedBook.setItemMeta(enchantMeta);
                        }

                        inventory.setItem(2, enchantedBook);
                    }
                }

            }
        }.runTaskLater(plugin, 2);

    }

}
