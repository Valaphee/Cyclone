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
public final class UserPunishCommand
		extends Command
{
	private final AccountManager manager;
	private final PermissionHandler permissionHandler;

	public UserPunishCommand(final AccountManager manager, final PermissionHandler permissionHandler)
	{
		this.manager = manager;
		this.permissionHandler = permissionHandler;

		setName(tl("cyclone.user.command.punish"));
		setDescription(tl("cyclone.user.command.punish.desc"));
		setUsage(tl("cyclone.user.command.punish.usage"));
		setArgumentsRange(2, 3);
		addKey("user punish");
		addKey("punish");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.user.punish"))
		{
			final User byUser = sender instanceof User ? (User) sender : null;
			final User forUser = manager.getUser(manager.findUserId(arguments.get(0)));
			if (forUser != null)
			{

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
