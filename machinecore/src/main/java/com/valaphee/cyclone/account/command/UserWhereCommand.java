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
public final class UserWhereCommand
		extends Command
{
	private final AccountManager manager;

	public UserWhereCommand(final AccountManager manager)
	{
		this.manager = manager;

		setName(tl("cyclone.user.command.where"));
		setDescription(tl("cyclone.user.command.where.desc"));
		setUsage(tl("cyclone.user.command.where.usage"));
		setArgumentsRange(0, 1);
		addKey("user whereami");
		addKey("whereami");
		addKey("user whereis");
		addKey("whereis");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.user.where.is") && (arguments.size() > 0))
		{
			final User forUser = manager.getUser(manager.findUserId(arguments.get(0)));
			if (forUser != null)
			{
				if ((forUser.getProxyServer() != null) && (forUser.getServer() != null))
				{
					sender.sendMessage(tl("cyclone.user.where.header"));
					sender.sendMessage(tl("cyclone.user.where.proxyserver", forUser.getProxyServer()));
					sender.sendMessage(tl("cyclone.user.where.server", forUser.getServer()));
					sender.sendMessage(tl("cyclone.user.where.footer"));
				}
				else
				{
					sender.sendMessage(tl("cyclone.user.notonline", arguments.get(0)));
				}
			}
			else
			{
				sender.sendMessage(tl("cyclone.user.notfound", arguments.get(0)));
			}
		}
		else if (sender.hasPermission("cyclone.user.where.ami"))
		{
			if (sender instanceof User)
			{
				final User byUser = (User) sender;
				sender.sendMessage(tl("cyclone.user.where.header"));
				sender.sendMessage(tl("cyclone.user.where.proxyserver", byUser.getProxyServer()));
				sender.sendMessage(tl("cyclone.user.where.server", byUser.getServer()));
				sender.sendMessage(tl("cyclone.user.where.footer"));
			}
			else
			{
				sender.sendMessage(tl("cyclone.noconsole"));
			}
		}
		else
		{
			sender.sendMessage(tl("cyclone.nopermission"));
		}
	}
}
