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

import com.valaphee.cyclone.command.CommandException;
import com.valaphee.cyclone.command.CommandSender;
import com.valaphee.cyclone.command.PaginatedCommand;
import static com.valaphee.cyclone.language.I18n.tl;
import com.valaphee.cyclone.server.Server;
import com.valaphee.cyclone.server.ServerManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Default
 *
 * @author valaphee
 */
public final class ServerListCommand
		extends PaginatedCommand<Server>
{
	private final ServerManager manager;

	public ServerListCommand(final ServerManager manager)
	{
		this.manager = manager;

		setName(tl("cyclone.server.command.list"));
		setDescription(tl("cyclone.server.command.list.desc"));
		setUsage(tl("cyclone.server.command.list.usage"));
		addKey("server list");
		addKey("servers");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.server.list"))
		{
			final Pagination pagination = getPagination(sender, arguments, manager.getServers(true));

			sender.sendMessage(tl("cyclone.server.listheader", pagination.getPage(), pagination.getPages()));
			if (pagination.getItems().isEmpty())
			{
				sender.sendMessage(tl("cyclone.server.listnothing"));
			}
			else
			{
				display(sender, pagination);
			}
			sender.sendMessage(tl("cyclone.server.listfooter", pagination.getPage(), pagination.getPages()));
		}
		else
		{
			sender.sendMessage(tl("cyclone.nopermission"));
		}
	}

	@Override
	public List<Server> run(final CommandSender sender, final Pagination pagination)
	{
		final String pattern = "(?i).*" + pagination.getFilter() + ".*";
		final List<Server> filtered = new ArrayList<>();
		pagination.getCollection().stream().forEach((server) ->
		{
			if (server.getId().toString().matches(pattern))
			{
				filtered.add(server);
			}
			else if (server.getCategory().getName().matches(pattern))
			{
				filtered.add(server);
			}
		});

		return filtered;
	}

	@Override
	public String format(final Server item)
	{
		return tl("cyclone.server.listentry", item.getId(), item.getPlayerCount(), item.getPlayerLimit(), item.getTicksPerSecond(), item.getState() != null ? item.getState().name() : "STARTING");
	}
}
