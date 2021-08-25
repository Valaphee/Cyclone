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
public final class GroupPermissionRemoveCommand
		extends Command
{
	private final AccountManager manager;

	public GroupPermissionRemoveCommand(final AccountManager manager)
	{
		this.manager = manager;

		setName(tl("cyclone.group.command.permission.remove"));
		setDescription(tl("cyclone.group.command.permission.remove.desc"));
		setUsage(tl("cyclone.group.command.permission.remove.usage"));
		setArgumentsRange(2, -1);
		addKey("group permission remove");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.group.permission.remove"))
		{
			final Long forGroupId = manager.findGroupId(arguments.get(0));
			if (forGroupId != null)
			{
				final Group forGroup = manager.getGroup(forGroupId);
				for (int i = 1; i < arguments.size(); ++i)
				{
					final String permission = arguments.get(i);
					if (forGroup.removePermission(permission))
					{
						sender.sendMessage(tl("cyclone.group.permission.remove", forGroup.getName(), permission));
					}
					else
					{
						sender.sendMessage(tl("cyclone.group.permission.notremoved", forGroup.getName(), permission));
					}
				}
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
}
