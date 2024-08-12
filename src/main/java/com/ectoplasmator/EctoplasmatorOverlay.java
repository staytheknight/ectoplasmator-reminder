package com.ectoplasmator;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemID;
import static net.runelite.api.ItemID.*;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.Varbits;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

class EctoplasmatorOverlay extends Overlay
{
	@Inject
	private EctoplasmatorConfig config;

	private final Client client;
	private final EctoplasmatorPlugin plugin;
	private final ItemManager itemManager;

	// Display variables
	private boolean combatStatus;
	private List<Boolean> displayChecks = new ArrayList<>();    // Boolean array of display toggles
	private boolean displayToggle;                                // Final display toggle boolean

	// Class Constructor
	@Inject
	private EctoplasmatorOverlay(Client client, EctoplasmatorPlugin plugin, ItemManager itemManager)
	{
		this.client = client;
		this.plugin = plugin;
		this.itemManager = itemManager;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.UNDER_WIDGETS);

		// sets default render toggle to be true
		displayToggle = true;
	}

	public void setCombatStatus(boolean combatStatus)
	{
		this.combatStatus = combatStatus;
	}

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

		// Clears the boolean array on each render loop
		if (!displayChecks.isEmpty())
		{
			displayChecks.clear();
		}

		// Adds different display toggles based on configuration settings to the display check boolean array
		if (config.hideIfInventory())
		{
			displayChecks.add(!client.getItemContainer(InventoryID.INVENTORY).contains(ECTOPLASMATOR));
		}
		if (config.onlyInCombat())
		{
			displayChecks.add(combatStatus);
		}
		if (config.hideInWilderness())
		{
			displayChecks.add(client.getVarbitValue(Varbits.IN_WILDERNESS) != 1);
		}

		// Iterates through the display check boolean array to see if any of the booleans are false
		// If they are false set the master toggle to false and break
		for (boolean b : displayChecks)
		{
			if (!b)
			{
				displayToggle = false;
				break;
			}
			else
			{
				displayToggle = true;
			}
		}

		// Catch if none of the config settings are checked - default behaviour is to display
		if (displayChecks.isEmpty())
		{
			displayToggle = true;
		}

		// If the master display toggle is true, display the overlay
		if (displayToggle)
		{
			renderOverlay(targets, graphics, image);
		}

		return null;
	}

	// Iterates through all the NPC targets, and if they are a spectral creature, render the overlay
	private void renderOverlay(List<NPC> targets, Graphics2D graphics, BufferedImage image)
	{
		for (NPC target : targets)
		{
			// Checks if the target is a spectral creature
			if (SpectralCreatures.SPECTRALCREATURES.contains(target.getId()))
			{
				renderTargetItem(graphics, target, image);
			}
		}
	}

	// Code snippet taken from:
	// runelite/client/plugins/slayer/TargetWeaknessOverlay.java
	// Altered to have a modifiable height adjustment through config
	private void renderTargetItem(Graphics2D graphics, NPC actor, BufferedImage image)
	{
		final LocalPoint actorPosition = actor.getLocalLocation();
		final int offset = actor.getLogicalHeight() + config.verticalOffset();

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
