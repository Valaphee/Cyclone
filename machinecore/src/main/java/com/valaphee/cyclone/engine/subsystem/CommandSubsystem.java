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

package com.valaphee.cyclone.engine.subsystem;

import static com.valaphee.cyclone.Cyclone.reader;
import com.valaphee.cyclone.command.*;
import com.valaphee.cyclone.context.Context;
import com.valaphee.cyclone.engine.EngineSubsystem;
import static com.valaphee.cyclone.language.I18n.tl;
import com.valaphee.cyclone.reflect.ThrowablePrinter;
import com.valaphee.cyclone.text.ANSICodeTextTranscoder;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Default
 *
 * @author valaphee
 */
public final class CommandSubsystem
		implements EngineSubsystem
{
	private static final Logger LOGGER = LogManager.getLogger(CommandSubsystem.class);
	private final Queue<String> queued = new ConcurrentLinkedQueue<>();
	private CommandHandler commandHandler;
	private final CommandSender commandSender = new ConsoleCommandSender(LOGGER, new ANSICodeTextTranscoder());

	@Override
	public String getName()
	{
		return "Command";
	}

	@Override
	public void preStartup(final Context context)
	{
		context.set(CommandHandler.class, commandHandler = new CommandHandler());
	}

	@Override
	public void startup(final Context context)
	{
		final Thread consoleReader = new Thread(() ->
		{
			for (;;)
			{
				try
				{
					final String message = reader().readLine(null, null, null);
					if ((message != null) && (!message.trim().isEmpty()))
					{
						queued.add(message);
					}
				}
				catch (final IOException | IndexOutOfBoundsException ignore)
				{}
			}
		}, "Console Reader");
		consoleReader.start();

		reader().addCompleter(new CommandCompleter(commandHandler));
	}

	@Override
	public void postUpdate(final float delta)
	{
		while (!queued.isEmpty())
		{
			final String message = queued.poll();
			try
			{
				if (!commandHandler.dispatch(commandSender, message))
				{
					commandSender.sendMessage(tl("cyclone.commandnotfound", message));
				}
			}
			catch (final CommandException ex)
			{
				commandSender.sendMessage(ThrowablePrinter.print(ex));
			}
		}
	}

	@Override
	public void preShutdown()
	{}
}
