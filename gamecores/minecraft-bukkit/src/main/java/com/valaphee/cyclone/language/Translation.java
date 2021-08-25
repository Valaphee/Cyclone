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

package com.valaphee.cyclone.language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Default
 *
 * @author valaphee
 */
public final class Translation
{
	private static final Pattern SPLIT_PATTERN = Pattern.compile("=");
	private final Map<String, String> translations = new HashMap<>();
	private final Map<String, MessageFormat> translationsCache = new HashMap<>();
	private boolean unicode;

	public Translation(final InputStream stream)
	{
		load(stream);
	}

	private void load(final InputStream stream)
	{
		translations.clear();
		translationsCache.clear();

		try
		{
			final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			String line;
			while ((line = reader.readLine()) != null)
			{
				if ((!line.isEmpty()) && (line.charAt(0) != '#'))
				{
					final String[] splitted = SPLIT_PATTERN.split(line, 2);
					if (splitted.length == 2)
					{
						final StringBuilder message = new StringBuilder(splitted[1]);
						for (int i = message.indexOf("\\u"); i > -1; i = message.indexOf("\\u"))
						{
							final int length = message.length();
							if (i > (length - 6))
							{
								break;
							}

							final String sequence = message.substring(i + 2, i + 6);
							final char sequenceValue = (char) Short.parseShort(sequence, 16);
							message.delete(i + 1, i + 6);
							message.setCharAt(i, sequenceValue);
						}
						translations.put(splitted[0], message.toString());
					}
				}
			}
		}
		catch (final IOException ignore)
		{}
	}

	private void checkUnicode()
	{
		int characters = 0;
		int unicodeCharacters = 0;

		for (final String translation : translations.values())
		{
			characters += translation.length();

			for (int i = translation.length() - 1; i >= 0; --i)
			{
				if (translation.charAt(i) >= 0xFF)
				{
					++unicodeCharacters;
				}
			}
		}

		this.unicode = (unicodeCharacters / (float) characters) > 0.1f;
	}

	public String translate(final String name)
	{
		return translations.get(name);
	}

	public String format(final String name, final Object... arguments)
	{
		final String translation = translate(name);
		if (translation == null)
		{
			return null;
		}

		MessageFormat format = translationsCache.get(name);
		if (format == null)
		{
			try
			{
				format = new MessageFormat(translation);
			}
			catch (final IllegalArgumentException ex)
			{
				format = new MessageFormat(translation.replaceAll("\\{(\\D*?)\\}", "\\[$1\\]"));
			}
			translationsCache.put(name, format);
		}

		return format.format(arguments);
	}

	public boolean isUnicode()
	{
		return unicode;
	}
}
