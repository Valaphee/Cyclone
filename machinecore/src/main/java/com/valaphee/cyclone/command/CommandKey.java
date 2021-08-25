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

package com.valaphee.cyclone.command;

import java.util.regex.Pattern;

/**
 * Default
 *
 * @author valaphee
 */
public final class CommandKey
		implements Comparable<CommandKey>
{
	private final String key;
	private final Pattern keyPattern;
	private final int minimumArguments;
	private final int maximumArguments;

	public CommandKey(final String key, final int minimumArguments, final int maximumArguments)
	{
		this.key = key.toLowerCase();
		keyPattern = Pattern.compile(this.key + "(\\s+.*|\\s*)");
		this.minimumArguments = minimumArguments;
		this.maximumArguments = maximumArguments;
	}

	public CommandKey(final String key)
	{
		this(key, -1, -1);
	}

	public String getKey()
	{
		return key;
	}

	public Pattern getKeyPattern()
	{
		return keyPattern;
	}

	public boolean checkArgumentsRange(final int count)
	{
		return (minimumArguments != -1) && (count >= minimumArguments) && ((maximumArguments == -1) || (count <= maximumArguments));
	}

	@Override
	public int compareTo(final CommandKey other)
	{
		if (getKey().length() > other.getKey().length())
		{
			return -1;
		}
		if (getKey().length() < other.getKey().length())
		{
			return 1;
		}

		return getKey().compareToIgnoreCase(other.getKey());
	}
}
