/*
 * Copyright (c) by Valaphee 2019.
 *
 * Licensed under the 4-clause BSD license (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      https://deploy.valaphee.com/license/BSD-4-Clause.txt
 *
 * THIS SOFTWARE IS PROVIDED BY VALAPHEE "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.
 */

package com.valaphee.cyclone.text;

/**
 * Default
 *
 * @author valaphee
 */
public final class MinecraftJSONTextTranscoder
		implements TextTranscoder<MinecraftJSONComponent>
{
	@Override
	public MinecraftJSONComponent transcode(final String text)
	{
		final MinecraftJSONComponent transcodedComponent = new MinecraftJSONComponent();
		boolean sequence = false;

		for (int i = 0; i < text.length(); ++i)
		{
			final char character = text.charAt(i);

			if (character == '`')
			{
				sequence = !sequence;
			}
			else if (sequence)
			{
				final TextAttribute attribute = TextAttribute.getAttribute(character);
				if (attribute != null)
				{

				}
			}
		}

		return transcodedComponent;
	}
}
