
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
package com.valaphee.cyclone.server.command;

import com.valaphee.cyclone.account.AccountManager;
import com.valaphee.cyclone.command.Command;
import com.valaphee.cyclone.command.CommandException;
import com.valaphee.cyclone.command.CommandSender;
import static com.valaphee.cyclone.language.I18n.tl;
import java.util.List;

/**
 * Default
 *
 * @author valaphee
 */
public final class ServerBroadcastCommand
		extends Command
{
	private final AccountManager manager;

	public ServerBroadcastCommand(final AccountManager manager)
	{
		this.manager = manager;

		setName(tl("cyclone.server.command.broadcast"));
		setDescription(tl("cyclone.server.command.broadcast.desc"));
		setUsage(tl("cyclone.server.command.broadcast.usage"));
		setArgumentsRange(1, -1);
		addKey("server broadcast");
		addKey("broadcast");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.server.broadcast"))
		{
			final StringBuilder message = new StringBuilder();
			for (int i = 0; i < arguments.size(); ++i)
			{
				message.append(arguments.get(i)).append(' ');
			}
			message.setLength(message.length() - 1);

			final String messageString = message.toString();
			manager.getUsers().forEach((user) ->
			{
				((CommandSender) user).sendMessage(messageString);
			});
		}
		else
		{
			sender.sendMessage(tl("cyclone.nopermission"));
		}
	}
}
