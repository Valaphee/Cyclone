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
import org.fusesource.jansi.Ansi;

/**
 * Default
 *
 * @author valaphee
 */
public final class ANSICodeTextTranscoder
		implements TextTranscoder<String>
{
	private static final Map<TextAttribute, CharSequence> ANSI_SEQUENCES = new HashMap<>();
	private static final Map<TextAttribute, CharSequence> ANSI_DISABLE_SEQUENCES = new HashMap<>();

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

							transcodedText.append(ANSI_SEQUENCES.get(attribute));
						}
						else
						{
							attributeStack.pop();

							if (!attributeStack.isEmpty())
							{
								transcodedText.append(ANSI_SEQUENCES.get(attributeStack.peek()));
							}
							else
							{
								ANSI_DISABLE_SEQUENCES.get(attribute);
							}
						}
					}
					else
					{
						if (!attributeFlags.contains(attribute))
						{
							attributeFlags.add(attribute);

							transcodedText.append(ANSI_SEQUENCES.get(attribute));
						}
						else
						{
							attributeFlags.remove(attribute);

							transcodedText.append(ANSI_DISABLE_SEQUENCES.get(attribute));
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
		ANSI_SEQUENCES.put(TextAttribute.BLACK, Ansi.ansi().fg(Ansi.Color.BLACK).boldOff().toString());
		ANSI_SEQUENCES.put(TextAttribute.BLUE, Ansi.ansi().fg(Ansi.Color.BLUE).boldOff().toString());
		ANSI_SEQUENCES.put(TextAttribute.GREEN, Ansi.ansi().fg(Ansi.Color.GREEN).boldOff().toString());
		ANSI_SEQUENCES.put(TextAttribute.CYAN, Ansi.ansi().fg(Ansi.Color.CYAN).boldOff().toString());
		ANSI_SEQUENCES.put(TextAttribute.RED, Ansi.ansi().fg(Ansi.Color.RED).boldOff().toString());
		ANSI_SEQUENCES.put(TextAttribute.MAGENTA, Ansi.ansi().fg(Ansi.Color.MAGENTA).boldOff().toString());
		ANSI_SEQUENCES.put(TextAttribute.BROWN, Ansi.ansi().fg(Ansi.Color.YELLOW).boldOff().toString());
		ANSI_SEQUENCES.put(TextAttribute.LIGHT_GRAY, Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString());
		ANSI_SEQUENCES.put(TextAttribute.DARK_GRAY, Ansi.ansi().fg(Ansi.Color.BLACK).bold().toString());
		ANSI_SEQUENCES.put(TextAttribute.LIGHT_BLUE, Ansi.ansi().fg(Ansi.Color.BLUE).bold().toString());
		ANSI_SEQUENCES.put(TextAttribute.LIGHT_GREEN, Ansi.ansi().fg(Ansi.Color.GREEN).bold().toString());
		ANSI_SEQUENCES.put(TextAttribute.LIGHT_CYAN, Ansi.ansi().fg(Ansi.Color.CYAN).bold().toString());
		ANSI_SEQUENCES.put(TextAttribute.LIGHT_RED, Ansi.ansi().fg(Ansi.Color.RED).bold().toString());
		ANSI_SEQUENCES.put(TextAttribute.LIGHT_MAGENTA, Ansi.ansi().fg(Ansi.Color.MAGENTA).bold().toString());
		ANSI_SEQUENCES.put(TextAttribute.YELLOW, Ansi.ansi().fg(Ansi.Color.YELLOW).bold().toString());
		ANSI_SEQUENCES.put(TextAttribute.WHITE, Ansi.ansi().fg(Ansi.Color.WHITE).bold().toString());
		ANSI_SEQUENCES.put(TextAttribute.BOLD, Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString());
		ANSI_SEQUENCES.put(TextAttribute.ITALIC, Ansi.ansi().a(Ansi.Attribute.ITALIC).toString());
		ANSI_SEQUENCES.put(TextAttribute.UNDERLINE, Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString());
		ANSI_SEQUENCES.put(TextAttribute.STRIKETHROUGH, Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString());
		ANSI_DISABLE_SEQUENCES.put(TextAttribute.BLACK, Ansi.ansi().fg(Ansi.Color.DEFAULT).toString());
		ANSI_DISABLE_SEQUENCES.put(TextAttribute.BLUE, Ansi.ansi().fg(Ansi.Color.DEFAULT).toString());
		ANSI_DISABLE_SEQUENCES.put(TextAttribute.GREEN, Ansi.ansi().fg(Ansi.Color.DEFAULT).toString());
		ANSI_DISABLE_SEQUENCES.put(TextAttribute.CYAN, Ansi.ansi().fg(Ansi.Color.DEFAULT).toString());
		ANSI_DISABLE_SEQUENCES.put(TextAttribute.RED, Ansi.ansi().fg(Ansi.Color.DEFAULT).toString());
		ANSI_DISABLE_SEQUENCES.put(TextAttribute.MAGENTA, Ansi.ansi().fg(Ansi.Color.DEFAULT).toString());
		ANSI_DISABLE_SEQUENCES.put(TextAttribute.BROWN, Ansi.ansi().fg(Ansi.Color.DEFAULT).toString());
		ANSI_DISABLE_SEQUENCES.put(TextAttribute.LIGHT_GRAY, Ansi.ansi().fg(Ansi.Color.DEFAULT).toString());
		ANSI_DISABLE_SEQUENCES.put(TextAttribute.DARK_GRAY, Ansi.ansi().fg(Ansi.Color.DEFAULT).boldOff().toString());
		ANSI_DISABLE_SEQUENCES.put(TextAttribute.LIGHT_BLUE, Ansi.ansi().fg(Ansi.Color.DEFAULT).boldOff().toString());
		ANSI_DISABLE_SEQUENCES.put(TextAttribute.LIGHT_GREEN, Ansi.ansi().fg(Ansi.Color.DEFAULT).boldOff().toString());
		ANSI_DISABLE_SEQUENCES.put(TextAttribute.LIGHT_CYAN, Ansi.ansi().fg(Ansi.Color.DEFAULT).boldOff().toString());
		ANSI_DISABLE_SEQUENCES.put(TextAttribute.LIGHT_RED, Ansi.ansi().fg(Ansi.Color.DEFAULT).boldOff().toString());
		ANSI_DISABLE_SEQUENCES.put(TextAttribute.LIGHT_MAGENTA, Ansi.ansi().fg(Ansi.Color.DEFAULT).boldOff().toString());
		ANSI_DISABLE_SEQUENCES.put(TextAttribute.YELLOW, Ansi.ansi().fg(Ansi.Color.DEFAULT).boldOff().toString());
		ANSI_DISABLE_SEQUENCES.put(TextAttribute.WHITE, Ansi.ansi().fg(Ansi.Color.DEFAULT).boldOff().toString());
		ANSI_DISABLE_SEQUENCES.put(TextAttribute.BOLD, Ansi.ansi().a(Ansi.Attribute.UNDERLINE_OFF).toString());
		ANSI_DISABLE_SEQUENCES.put(TextAttribute.ITALIC, Ansi.ansi().a(Ansi.Attribute.ITALIC_OFF).toString());
		ANSI_DISABLE_SEQUENCES.put(TextAttribute.UNDERLINE, Ansi.ansi().a(Ansi.Attribute.UNDERLINE_OFF).toString());
		ANSI_DISABLE_SEQUENCES.put(TextAttribute.STRIKETHROUGH, Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_OFF).toString());
	}
}
