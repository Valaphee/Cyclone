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

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import jline.console.completer.Completer;

/**
 * Default
 *
 * @author valaphee
 */
public final class CommandCompleter
		implements Completer
{
	private final CommandHandler commandHandler;

	public CommandCompleter(final CommandHandler commandHandler)
	{
		this.commandHandler = commandHandler;
	}

	@Override
	public int complete(final String buffer, final int cursor, final List<CharSequence> candidates)
	{
		if (buffer == null)
		{
			commandHandler.getCommands().forEach((command) -> command.getKeys().stream().map((commandKey) -> commandKey.getKey()).forEach(candidates::add));
		}
		else
		{
			final SortedSet<String> commands = new TreeSet<>();
			commandHandler.getCommands().forEach((command) -> command.getKeys().stream().map((commandKey) -> commandKey.getKey()).forEach(commands::add));
			for (final String command : commands.tailSet(buffer))
			{
				if (!command.startsWith(buffer))
				{
					break;
				}
				candidates.add(command);
			}
		}
		if (candidates.size() == 1)
		{
			candidates.set(0, candidates.get(0) + " ");
		}

		return candidates.isEmpty() ? -1 : 0;
	}
}
