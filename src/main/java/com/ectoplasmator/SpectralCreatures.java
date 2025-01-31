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

import lombok.Getter;
import java.net.*;
import java.io.*;
import java.util.*;

public abstract class SpectralCreatures
{
	// Ideally this would be by querying the npc type (spectral) but this can't be done with the runelite
	// API as it's considered cheating.
	// Instead, the wiki was used to make this list:
	// https://oldschool.runescape.wiki/w/Spectral_(attribute)

	@Getter
	static Set<Integer> SPECTRALCREATURES = new HashSet<Integer>();

	@Getter
    static Set<Integer> SPECTRALBOSSES = new HashSet<Integer>();

	// Reads a URL containing a plain text list of the spectral creatures
	// This text file is stored on a separate branch to prevent main branch from having to be pushed
	// every time a new creature is added.
	public static void FetchSpectralCreaturesLists() throws IOException {
		URL oracle = new URL("https://raw.githubusercontent.com/staytheknight/ectoplasmator-reminder/refs/heads/TextFiles/src/main/resources/Text%20Files/SpectralCreatures.txt");
		BufferedReader in = new BufferedReader(
				new InputStreamReader(oracle.openStream()));

		String inputLine;
		while ((inputLine = in.readLine()) != null)
		{
			int ID = Integer.parseInt(inputLine);
			SPECTRALCREATURES.add(ID);
		}

		oracle = new URL("https://raw.githubusercontent.com/staytheknight/ectoplasmator-reminder/refs/heads/TextFiles/src/main/resources/Text%20Files/SpectralBosses.txt");
		in = new BufferedReader(
				new InputStreamReader(oracle.openStream()));

		while ((inputLine = in.readLine()) != null)
		{
			int ID = Integer.parseInt(inputLine);
			SPECTRALBOSSES.add(ID);
		}
		in.close();
	}
}


