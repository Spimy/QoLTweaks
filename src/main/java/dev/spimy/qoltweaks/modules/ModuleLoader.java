package dev.spimy.qoltweaks.modules;

import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.modules.blocks.LadderWarp;
import dev.spimy.qoltweaks.modules.blocks.TntDropRate;
import dev.spimy.qoltweaks.modules.enchanting.BookExtract;
import dev.spimy.qoltweaks.modules.entities.AntiEndermanGrief;
import dev.spimy.qoltweaks.modules.entities.NametagShear;
import dev.spimy.qoltweaks.modules.entities.PreventPetDamage;
import dev.spimy.qoltweaks.modules.entities.petting.Petting;
import dev.spimy.qoltweaks.modules.farming.HoeHarvest;
import dev.spimy.qoltweaks.modules.security.HostnameWhitelist;
import dev.spimy.qoltweaks.modules.security.restrictping.RestrictPing;

public class ModuleLoader {

    private final QoLTweaks plugin = QoLTweaks.getInstance();

    public ModuleLoader() {
        loadModules();
    }

    private void registerEvent(Module module) {
        if (module.requireProtocolLib()) {
            if (!plugin.hasProtocolLib()) {
                plugin.getLogger().warning("Could not enable " + module.getName() + " module as it requires ProtocolLib.");
                return;
            }
        }
        plugin.getServer().getPluginManager().registerEvents(module, plugin);
    }

    private void loadModules() {
        this.registerEvent(new LadderWarp());
        this.registerEvent(new BookExtract());
        this.registerEvent(new NametagShear());
        this.registerEvent(new Petting());
        this.registerEvent(new PreventPetDamage());
        this.registerEvent(new HoeHarvest());
        this.registerEvent(new TntDropRate());
        this.registerEvent(new RestrictPing());
        this.registerEvent(new HostnameWhitelist());
        this.registerEvent(new AntiEndermanGrief());
    }

}
