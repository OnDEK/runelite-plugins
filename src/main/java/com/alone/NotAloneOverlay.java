package com.alone;

import com.google.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import java.awt.*;

public class NotAloneOverlay extends Overlay {
    private final Client client;
    private final AlonePlugin plugin;
    private final AloneConfig config;

    @Inject
    private NotAloneOverlay(Client client, AlonePlugin plugin, AloneConfig config) {
        super(plugin);
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (plugin.shouldRenderOverlay()) {
            Color color = graphics.getColor();
            graphics.setColor(config.overlayColor());
            graphics.fill(new Rectangle(client.getCanvas().getSize()));
            graphics.setColor(color);
        }
        return null;
    }
}
