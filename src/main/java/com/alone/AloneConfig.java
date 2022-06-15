package com.alone;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.*;

@ConfigGroup("example")
public interface AloneConfig extends Config
{
	@Alpha
	@ConfigItem(
			keyName = "overlayColor",
			name = "Notification color",
			description = "Set the notification overlay color",
			position = 1
	)
	default Color overlayColor() {
		return new Color(0.0f, 0.0f, 0.0f, 1.0f);
	}
// TODO
//	@ConfigItem(
//			keyName = "FRIENDS",
//			name = "Allow Friends",
//			description = "Prevents friends list players from forcing you to close your eyes",
//			position = 2
//	)
//	default boolean friends() {
//		return false;
//	}

	@ConfigItem(
			keyName = "NUM_PLAYERS",
			name = "Allowed Players",
			description = "The number of players allowed on screen before you close your eyes (excluding yourself)",
			position = 2
	)
	default int numPlayers() {
		return 0;
	}

}
