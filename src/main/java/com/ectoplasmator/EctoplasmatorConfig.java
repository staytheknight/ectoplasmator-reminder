package com.ectoplasmator;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("example")
public interface EctoplasmatorConfig extends Config
{
	// Display Behaviour Section
	@ConfigSection(
		name = "Display Behaviour",
		description = "Various in game behaviours to control when the Ectoplasmator overlay is displayed",
		position = 0
	)
	String displayBehaviourSection = "Display Behaviour";

	@ConfigItem(
		position = 1,
		keyName = "hideWhileInInventory",
		name = "Hide overlay if in Inventory",
		description = "Hides the Ectoplasmator overlay if it's in player's inventory",
		section = displayBehaviourSection
	)
	default boolean hideIfInventory()
	{
		return true;
	}

	@ConfigItem(
		position = 2,
		keyName = "displayOnlyInCombat",
		name = "Display Only In Combat",
		description = "Display the Ectoplasmator overlay only while in combat",
		section = displayBehaviourSection
	)
	default boolean onlyInCombat()
	{
		return false;
	}

	@ConfigItem(
		position = 3,
		keyName = "hideInWilderness",
		name = "Hide Overlay in Wilderness",
		description = "Hide the Ectoplasmator overlay while in the wilderness",
		section = displayBehaviourSection
	)
	default boolean hideInWilderness()
	{
		return false;
	}

	// Location & Scale Section
	@ConfigSection(
		name = "Display Location & Scale",
		description = "Controls to change the size and vertical location of the Ectoplasmator overlay",
		position = 4
	)
	String displayControls = "Display Location & Scale";

	@ConfigItem(
		position = 5,
		keyName = "overlayVerticalOffset",
		name = "Overlay Vertical Offset",
		description = "How high above the NPCs he overlay is displayed, this is to customize location in case it's conflicting with the position of other addons.",
		section = displayControls
	)
	default int verticalOffset()
	{
		return 50;
	}

	@ConfigItem(
		position = 6,
		keyName = "overlayScale",
		name = "Overlay Scale",
		description = "Scale (size) of the Ectoplasmator overlay",
		section = displayControls
	)
	default int overlayScale()
	{
		return 1;
	}
}
