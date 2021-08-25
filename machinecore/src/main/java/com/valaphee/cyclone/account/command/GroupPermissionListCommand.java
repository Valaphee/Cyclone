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
public final class GroupPermissionListCommand
		extends PaginatedCommand<String>
{
	private final AccountManager manager;

	public GroupPermissionListCommand(final AccountManager manager)
	{
		this.manager = manager;

		setName(tl("cyclone.group.command.permission.list"));
		setDescription(tl("cyclone.group.command.permission.list.desc"));
		setUsage(tl("cyclone.group.command.permission.list.usage"));
		setArgumentsRange(getMinimumArguments() + 1, getMaximumArguments() + 1);
		addKey("group permission list");
		addKey("group permissions");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.group.permission.list"))
		{
			final Group forGroup = manager.getGroup(manager.findGroupId(arguments.get(0)));
			if (forGroup != null)
			{
				final Pagination pagination = getPagination(sender, arguments, forGroup.getPermissions());

				sender.sendMessage(tl("cyclone.group.permission.listheader", pagination.getPage(), pagination.getPages()));
				if (pagination.getItems().isEmpty())
				{
					sender.sendMessage(tl("cyclone.group.permission.listnothing"));
				}
				else
				{
					display(sender, pagination);
				}
				sender.sendMessage(tl("cyclone.group.permission.listfooter", pagination.getPage(), pagination.getPages()));
			}
			else
			{
				sender.sendMessage(tl("cyclone.group.notfound", arguments.get(0)));
			}
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
		final List<String> filtered = new LinkedList<>();
		pagination.getCollection().stream().forEach((permission) ->
		{
			if (permission.matches(pattern))
			{
				filtered.add(permission);
			}
		});

		return filtered;
	}

	@Override
	public String format(final String item)
	{
		return tl("cyclone.group.permission.listentry", item);
	}
}
