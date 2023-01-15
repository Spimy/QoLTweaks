package dev.spimy.qoltweaks.modules.enchanting;

import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.modules.Module;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class BookExtract extends Module {

    public BookExtract() {
        super(false);
    }

    @EventHandler
    public void onGrindstone(InventoryClickEvent event) {
        if (isDisabled()) return;

        if (!(event.getView().getTopInventory() instanceof GrindstoneInventory inventory)) return;

        ItemStack cursor = event.getCursor();
        ItemStack clickedItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (isMissingDefaultPermission(player)) return;
        if (cursor == null) return;

        if (event.isShiftClick()) {
            if (clickedItem == null || clickedItem.getType() != Material.BOOK) return;
            if (event.getSlot() == 2) return;
            event.setCancelled(true);

            Inventory clickedInventory = event.getClickedInventory();
            if (clickedInventory == null) return;

            if (clickedInventory.getType() == InventoryType.GRINDSTONE) {
                player.getInventory().addItem(new ItemStack(Material.BOOK));
                inventory.setItem(1, new ItemStack(Material.AIR));

            } else if (clickedInventory.getType() == InventoryType.PLAYER) {
                ItemStack item = inventory.getItem(1);
                if (item != null && item.getType() != Material.AIR) return;

                clickedItem.setAmount(clickedItem.getAmount() - 1);
                inventory.setItem(1, new ItemStack(Material.BOOK));

            } else return;
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

                        if (enchantMeta != null) {
                            for (Enchantment enchant : extractFrom.getEnchantments().keySet()) {
                                enchantMeta.addStoredEnchant(enchant, extractFrom.getEnchantmentLevel(enchant), false);
                                enchantedBook.setItemMeta(enchantMeta);
                            }
                        }

                        inventory.setItem(2, enchantedBook);
                    }
                }

            }
        }.runTaskLater(QoLTweaks.getInstance(), 2);

    }

}
