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

import com.valaphee.cyclone.account.User;
import com.valaphee.cyclone.command.Command;
import com.valaphee.cyclone.command.CommandException;
import com.valaphee.cyclone.command.CommandSender;
import static com.valaphee.cyclone.language.I18n.tl;
import com.valaphee.cyclone.server.Server;
import com.valaphee.cyclone.server.ServerCategory;
import com.valaphee.cyclone.server.ServerManager;
import java.util.List;
import java.util.UUID;

/**
 * Default
 *
 * @author valaphee
 */
public final class ServerStopCommand
		extends Command
{
	private final ServerManager manager;

	public ServerStopCommand(final ServerManager manager)
	{
		this.manager = manager;

		setName(tl("cyclone.server.command.stop"));
		setDescription(tl("cyclone.server.command.stop.desc"));
		setUsage(tl("cyclone.server.command.stop.usage"));
		setArgumentsRange(0, 1);
		addKey("server stop");
		addKey("stop");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if ((sender.hasPermission("cyclone.server.stop.server") || sender.hasPermission("cyclone.server.stop.category")) && (arguments.size() > 0))
		{
			try
			{
				final Server server = manager.getServer(UUID.fromString(arguments.get(0)), true);
				if (sender.hasPermission("cyclone.server.stop.server"))
				{
					if (server != null)
					{
						server.stop();
						sender.sendMessage(tl("cyclone.server.stop", server.getId()));
					}
					else
					{
						sender.sendMessage(tl("cyclone.server.notfound", arguments.get(0)));
					}
				}
			}
			catch (final IllegalArgumentException ex)
			{
				if (sender.hasPermission("cyclone.server.stop.category"))
				{
					final ServerCategory category = manager.getSubcategory(arguments.get(0));
					if (category != manager)
					{
						category.getServers(true).stream().forEachOrdered((server) ->
						{
							server.stop();
							sender.sendMessage(tl("cyclone.server.stop", server.getId()));
						});
					}
					else
					{
						sender.sendMessage(tl("cyclone.category.notfound", arguments.get(0)));
					}
				}
			}
		}
		else if (sender.hasPermission("cyclone.stop.self"))
		{
			if (sender instanceof User)
			{
				final User byUser = (User) sender;
				final Server server = manager.getServer(byUser.getServer(), true);
				server.stop();
				sender.sendMessage(tl("cyclone.server.stop", server.getId()));
			}
			else
			{
				sender.sendMessage(tl("cyclone.noconsole"));
			}
		}
		else
		{
			sender.sendMessage(tl("cyclone.nopermission"));
		}
	}
}
