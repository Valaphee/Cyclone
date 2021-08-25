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

package com.valaphee.cyclone.engine.mode;

import com.valaphee.cyclone.command.CommandException;
import com.valaphee.cyclone.command.CommandHandler;
import com.valaphee.cyclone.command.CommandSender;
import com.valaphee.cyclone.command.QueuedCommand;
import com.valaphee.cyclone.engine.Engine;
import static com.valaphee.cyclone.language.I18n.tl;
import com.valaphee.cyclone.util.TimestampUtil;
import java.sql.Timestamp;
import java.util.List;

/**
 * Default
 *
 * @author valaphee
 */
public final class EndCommand
		extends QueuedCommand
{
	private final Engine engine;

	public EndCommand(final CommandHandler commandHandler, final Engine engine)
	{
		super(commandHandler);

		this.engine = engine;

		setName(tl("cyclone.command.end"));
		setDescription(tl("cyclone.command.end.desc"));
		setUsage(tl("cyclone.command.end.usage"));
		setArgumentsRange(0, 1);
		addKey("end");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.end"))
		{
			if (engine.isRunning())
			{
				queue(sender, getQueuenation(label, arguments));
			}
			else
			{
				sender.sendMessage(tl("cyclone.alreadyend"));
			}
		}
		else
		{
			sender.sendMessage(tl("cyclone.nopermission"));
		}
	}

	@Override
	public void confirmed(final CommandSender sender, final String label, final List<String> arguments)
	{
		if (arguments.size() > 0)
		{
			final Timestamp from = TimestampUtil.getCurrentTimestamp();
			final Timestamp to = arguments.size() > 0 ? TimestampUtil.getTimestampForDiff(from, arguments.get(0), true) : new Timestamp(-1);
			final long difference = to.getTime() - from.getTime();

			engine.stop();
			sender.sendMessage(tl("cyclone.endwithtime", difference));
		}
		else
		{
			engine.stop();
			sender.sendMessage(tl("cyclone.end"));
		}
	}
}
