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
import com.valaphee.cyclone.account.PermissionHandler;
import com.valaphee.cyclone.account.User;
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
public final class UserPromoteCommand
		extends Command
{
	private final AccountManager manager;
	private final PermissionHandler permissionHandler;

	public UserPromoteCommand(final AccountManager manager, final PermissionHandler permissionHandler)
	{
		this.manager = manager;
		this.permissionHandler = permissionHandler;

		setName(tl("cyclone.user.command.promote"));
		setDescription(tl("cyclone.user.command.promote.desc"));
		setUsage(tl("cyclone.user.command.promote.usage"));
		setArgumentsRange(2, 2);
		addKey("user promote");
		addKey("promote");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.user.promote"))
		{
			final User byUser = sender instanceof User ? (User) sender : null;
			final User forUser = manager.getUser(manager.findUserId(arguments.get(0)));
			if (forUser != null)
			{
				if ((byUser == null) || (permissionHandler.getVariableAsLong(forUser, "weight") < permissionHandler.getVariableAsLong(byUser, "weight")) || forUser.equals(byUser))
				{
					final Group group = manager.getGroup(manager.findGroupId(arguments.get(1)));
					if (group != null)
					{
						forUser.setGroup(group);
						sender.sendMessage(tl("cyclone.user.promote", forUser.getName(), group.getName()));
					}
					else
					{
						sender.sendMessage(tl("cyclone.group.notfound", arguments.get(1)));
					}
				}
				else
				{
					sender.sendMessage(tl("cyclone.user.tooheavy", forUser.getName()));
				}
			}
			else
			{
				sender.sendMessage(tl("cyclone.user.notfound", arguments.get(0)));
			}
		}
		else
		{
			sender.sendMessage(tl("cyclone.nopermission"));
		}
	}
}
