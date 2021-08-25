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
import com.valaphee.cyclone.util.TimestampUtil;
import java.sql.Timestamp;
import java.util.List;

/**
 * Default
 *
 * @author valaphee
 */
public final class UserWhoCommand
		extends Command
{
	private final AccountManager manager;

	public UserWhoCommand(final AccountManager manager)
	{
		this.manager = manager;

		setName(tl("cyclone.user.command.who"));
		setDescription(tl("cyclone.user.command.who.desc"));
		setUsage(tl("cyclone.user.command.who.usage"));
		setArgumentsRange(0, 1);
		addKey("user whoami");
		addKey("whoami");
		addKey("user whois");
		addKey("whois");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.user.who.is") && (arguments.size() > 0))
		{
			final User forUser = manager.getUser(manager.findUserId(arguments.get(0)));
			if (forUser != null)
			{
				sender.sendMessage(tl("cyclone.user.who.header"));
				sender.sendMessage(tl("cyclone.user.who.id", forUser.getId()));
				sender.sendMessage(tl("cyclone.user.who.name", forUser.getName()));
				sender.sendMessage(tl("cyclone.user.who.group", forUser.getGroup().getName()));
				sender.sendMessage(tl("cyclone.user.who.ip", forUser.getIp()));
				sender.sendMessage(tl("cyclone.user.who.playedtime", TimestampUtil.formatTimestamp(new Timestamp(forUser.getPlayedTime()))));
				sender.sendMessage(tl("cyclone.user.who.lastjoined", forUser.getJoined()));
				sender.sendMessage(tl("cyclone.user.who.footer"));
			}
			else
			{
				sender.sendMessage(tl("cyclone.user.notfound", arguments.get(0)));
			}
		}
		else if (sender.hasPermission("cyclone.user.who.ami"))
		{
			if (sender instanceof User)
			{
				final User byUser = (User) sender;
				sender.sendMessage(tl("cyclone.user.who.header"));
				sender.sendMessage(tl("cyclone.user.who.id", byUser.getId()));
				sender.sendMessage(tl("cyclone.user.who.name", byUser.getName()));
				sender.sendMessage(tl("cyclone.user.who.group", byUser.getGroup().getName()));
				sender.sendMessage(tl("cyclone.user.who.ip", byUser.getIp()));
				sender.sendMessage(tl("cyclone.user.who.playedtime", TimestampUtil.formatTimestamp(new Timestamp(byUser.getPlayedTime()))));
				sender.sendMessage(tl("cyclone.user.who.lastjoined", byUser.getJoined()));
				sender.sendMessage(tl("cyclone.user.who.footer"));
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
