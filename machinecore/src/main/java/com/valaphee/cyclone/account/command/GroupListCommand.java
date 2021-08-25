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

package com.valaphee.cyclone.account.command;

import com.valaphee.cyclone.account.AccountManager;
import com.valaphee.cyclone.account.Group;
import com.valaphee.cyclone.command.CommandException;
import com.valaphee.cyclone.command.CommandSender;
import com.valaphee.cyclone.command.PaginatedCommand;
import static com.valaphee.cyclone.language.I18n.tl;
import java.util.LinkedList;
import java.util.List;

/**
 * Default
 *
 * @author valaphee
 */
public final class GroupListCommand
		extends PaginatedCommand<Group>
{
	private final AccountManager manager;

	public GroupListCommand(final AccountManager manager)
	{
		this.manager = manager;

		setName(tl("cyclone.group.command.list"));
		setDescription(tl("cyclone.group.command.list.desc"));
		setUsage(tl("cyclone.group.command.list.usage"));
		addKey("group list");
		addKey("groups");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.group.list"))
		{
			final Pagination pagination = getPagination(sender, arguments, manager.getGroups());

			sender.sendMessage(tl("cyclone.group.listheader", pagination.getPage(), pagination.getPages()));
			if (pagination.getItems().isEmpty())
			{
				sender.sendMessage(tl("cyclone.group.listnothing"));
			}
			else
			{
				display(sender, pagination);
			}
			sender.sendMessage(tl("cyclone.group.listfooter", pagination.getPage(), pagination.getPages()));
		}
		else
		{
			sender.sendMessage(tl("cyclone.nopermission"));
		}
	}

	@Override
	public List<Group> run(final CommandSender sender, final Pagination pagination)
	{
		final String pattern = "(?i).*" + pagination.getFilter() + ".*";
		final List<Group> filtered = new LinkedList<>();
		pagination.getCollection().stream().forEach((group) ->
		{
			if (Long.toString(group.getId()).matches(pattern))
			{
				filtered.add(group);
			}
			else if (group.getName().matches(pattern))
			{
				filtered.add(group);
			}
		});

		return filtered;
	}

	@Override
	public String format(final Group item)
	{
		return tl("cyclone.group.listentry", item.getId(), item.getName());
	}
}
