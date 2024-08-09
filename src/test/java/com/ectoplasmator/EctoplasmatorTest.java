package com.ectoplasmator;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class EctoplasmatorTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(EctoplasmatorPlugin.class);
		RuneLite.main(args);
	}
}