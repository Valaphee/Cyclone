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
import com.valaphee.cyclone.util.TimestampUtil;
import java.sql.Timestamp;
import java.util.List;

/**
 * Default
 *
 * @author valaphee
 */
public final class UserBanCommand
		extends Command
{
	private final AccountManager manager;
	private final PermissionHandler permissionHandler;

	public UserBanCommand(final AccountManager manager, final PermissionHandler permissionHandler)
	{
		this.manager = manager;
		this.permissionHandler = permissionHandler;

		setName(tl("cyclone.user.command.ban"));
		setDescription(tl("cyclone.user.command.ban.desc"));
		setUsage(tl("cyclone.user.command.ban.usage"));
		setArgumentsRange(2, -1);
		addKey("user ban");
		addKey("ban");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.user.ban"))
		{
			final User byUser = sender instanceof User ? (User) sender : null;
			final User forUser = manager.getUser(manager.findUserId(arguments.get(0)));
			if (forUser != null)
			{
				if ((byUser == null) || (permissionHandler.getVariableAsLong(forUser, "weight") < permissionHandler.getVariableAsLong(byUser, "weight")))
				{
					final Timestamp from = TimestampUtil.getCurrentTimestamp();
					final Timestamp to = TimestampUtil.getTimestampForDiff(from, arguments.get(1), true);
					final long difference = to.getTime() - from.getTime();
					final long maximum = permissionHandler.getVariableAsLong(byUser, "bantime");
					StringBuilder reason = null;
					if (arguments.size() > 2)
					{
						reason = new StringBuilder();
						for (int i = 2; i < arguments.size(); ++i)
						{
							reason.append(arguments.get(i)).append(' ');
						}
						reason.setLength(reason.length() - 1);
					}

					if ((maximum == -1) || (difference <= maximum))
					{
						if (reason != null)
						{
							forUser.addBanPunishment(reason.toString(), byUser, from, to);
							sender.sendMessage(tl("cyclone.user.banwithdesc", forUser.getName(), TimestampUtil.formatTimestamp(from, to), reason.toString()));
						}
						else
						{
							forUser.addBanPunishment(null, byUser, from, to);
							sender.sendMessage(tl("cyclone.user.ban", forUser.getName(), TimestampUtil.formatTimestamp(from, to)));
						}
					}
					else
					{
						sender.sendMessage(tl("cyclone.user.bantoolong", TimestampUtil.formatTimestamp(new Timestamp(maximum))));
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
