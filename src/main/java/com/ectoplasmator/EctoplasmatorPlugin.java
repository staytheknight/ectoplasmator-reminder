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

import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Ectoplasmator Reminder"
)
public class EctoplasmatorPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private EctoplasmatorConfig config;

	@Inject
	private EctoplasmatorOverlay overlay;

	@Getter(AccessLevel.PACKAGE)
	private final List<NPC> NPCTargets = new ArrayList<>();

	// Timer
	Timer outOfCombatTimer = new Timer("Timer");
	// Combat ends after 10 seconds (player is able to log)
	long outOfCombatDelay = 10000L; // 10000L = 10 seconds
	// The timer task that sets the combat statue to false
	TimerTask outOfCombatTask = null;

	// Function that will re-initialize the outOfCombatTask, as it gets canceled
	// from time to time which clears it's run() function
	public void outOfCombatTaskSetup()
	{
		outOfCombatTask = new TimerTask()
		{
			public void run()
			{
				overlay.setCombatStatus(false);
			}
		};
	}

	// Triggers when a hitsplat is detected (on player & on NPC from the player)
	@Subscribe
	public void onHitsplatApplied(HitsplatApplied hitSplat) throws InterruptedException
	{
		// If the hit splat is applied to the player or is from the player start the combat timer
		if (hitSplat.getActor() == client.getLocalPlayer() || hitSplat.getHitsplat().isMine())
		{
			// Cancels any previous out of combat timers
			outOfCombatTask.cancel();
			outOfCombatTimer.purge();
			// Re-initializes a new combat task, as .cancel() purges the run() function
			outOfCombatTaskSetup();
			// Sets the in combat statue to true
			overlay.setCombatStatus(true);
			// Start the combat timer
			outOfCombatTimer.schedule(outOfCombatTask, outOfCombatDelay);
		}

	}

	// When an NPC spawns, add it to the NPC targets list
	@Subscribe
	public void onNpcSpawned(NpcSpawned npcSpawned)
	{
		NPC npc = npcSpawned.getNpc();
		NPCTargets.add(npc);
	}

	// When an NPC despawned, remove it from the NPC targets list
	@Subscribe
	public void onNpcDespawned(NpcDespawned npcDespawned)
	{
		NPC npc = npcDespawned.getNpc();
		NPCTargets.remove(npc);
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
		overlay.revalidate();
		overlay.setCombatStatus(false);
		outOfCombatTaskSetup();
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		NPCTargets.clear();
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged configChanged)
	{
		if (configChanged.getKey().equals("overlayScale"))
		{
			overlay.scaleImage();
		}
	}

	@Provides
	EctoplasmatorConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(EctoplasmatorConfig.class);
	}


}
