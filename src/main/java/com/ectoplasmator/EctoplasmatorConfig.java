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
}
