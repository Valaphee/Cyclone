
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
package com.valaphee.cyclone.connection.command;

import com.valaphee.cyclone.command.Command;
import com.valaphee.cyclone.command.CommandException;
import com.valaphee.cyclone.command.CommandSender;
import com.valaphee.cyclone.communication.CommunicationMetrics;
import com.valaphee.cyclone.connection.ConnectionManager;
import static com.valaphee.cyclone.language.I18n.tl;
import java.util.List;

/**
 * Default
 *
 * @author valaphee
 */
public final class ConnectionCommand
		extends Command
{
	private final ConnectionManager manager;

	public ConnectionCommand(final ConnectionManager manager)
	{
		this.manager = manager;

		setName(tl("cyclone.connection.command"));
		setDescription(tl("cyclone.connection.command.desc"));
		setUsage(tl("cyclone.connection.command.usage"));
		setArgumentsRange(0, 0);
		addKey("connection");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.connection "))
		{
			final CommunicationMetrics metrics = manager.getCommunication().getMetrics();

			sender.sendMessage(tl("cyclone.connection.header"));
			sender.sendMessage(tl("cyclone.connection.incommingnode", manager.getIncomingId(), manager.getIncomingConnection().getAddress()));
			sender.sendMessage(tl("cyclone.connection.ownnode", manager.getId(), manager.getAddress()));
			sender.sendMessage(tl("cyclone.connection.outgoingnode", manager.getOutgoingId(), manager.getOutgoingConnection().getAddress()));
			sender.sendMessage(tl("cyclone.connection.received", metrics.getReceivedPackets(), metrics.getReceivedBytes()));
			sender.sendMessage(tl("cyclone.connection.sent", metrics.getSentPackets(), metrics.getSentBytes()));
			sender.sendMessage(tl("cyclone.connection.footer"));
		}
		else
		{
			sender.sendMessage(tl("cyclone.nopermission"));
		}
	}
}
