package com.ectoplasmator;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import static net.runelite.api.ItemID.*;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.cluescrolls.clues.item.AnyRequirementCollection;
import net.runelite.client.plugins.cluescrolls.clues.item.ItemRequirement;
import static net.runelite.client.plugins.cluescrolls.clues.item.ItemRequirements.item;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

class EctoplasmatorOverlay extends Overlay
{
	private final Client client;
	private final EctoplasmatorPlugin plugin;
	private final ItemManager itemManager;

	// Class Constructor
	@Inject
	private EctoplasmatorOverlay(Client client, EctoplasmatorPlugin plugin, ItemManager itemManager)
	{
		this.client = client;
		this.plugin = plugin;
		this.itemManager = itemManager;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.UNDER_WIDGETS);
	}

	// Check to see if Ectoplasmator is in the player's inventory
	private static final ItemRequirement HAS_ECTOPLASMATOR = new AnyRequirementCollection("Ectoplasmator",
		item(ECTOPLASMATOR));

	@Override
	public Dimension render(Graphics2D graphics)
	{
		// Gets the image for the Ectoplasmator to be rendered
		final BufferedImage image = itemManager.getImage(ItemID.ECTOPLASMATOR);
		// Error Catch if the image is null
		if (image == null)
		{
			return null;
		}

		// Gets list of NPCS to render the image above
		final List<NPC> targets = plugin.getNPCTargets();
		// Error catch for if the npc target list is empty
		if (targets.isEmpty())
		{
			return null;
		}

		// Gets all the items in the player's inventory
		final Item[] inventoryItems = plugin.getInventoryItems();

		// If the player does not have an Ectoplasmator in their inventory - then render overlay;
		if (HAS_ECTOPLASMATOR.fulfilledBy(inventoryItems))
		{
			for (NPC target : targets)
			{
				renderTargetItem(graphics, target, image);
			}

		}
		return null;
	}

	// Code snippet taken from:
	// runelite/client/plugins/slayer/TargetWeaknessOverlay.java
	// Altered to have a modifiable height adjustment through config
	private void renderTargetItem(Graphics2D graphics, NPC actor, BufferedImage image)
	{
		final LocalPoint actorPosition = actor.getLocalLocation();
		// TODO: set +40 offset to config
		final int offset = actor.getLogicalHeight() + 40;

		if (actorPosition == null || image == null)
		{
			return;
		}

		final Point imageLoc = Perspective.getCanvasImageLocation(client, actorPosition, image, offset);

		if (imageLoc != null)
		{
			OverlayUtil.renderImageLocation(graphics, imageLoc, image);
		}
	}
}
