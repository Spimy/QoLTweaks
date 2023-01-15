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

        if (!plugin.hasProtocolLib()) {
            plugin.getLogger().warning("Some modules were not loaded as they required ProtocolLib");
            return;
        }

        loadProtocolLibModules();
    }

    private void registerEvent(Module module) {
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
        this.registerEvent(new HostnameWhitelist());
        this.registerEvent(new AntiEndermanGrief());
    }

    public void loadProtocolLibModules() {
        this.registerEvent(new RestrictPing());
    }

}
