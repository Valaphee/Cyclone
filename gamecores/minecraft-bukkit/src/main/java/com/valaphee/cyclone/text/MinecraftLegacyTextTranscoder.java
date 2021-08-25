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

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Default
 *
 * @author valaphee
 */
public final class MinecraftLegacyTextTranscoder
		implements TextTranscoder<String>
{
	@Override
	public String transcode(final String text)
	{
		final StringBuffer transcodedText = new StringBuffer();
		final Stack<TextAttribute> attributeStack = new Stack<>();
		final Set<TextAttribute> attributeFlags = new HashSet<>();
		boolean sequence = false;

		for (int i = 0; i < text.length(); ++i)
		{
			final char character = text.charAt(i);

			if (character == '`')
			{
				if (sequence)
				{
					if (!attributeStack.isEmpty())
					{
						transcodedText.append('\u00A7');
						transcodedText.append(attributeStack.peek().getCharacter());
					}
					else
					{
						transcodedText.append('\u00A7');
						transcodedText.append('r');
					}
					attributeFlags.stream().forEach((attributeFlag) ->
					{
						transcodedText.append('\u00A7');
						transcodedText.append(attributeFlag.getCharacter());
					});
				}

				sequence = !sequence;
			}
			else if (sequence)
			{
				final TextAttribute attribute = TextAttribute.getAttribute(character);
				if (attribute != null)
				{
					if (attribute.isStackable())
					{
						if (attributeStack.isEmpty() || (!attribute.equals(attributeStack.peek())))
						{
							attributeStack.push(attribute);
						}
						else
						{
							attributeStack.pop();
						}
					}
					else
					{
						if (!attributeFlags.contains(attribute))
						{
							attributeFlags.add(attribute);
						}
						else
						{
							attributeFlags.remove(attribute);
						}
					}
				}
			}
			else
			{
				transcodedText.append(character);

				if (character == '\u00A7')
				{
					if (!attributeStack.isEmpty())
					{
						transcodedText.append('\u00A7');
						transcodedText.append(attributeStack.peek().getCharacter());
					}
					else
					{
						transcodedText.append('\u00A7');
						transcodedText.append('r');
					}
				}
			}
		}

		return transcodedText.toString();
	}
}
