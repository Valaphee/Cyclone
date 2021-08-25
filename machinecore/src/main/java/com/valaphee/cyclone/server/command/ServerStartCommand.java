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
public final class ServerStartCommand
		extends Command
{
	private final ServerManager manager;

	public ServerStartCommand(final ServerManager manager)
	{
		this.manager = manager;

		setName(tl("cyclone.server.command.start"));
		setDescription(tl("cyclone.server.command.start.desc"));
		setUsage(tl("cyclone.server.command.start.usage"));
		setArgumentsRange(1, 1);
		addKey("server start");
		addKey("start");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.server.start"))
		{
			try
			{
				final Server server = manager.getServer(UUID.fromString(arguments.get(0)), true);
				if (sender.hasPermission("cyclone.server.start.server"))
				{
					if (server != null)
					{
						server.start();
						sender.sendMessage(tl("cyclone.server.start", server.getId(), server.getCategory().getUrn()));
					}
					else
					{
						sender.sendMessage(tl("cyclone.server.notfound", arguments.get(0)));
					}
				}
			}
			catch (final IllegalArgumentException ex)
			{
				if (sender.hasPermission("cyclone.server.start.category"))
				{
					final ServerCategory category = manager.getSubcategory(arguments.get(0));
					if (category != manager)
					{
						manager.create(category);
						sender.sendMessage(tl("cyclone.server.create", category.getUrn()));
					}
					else
					{
						sender.sendMessage(tl("cyclone.category.notfound", arguments.get(0)));
					}
				}
			}
		}
		else
		{
			sender.sendMessage(tl("cyclone.nopermission"));
		}
	}
}
