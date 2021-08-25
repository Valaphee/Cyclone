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
import com.valaphee.cyclone.account.data.UserPunishmentData;
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
public final class UserUnbanCommand
		extends Command
{
	private final AccountManager manager;
	private final PermissionHandler permissionHandler;

	public UserUnbanCommand(final AccountManager manager, final PermissionHandler permissionHandler)
	{
		this.manager = manager;
		this.permissionHandler = permissionHandler;

		setName(tl("cyclone.user.command.unban"));
		setDescription(tl("cyclone.user.command.unban.desc"));
		setUsage(tl("cyclone.user.command.unban.usage"));
		setArgumentsRange(1, 1);
		addKey("user unban");
		addKey("unban");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.user.unban"))
		{
			final User byUser = sender instanceof User ? (User) sender : null;
			final User forUser = manager.getUser(manager.findUserId(arguments.get(0)));
			if (forUser != null)
			{
				final UserPunishmentData punishment = forUser.getActivePunishment(UserPunishmentData.Type.BAN);
				if (punishment != null)
				{
					final User createdByUser = manager.getUser(punishment.createdBy);
					if ((createdByUser == null) || (byUser == null) || (permissionHandler.getVariableAsLong(createdByUser, "weight") <= permissionHandler.getVariableAsLong(byUser, "weight")))
					{
						forUser.deactivatePunishment(UserPunishmentData.Type.BAN);
						sender.sendMessage(tl("cyclone.user.unban", forUser.getName()));
					}
					else
					{
						sender.sendMessage(tl("cyclone.user.tooheavy", forUser.getName()));
					}
				}
				else
				{
					sender.sendMessage(tl("cyclone.user.notbanned", forUser.getName()));
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
