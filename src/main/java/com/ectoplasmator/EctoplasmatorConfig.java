package com.ectoplasmator;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface EctoplasmatorConfig extends Config
{
	@ConfigItem(
		position = 1,
		keyName = "hideWhileInInventory",
		name = "Hide overlay if in Inventory",
		description = "Hides the Ectoplasmator overlay if it's in player's inventory"
	)
	default boolean hideIfInventory()
	{
		return true;
	}

	@ConfigItem(
		position = 2,
		keyName = "displayOnlyInCombat",
		name = "Display Only In Combat",
		description = "Display the Ectoplasmator overlay only while in combat"
	)
	default boolean onlyInCombat()
	{
		return false;
	}

	@ConfigItem(
		position = 3,
		keyName = "hideInWilderness",
		name = "Hide Overlay in Wilderness",
		description = "Hide the Ectoplasmator overlay while in the wilderness"
	)
	default boolean hideInWilderness() { return false; }

	@ConfigItem(
		position = 4,
		keyName = "overlayVerticalOffset",
		name = "Overlay Vertical Offset",
		description = "How high above the NPCs the overlay is displayed, this is to customize location in case it's conflicting with the position of other addons."
	)
	default int verticalOffset()
	{
		return 50;
	}
}
