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
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
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

	Timer timer = new Timer("Timer");

	// Triggers when a hitsplat is detected (on player & on NPC from the player)
	@Subscribe
	public void onHitsplatApplied(HitsplatApplied hitSplat) throws InterruptedException
	{
		// Sets the in combat statue to true, and calls the combat timer
		overlay.setCombatStatus(true);
		outOfCombat();
	}

	// Combat timer sets the combat statue to false after 10 seconds
	public void outOfCombat() throws InterruptedException
	{
		TimerTask task = new TimerTask()
		{
			public void run()
			{
				overlay.setCombatStatus(false);
			}
		};
		long delay = 10000L; // 10000L = 10 seconds
		timer.schedule(task, delay);
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
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		NPCTargets.clear();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}

	@Provides
	EctoplasmatorConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(EctoplasmatorConfig.class);
	}


}
