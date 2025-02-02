/*
 * Copyright (c) 2024, Elise Chevalier <https://github.com/staytheknight>
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
import okhttp3.*;

import javax.inject.Inject;
import java.io.*;
import java.util.*;

public class SpectralCreatures
{
	@Inject
	private OkHttpClient okClient;

	// Ideally this would be by querying the npc type (spectral) but this can't be done with the runelite
	// API as it's considered cheating.
	// Instead, the wiki was used to make this list:
	// https://oldschool.runescape.wiki/w/Spectral_(attribute)

	@Getter
	static Set<String> SPECTRALCREATURES = new HashSet<String>();

	@Getter
    static Set<String> SPECTRALBOSSES = new HashSet<String>();

	final static String S_CREATURE_URL = "https://raw.githubusercontent.com/staytheknight/ectoplasmator-reminder/refs/heads/TextFiles/src/main/resources/Text%20Files/SpectralCreatures.txt";
	final static String S_BOSS_URL = "https://raw.githubusercontent.com/staytheknight/ectoplasmator-reminder/refs/heads/TextFiles/src/main/resources/Text%20Files/SpectralBosses.txt";

	// Reads a URL containing a plain text list of the spectral creatures
	// This text file is stored on a separate branch to prevent main branch from having to be pushed
	// every time a new creature is added.
	public void FetchSpectralCreaturesLists() throws IOException
	{
		CallUrl(S_CREATURE_URL, SPECTRALCREATURES);
		CallUrl(S_BOSS_URL, SPECTRALBOSSES);
	}

	protected void CallUrl(String url, Set<String> set)
	{
		Request request = new Request.Builder()
				.url(url)
				.build();

		okClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Call call, Response response) throws IOException
			{
				assert response.body() != null;
				String s = response.body().string();
				response.body().close();

				// Sends the string to a processor to split each line into a Set
				ProcessStringInput(s, set);
			}

			@Override
			public void onFailure(Call call, IOException e)
			{
				System.out.println("Unable to read creature file");
				e.printStackTrace();
			}
		});
	}

	// Processes a string of names separated by a new line character into the chosen set
	static void ProcessStringInput(String string, Set<String> set)
	{
		String[] parts = string.split("\n");

		// NOTE: The string must be all lowercase, as the Overlay checks for lowercase string match
		for (String s : parts)
		{
			set.add(s.toLowerCase(Locale.ROOT));
		}
	}
}


