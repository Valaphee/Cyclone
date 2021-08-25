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
public final class ConfirmCommand
		extends Command
{
	private final CommandHandler commandHandler;

	public ConfirmCommand(final CommandHandler commandHandler)
	{
		this.commandHandler = commandHandler;

		setName(tl("cyclone.command.confirm"));
		setDescription(tl("cyclone.command.confirm.desc"));
		setUsage(tl("cyclone.command.confirm.usage"));
		setArgumentsRange(0, 0);
		addKey("confirm");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		final QueuedCommand.Queuenation queuenation = commandHandler.unqueue(sender);
		if (queuenation.getValidUntil() >= System.currentTimeMillis())
		{
			queuenation.getCommand().confirmed(sender, queuenation.getLabel(), queuenation.getArguments());
		}
		else
		{
			queuenation.getCommand().aborted(sender, queuenation.getLabel(), queuenation.getArguments());
		}
	}
}
