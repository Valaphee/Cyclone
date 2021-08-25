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

import com.valaphee.cyclone.account.User;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Default
 *
 * @author valaphee
 */
public final class CommandHandler
{
	private static final Pattern ARGUMENTS_PATTERN = Pattern.compile(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");
	private final List<Command> commands = new ArrayList<>();
	private final Map<Long, QueuedCommand.Queuenation> queuedCommands = new HashMap<>();

	public Collection<Command> getCommands()
	{
		return Collections.unmodifiableCollection(commands);
	}

	public synchronized void register(final Command command)
	{
		commands.add(command);
	}

	public void registerAll(final Collection<Command> commands)
	{
		commands.forEach((command) ->
		{
			register(command);
		});
	}

	public synchronized void clear()
	{
		commands.clear();
	}

	public boolean dispatch(final CommandSender sender, final String line)
			throws CommandException
	{
		final String trimmedLine = line.trim();

		final List<Command> foundCommands = new ArrayList<>();
		final List<CommandKey> foundCommandKeys = new ArrayList<>();
		commands.forEach((command) ->
		{
			final CommandKey commandKey = command.getKeyFor(trimmedLine);
			if (commandKey != null)
			{
				foundCommands.add(command);
				foundCommandKeys.add(commandKey);
			}
		});
		if (foundCommands.isEmpty())
		{
			return false;
		}

		Command bestMatch = null;
		CommandKey bestMatchKey = null;
		int bestMatchAgreement = 0;
		for (int i = foundCommands.size() - 1; i >= 0; --i)
		{
			final int agreement = ARGUMENTS_PATTERN.split(foundCommandKeys.get(i).getKey()).length;
			if (agreement >= bestMatchAgreement)
			{
				bestMatch = foundCommands.get(i);
				bestMatchKey = foundCommandKeys.get(i);
				bestMatchAgreement = agreement;
			}
		}
		if ((bestMatch == null) || (bestMatchKey == null))
		{
			return false;
		}

		final String label = bestMatchKey.getKey();
		final String[] argumentsArray = ARGUMENTS_PATTERN.split(trimmedLine.substring(label.length()));
		final List<String> arguments = Collections.unmodifiableList(Arrays.asList(Arrays.copyOfRange(argumentsArray, 1, argumentsArray.length)));
		if (bestMatch.checkArgumentsRange(arguments.size()) || bestMatchKey.checkArgumentsRange(arguments.size()))
		{
			try
			{
				bestMatch.execute(sender, label, arguments);
			}
			catch (final CommandException ex)
			{
				throw ex;
			}
			catch (final Throwable thrown)
			{
				throw new CommandException("An unexpected error occured while dispatching command " + label + ".", thrown);
			}
		}
		else
		{
			try
			{
				bestMatch.usage(sender);
			}
			catch (final CommandException ex)
			{
				throw ex;
			}
			catch (final Throwable thrown)
			{
				throw new CommandException("An unexpected error occured while dispatching command " + label + ".", thrown);
			}
		}

		return true;
	}

	public void queue(final CommandSender sender, final QueuedCommand.Queuenation quenation)
	{
		final long id = sender instanceof User ? ((User) sender).getId() : 0;
		final QueuedCommand.Queuenation oldQueuenation = queuedCommands.put(id, quenation);
		if (oldQueuenation != null)
		{
			oldQueuenation.getCommand().aborted(sender, null, oldQueuenation.getArguments());
		}
	}

	public QueuedCommand.Queuenation unqueue(final CommandSender sender)
	{
		return queuedCommands.remove(sender instanceof User ? ((User) sender).getId() : 0);
	}
}
