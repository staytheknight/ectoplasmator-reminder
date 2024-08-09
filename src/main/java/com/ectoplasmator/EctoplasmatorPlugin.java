package com.ectoplasmator;

import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.NPC;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
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

	// Getters for inventory items, the function is there as the @Getter is not being seeing in
	// EctoplasmatorOverlay.java
	@Getter
	private Item[] inventoryItems;

	public Item[] getInventoryItems()
	{
		return inventoryItems;
	}

	// Getter for target NPC to display overlay above
	@Getter(AccessLevel.PACKAGE)
	private final List<NPC> NPCTargets = new ArrayList<>();

	public List<NPC> getNPCTargets()
	{
		return NPCTargets;
	}

	// When an NPC spawns, add it to the NPC targets list
	@Subscribe
	public void onNpcSpawned(NpcSpawned npcSpawned)
	{
		NPC npc = npcSpawned.getNpc();
		// TODO: Add a conditional for spectral creatures
		NPCTargets.add(npc);
	}

	// When an NPC despawned, remove it from the NPC targets list
	@Subscribe
	public void onNpcDespawned(NpcDespawned npcDespawned)
	{
		NPC npc = npcDespawned.getNpc();
		NPCTargets.remove(npc);
	}

	// This gets called when a player picks up, drops, or takes an item from the bank
	@Subscribe
	public void onItemContainerChanged(final ItemContainerChanged event)
	{
		final ItemContainer itemContainer = event.getItemContainer();
		inventoryItems = itemContainer.getItems();
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);

		inventoryItems = null;
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
