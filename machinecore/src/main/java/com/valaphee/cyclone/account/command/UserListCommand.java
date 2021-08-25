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
import com.valaphee.cyclone.account.User;
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
public final class UserListCommand
		extends PaginatedCommand<User>
{
	private final AccountManager manager;

	public UserListCommand(final AccountManager manager)
	{
		this.manager = manager;

		setName(tl("cyclone.user.command.list"));
		setDescription(tl("cyclone.user.command.list.desc"));
		setUsage(tl("cyclone.user.command.list.usage"));
		addKey("user list");
		addKey("users");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.user.list"))
		{
			final Pagination pagination = getPagination(sender, arguments, manager.getUsers());

			sender.sendMessage(tl("cyclone.user.listheader", pagination.getPage(), pagination.getPages()));
			if (pagination.getItems().isEmpty())
			{
				sender.sendMessage(tl("cyclone.user.listnothing"));
			}
			else
			{
				display(sender, pagination);
			}
			sender.sendMessage(tl("cyclone.user.listfooter", pagination.getPage(), pagination.getPages()));
		}
		else
		{
			sender.sendMessage(tl("cyclone.nopermission"));
		}
	}

	@Override
	public List<User> run(final CommandSender sender, final Pagination pagination)
	{
		final String pattern = "(?i).*" + pagination.getFilter() + ".*";
		final List<User> filtered = new LinkedList<>();
		pagination.getCollection().stream().forEach((user) ->
		{
			if (Long.toString(user.getId()).matches(pattern))
			{
				filtered.add(user);
			}
			else if (user.getName().matches(pattern))
			{
				filtered.add(user);
			}
		});

		return filtered;
	}

	@Override
	public String format(final User item)
	{
		return tl("cyclone.user.listentry", item.getId(), item.getName());
	}
}
