package dev.spimy.qoltweaks.modules.entities.petting;

import dev.spimy.qoltweaks.QoLTweaks;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Tameable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PettingTimer {

    private final Tameable tameable;
    private final PersistentDataContainer data;
    private final NamespacedKey key;

    public PettingTimer(final QoLTweaks plugin, final Tameable tameable) {
        this.tameable = tameable;
        this.data = tameable.getPersistentDataContainer();
        this.key = plugin.getKey("petting-timer");
    }

    public void setPetTime() {
        data.set(key, PersistentDataType.LONG, tameable.getWorld().getGameTime());
    }

    public boolean canPet(long cooldown) {
        return timeSinceLastPet() > cooldown;
    }

    @SuppressWarnings("ConstantConditions")
    public long getPetTime() {
        return data.has(key, PersistentDataType.LONG) ? data.get(key, PersistentDataType.LONG) : 0;
    }

    public long timeSinceLastPet() {
        if (!tameable.isTamed()) return 0;
        final long lastPetAt = getPetTime();
        return tameable.getWorld().getGameTime() - lastPetAt;
    }

}
