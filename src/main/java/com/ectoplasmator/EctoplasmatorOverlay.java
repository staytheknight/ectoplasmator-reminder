package com.ectoplasmator;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Item;
import static net.runelite.api.ItemID.*;
import net.runelite.api.NPC;
import net.runelite.client.plugins.cluescrolls.clues.item.AnyRequirementCollection;
import net.runelite.client.plugins.cluescrolls.clues.item.ItemRequirement;
import static net.runelite.client.plugins.cluescrolls.clues.item.ItemRequirements.item;
import net.runelite.client.ui.overlay.Overlay;

class EctoplasmatorOverlay extends Overlay
{
	private final EctoplasmatorPlugin plugin;

	// Class Constructor
	@Inject
	private EctoplasmatorOverlay(EctoplasmatorPlugin plugin)
	{
		super(plugin);
		this.plugin=plugin;
	}

	// Check to see if Ectoplasmator is in the player's inventory
	private static final ItemRequirement HAS_ECTOPLASMATOR = new AnyRequirementCollection("Ectoplasmator",
		item(ECTOPLASMATOR));

	@Override
	public Dimension render(Graphics2D graphics)
	{
		final List<NPC> targets = plugin.getNPCTargets();

		// Gets all the items in the player's inventory
		final Item[] inventoryItems = plugin.getInventoryItems();

		// If the player does not have an Ectoplasmator in their inventory - then render overlay;
		if (!HAS_ECTOPLASMATOR.fulfilledBy(inventoryItems))
		{
		}
		return null;
	}
}
