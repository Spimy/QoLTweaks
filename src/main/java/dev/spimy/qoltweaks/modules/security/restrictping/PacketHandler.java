package dev.spimy.qoltweaks.modules.security.restrictping;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.config.ConfigManager;

import java.util.HashSet;
import java.util.Set;

public class PacketHandler extends PacketAdapter {

    private final ConfigManager configManager;

    public PacketHandler(final ConfigManager configManager) {
        super(QoLTweaks.getInstance(), ListenerPriority.HIGHEST, PacketType.Handshake.Client.SET_PROTOCOL);
        this.configManager = configManager;
    }

    @Override
    public void onPacketReceiving(final PacketEvent event) {
        if (!configManager.getConfig().getBoolean("enabled")) return;

        final PacketContainer packet = event.getPacket();
        if (packet.getProtocols().read(0) != PacketType.Protocol.STATUS) return;

        final String hostname = packet.getStrings().read(0);
        final Set<String> whitelistedHostnames = new HashSet<>(plugin.getConfig().getStringList("allowed-hostnames"));

        if (whitelistedHostnames.contains(hostname.toLowerCase())) return;
        event.setCancelled(true);
    }

}
