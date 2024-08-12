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

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("Ectoplasmator Reminder Configs")
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
