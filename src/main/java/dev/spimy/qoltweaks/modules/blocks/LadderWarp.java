package dev.spimy.qoltweaks.modules.blocks;

import dev.spimy.qoltweaks.modules.Module;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

public class LadderWarp extends Module {

    public LadderWarp() {
        super(false);
    }

    @EventHandler
    public void onLadderClick(PlayerInteractEvent event) {
        if (isDisabled()) return;

        Player player = event.getPlayer();

        if (event.getItem() != null) return;
        if (!player.isSneaking()) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() != Material.LADDER) return;

        if (isMissingPermission(player)) return;

        Block ladder = event.getClickedBlock();
        Vector face = player.getEyeLocation().getDirection().clone();

        if (ladder.getRelative(BlockFace.DOWN).getType() != Material.LADDER) {
            Block topBlock = getFirstNonLadderBlock(ladder, BlockFace.UP);
            if (topBlock != null) {
                if (topBlock.getRelative(BlockFace.UP).getType() == Material.AIR) {
                    player.teleport(topBlock.getLocation().setDirection(face).subtract(-0.5, 1, -0.5));
                    player.playSound(player.getLocation(), Sound.BLOCK_LADDER_STEP, 1, 1);
                }
            }
        } else if (ladder.getRelative(BlockFace.UP).getType() != Material.LADDER) {
            Block downBlock = getFirstNonLadderBlock(ladder, BlockFace.DOWN);
            if (downBlock != null) {
                player.teleport(downBlock.getLocation().setDirection(face).add(0.5, 1, 0.5));
                player.playSound(player.getLocation(), Sound.BLOCK_LADDER_STEP, 1, 1);
            }
        }
    }

    private Block getFirstNonLadderBlock(Block block, BlockFace direction) {
        Block upBlock = block.getRelative(direction);
        if (upBlock.getType() == Material.LADDER) {
            return getFirstNonLadderBlock(upBlock, direction);
        } else if (upBlock.getType() == Material.AIR) {
            return upBlock;
        } else if (direction == BlockFace.DOWN) {
            return upBlock;
        }

        return null;
    }

}
