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

import java.util.*;

/**
 * Default
 *
 * @author valaphee
 */
public final class BBCodeTextTranscoder
		implements TextTranscoder<String>
{
	private static final Map<TextAttribute, CharSequence> BBCODE_COLORS = new HashMap<>();
	private static final Map<TextAttribute, CharSequence> BBCODES = new HashMap<>();

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

							transcodedText.append("[color=").append(BBCODE_COLORS.get(attribute)).append(']');
						}
						else
						{
							attributeStack.pop();

							transcodedText.append("[/color]");
						}
					}
					else
					{
						if (!attributeFlags.contains(attribute))
						{
							attributeFlags.add(attribute);

							transcodedText.append('[').append(BBCODES.get(attribute)).append(']');
						}
						else
						{
							attributeFlags.remove(attribute);

							transcodedText.append('[').append('/').append(BBCODES.get(attribute)).append(']');
						}
					}
				}
			}
			else
			{
				transcodedText.append(character);
			}
		}

		return transcodedText.toString();
	}

	static
	{
		BBCODE_COLORS.put(TextAttribute.BLACK, "black");
		BBCODE_COLORS.put(TextAttribute.BLUE, "blue");
		BBCODE_COLORS.put(TextAttribute.GREEN, "green");
		BBCODE_COLORS.put(TextAttribute.CYAN, "cyan");
		BBCODE_COLORS.put(TextAttribute.RED, "red");
		BBCODE_COLORS.put(TextAttribute.MAGENTA, "magenta");
		BBCODE_COLORS.put(TextAttribute.BROWN, "goldenrod");
		BBCODE_COLORS.put(TextAttribute.LIGHT_GRAY, "lightgray");
		BBCODE_COLORS.put(TextAttribute.DARK_GRAY, "darkgray");
		BBCODE_COLORS.put(TextAttribute.LIGHT_BLUE, "lightblue");
		BBCODE_COLORS.put(TextAttribute.LIGHT_GREEN, "lightgreen");
		BBCODE_COLORS.put(TextAttribute.LIGHT_CYAN, "lightcyan");
		BBCODE_COLORS.put(TextAttribute.LIGHT_RED, "lightcoral");
		BBCODE_COLORS.put(TextAttribute.LIGHT_MAGENTA, "hotpink");
		BBCODE_COLORS.put(TextAttribute.YELLOW, "yellow");
		BBCODE_COLORS.put(TextAttribute.WHITE, "white");
		BBCODES.put(TextAttribute.BOLD, "b");
		BBCODES.put(TextAttribute.STRIKETHROUGH, "s");
		BBCODES.put(TextAttribute.UNDERLINE, "u");
		BBCODES.put(TextAttribute.ITALIC, "i");
	}
}
