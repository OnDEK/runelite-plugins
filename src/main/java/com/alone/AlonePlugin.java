package com.alone;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.PlayerDespawned;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@PluginDescriptor(
	name = "Alone Man Mode"
)
public class AlonePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private AloneConfig config;

	@Inject
	OverlayManager overlayManager;

	@Inject
	NotAloneOverlay overlay;

	ThreadPoolExecutor executor;

	private AtomicInteger players;
	private AtomicBoolean alone;

	@Override
	protected void startUp() throws Exception {
		executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		players = new AtomicInteger(0);
		alone = new AtomicBoolean(true);
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception {
		overlayManager.remove(overlay);
		executor.shutdown();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged) {
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN) {
			players.set(0);
			alone.set(true);
		}
	}

	@Subscribe
	public void onPlayerSpawned(PlayerSpawned event) {
		if (event.getPlayer().getId() == client.getLocalPlayer().getId()) return;
		// TODO: Add in friends not counting when config is checked.
//		if (config.friends()) {
//			if(Arrays.stream(client.getFriendContainer().getMembers()).anyMatch(
//					friend -> (friend.equals(event.getPlayer()))))
//				return;
//		}
		executor.execute(() -> {
			if (players.incrementAndGet() > config.numPlayers()) {
				alone.set(false);
			} else {
				alone.set(true);
			}
		});
	}

	@Subscribe
	public void onPlayerDespawned(PlayerDespawned event) {
		if (event.getPlayer().getId() == client.getLocalPlayer().getId()) return;
		executor.execute(() -> {
			if (players.get() != 0 && players.decrementAndGet() > config.numPlayers()) {
				alone.set(false);
			} else {
				alone.set(true);
			}
		});
	}

	@Provides
	AloneConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AloneConfig.class);
	}

	boolean shouldRenderOverlay() {
		return !alone.get();
	}
}
