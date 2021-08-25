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

package com.valaphee.cyclone.server.command;

import com.valaphee.cyclone.account.User;
import com.valaphee.cyclone.command.Command;
import com.valaphee.cyclone.command.CommandException;
import com.valaphee.cyclone.command.CommandSender;
import static com.valaphee.cyclone.language.I18n.tl;
import com.valaphee.cyclone.server.Server;
import com.valaphee.cyclone.server.ServerManager;
import java.util.List;

/**
 * Default
 *
 * @author valaphee
 */
public final class ServerPersistentCommand
		extends Command
{
	private final ServerManager manager;

	public ServerPersistentCommand(final ServerManager manager)
	{
		this.manager = manager;

		setName(tl("cyclone.server.command.persistent"));
		setDescription(tl("cyclone.server.command.persistent.desc"));
		setUsage(tl("cyclone.server.command.persistent.usage"));
		setArgumentsRange(0, 1);
		addKey("server persistent");
		addKey("persistent");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.server.persistent"))
		{
			if (sender instanceof User)
			{
				final User byUser = (User) sender;
				final Server server = manager.getServer(byUser.getServer(), true);
				server.setPersistent(true);
				sender.sendMessage(tl("cyclone.server.persistent", server.getId()));
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
