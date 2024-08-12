/*
 * Copyright (c) 2024, Elise Chevaier <https://github.com/staytheknight>
 * <https://elisechevalier.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.ectoplasmator;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import static java.awt.Image.SCALE_DEFAULT;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
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
	private BufferedImage image;                                // Image variable the render loop uses
	private BufferedImage baseImage;                            // Default image information used for scaling
	private boolean combatStatus;                                // Combat status of player
	private List<Boolean> displayChecks = new ArrayList<>();    // Boolean array of display toggles
	private boolean displayToggle;                                // Final display toggle boolean
	private int previousDisplayScale = 1;

	// Class Constructor
	@Inject
	private EctoplasmatorOverlay(Client client, EctoplasmatorPlugin plugin, ItemManager itemManager)
	{
		this.client = client;
		this.plugin = plugin;
		this.itemManager = itemManager;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.UNDER_WIDGETS);

		// Setting defaults for image variables
		baseImage = itemManager.getImage(ItemID.ECTOPLASMATOR);
		image = baseImage;

		// sets default render toggle to be true
		displayToggle = true;
	}

	// This is called in the EctoplasmatorPlugin.java to change the combat status
	public void setCombatStatus(boolean combatStatus)
	{
		this.combatStatus = combatStatus;
	}

	// Casts an Image to BufferedImage
	public BufferedImage imageToBufferedImage(Image image)
	{
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		BufferedImage bufferedImage = new BufferedImage(width, height, TYPE_INT_ARGB);
		bufferedImage.getGraphics().drawImage(image, 0, 0, null);
		return bufferedImage;
	}

	// Scales the base image to user configuration
	public void scaleImage()
	{
		image = imageToBufferedImage(baseImage.getScaledInstance(baseImage.getWidth() * config.overlayScale(),
			baseImage.getHeight() * config.overlayScale(),
			SCALE_DEFAULT));
		previousDisplayScale = config.overlayScale();
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
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
