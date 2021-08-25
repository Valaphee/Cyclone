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
import com.valaphee.cyclone.util.TimestampUtil;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

/**
 * Default
 *
 * @author valaphee
 */
public abstract class QueuedCommand
		extends Command
{
	private final CommandHandler commandHandler;
	private long expiration;

	public QueuedCommand(final CommandHandler commandHandler)
	{
		this.commandHandler = commandHandler;

		setExpiration(15_000L);
	}

	public long getExpiration()
	{
		return expiration;
	}

	protected final void setExpiration(final long expiration)
	{
		this.expiration = expiration;
	}

	public Queuenation getQueuenation(final String label, final List<String> arguments)
	{
		return new Queuenation(this, label, arguments, System.currentTimeMillis() + expiration);
	}

	public void queue(final CommandSender sender, final Queuenation queuenation)
	{
		commandHandler.queue(sender, queuenation);

		sender.sendMessage(tl("cyclone.queued", TimestampUtil.formatTimestamp(new Timestamp(expiration))));
	}

	public void confirmed(final CommandSender sender, final String label, final List<String> arguments)
	{}

	public void aborted(final CommandSender sender, final String label, final List<String> arguments)
	{
	}

	protected final class Queuenation
	{
		final QueuedCommand command;
		final String label;
		final List<String> arguments;
		final long validUntil;

		private Queuenation(final QueuedCommand command, final String label, final List<String> arguments, final long validUntil)
		{
			this.command = command;
			this.label = label;
			this.arguments = arguments;
			this.validUntil = validUntil;
		}

		public QueuedCommand getCommand()
		{
			return command;
		}

		public String getLabel()
		{
			return label;
		}

		public List<String> getArguments()
		{
			return Collections.unmodifiableList(arguments);
		}

		public long getValidUntil()
		{
			return validUntil;
		}
	}
}
