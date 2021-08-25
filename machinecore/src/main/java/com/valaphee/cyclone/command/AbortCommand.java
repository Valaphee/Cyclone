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

import static com.valaphee.cyclone.language.I18n.tl;
import java.util.List;

/**
 * Default
 *
 * @author valaphee
 */
public final class AbortCommand
		extends Command
{
	private final CommandHandler commandHandler;

	public AbortCommand(final CommandHandler commandHandler)
	{
		this.commandHandler = commandHandler;

		setName(tl("cyclone.command.abort"));
		setDescription(tl("cyclone.command.abort.desc"));
		setUsage(tl("cyclone.command.abort.usage"));
		setArgumentsRange(0, 0);
		addKey("abort");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		final QueuedCommand.Queuenation queuenation = commandHandler.unqueue(sender);

		queuenation.getCommand().aborted(sender, queuenation.getLabel(), queuenation.getArguments());
	}
}
