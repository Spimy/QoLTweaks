package dev.spimy.qoltweaks.modules.security.restrictping;

import com.comphenix.protocol.ProtocolLibrary;
import dev.spimy.qoltweaks.config.RemovableConfigPaths;
import dev.spimy.qoltweaks.modules.Module;

public class RestrictPing extends Module {

    public RestrictPing() {
        super(
            new RemovableConfigPaths[]{
                RemovableConfigPaths.REQUIRE_PERMISSION
            }
        );

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketHandler(getConfigManager()));
    }

}
