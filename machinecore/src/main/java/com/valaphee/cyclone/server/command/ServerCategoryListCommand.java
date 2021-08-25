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
import com.valaphee.cyclone.server.ServerCategory;
import com.valaphee.cyclone.server.ServerManager;
import com.valaphee.cyclone.util.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Default
 *
 * @author valaphee
 */
public final class ServerCategoryListCommand
		extends PaginatedCommand<String>
{
	private final ServerManager manager;

	public ServerCategoryListCommand(final ServerManager manager)
	{
		this.manager = manager;

		setName(tl("cyclone.server.command.category.list"));
		setDescription(tl("cyclone.server.command.category.list.desc"));
		setUsage(tl("cyclone.servercategory.command.category.list.usage"));
		addKey("server category list");
		addKey("server categories");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.server.category.list"))
		{
			final List<String> serverCategories = new ArrayList<>();
			final Stack<ServerCategory> stacked = new Stack<>();
			int level = 0;
			stacked.push(manager);
			while (!stacked.isEmpty())
			{
				final ServerCategory current = stacked.pop();

				serverCategories.add(StringUtil.repeat(level, "=") + current.getName());

				level = current.getSubcategories().isEmpty() ? level - 1 : level + 1;
				stacked.addAll(current.getSubcategories());
			}
			final Pagination pagination = getPagination(sender, arguments, serverCategories);

			sender.sendMessage(tl("cyclone.server.category.listheader", pagination.getPage(), pagination.getPages()));
			if (pagination.getItems().isEmpty())
			{
				sender.sendMessage(tl("cyclone.server.category.listnothing"));
			}
			else
			{
				display(sender, pagination);
			}
			sender.sendMessage(tl("cyclone.server.category.listfooter", pagination.getPage(), pagination.getPages()));
		}
		else
		{
			sender.sendMessage(tl("cyclone.nopermission"));
		}
	}

	@Override
	public List<String> run(final CommandSender sender, final Pagination pagination)
	{
		final String pattern = "(?i).*" + pagination.getFilter() + ".*";
		final List<String> filtered = new ArrayList<>();
		pagination.getCollection().stream().forEach((serverCategory) ->
		{
			filtered.add(serverCategory);
		});

		return filtered;
	}

	@Override
	public String format(final String item)
	{
		return tl("cyclone.servercategory.listentry", item);
	}
}
