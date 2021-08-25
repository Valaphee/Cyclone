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
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Default
 *
 * @author valaphee
 */
public final class GroupInheritanceListCommand
		extends PaginatedCommand<Group>
{
	private final AccountManager manager;

	public GroupInheritanceListCommand(final AccountManager manager)
	{
		this.manager = manager;

		setName(tl("cyclone.group.command.inheritance.list"));
		setDescription(tl("cyclone.group.command.inheritance.list.desc"));
		setUsage(tl("cyclone.group.command.inheritance.list.usage"));
		setArgumentsRange(getMinimumArguments() + 1, getMaximumArguments() + 1);
		addKey("group inheritance list");
		addKey("group inheritances");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.group.inheritance.list"))
		{
			final Group forGroup = manager.getGroup(manager.findGroupId(arguments.get(0)));
			if (forGroup != null)
			{
				final List<Group> inheritances = new ArrayList<>();
				final Stack<Group> stacked = new Stack<>();
				stacked.push(forGroup);
				while (!stacked.isEmpty())
				{
					stacked.pop().getInheritances().stream().map((id) -> manager.getGroup(id)).filter((inheritance) -> ((inheritance != null) && (!inheritances.contains(inheritance)))).forEachOrdered((inheritance) ->
					{
						stacked.add(inheritance);
						inheritances.add(inheritance);
					});
				}
				final Pagination pagination = getPagination(sender, arguments, inheritances);

				sender.sendMessage(tl("cyclone.group.inheritance.listheader", pagination.getPage(), pagination.getPages()));
				if (pagination.getItems().isEmpty())
				{
					sender.sendMessage(tl("cyclone.group.inheritance.listnothing"));
				}
				else
				{
					display(sender, pagination);
				}
				sender.sendMessage(tl("cyclone.group.inheritance.listfooter", pagination.getPage(), pagination.getPages()));
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
	public List<Group> run(final CommandSender sender, final Pagination pagination)
	{
		return new ArrayList<>(pagination.getCollection());
	}

	@Override
	public String format(final Group item)
	{
		return tl("cyclone.group.inheritance.listentry", item.getName());
	}
}
