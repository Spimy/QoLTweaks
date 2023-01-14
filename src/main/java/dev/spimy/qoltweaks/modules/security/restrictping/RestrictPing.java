package dev.spimy.qoltweaks.modules.security.restrictping;

import dev.spimy.qoltweaks.config.RemovableConfigPaths;
import dev.spimy.qoltweaks.modules.Module;

public class RestrictPing extends Module {

    public RestrictPing() {
        super(
            true,
            new RemovableConfigPaths[]{
                RemovableConfigPaths.REQUIRE_PERMISSION
            }
        );

        assert getProtocolManager() != null;
        getProtocolManager().addPacketListener(new PacketHandler(getConfigManager()));
    }

}
