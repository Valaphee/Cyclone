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

import com.valaphee.cyclone.account.AccountManager;
import com.valaphee.cyclone.account.Player;
import com.valaphee.cyclone.command.Command;
import com.valaphee.cyclone.command.CommandException;
import com.valaphee.cyclone.command.CommandSender;
import static com.valaphee.cyclone.language.I18n.tl;
import com.valaphee.cyclone.server.Server;
import com.valaphee.cyclone.server.ServerManager;
import java.util.List;
import java.util.UUID;

/**
 * Default
 *
 * @author valaphee
 */
public final class ServerSwitchCommand
		extends Command
{
	private final ServerManager manager;
	private final AccountManager accountManager;

	public ServerSwitchCommand(final ServerManager manager, final AccountManager accountManager)
	{
		this.manager = manager;
		this.accountManager = accountManager;

		setName(tl("cyclone.server.command.switch"));
		setDescription(tl("cyclone.server.command.switch.desc"));
		setUsage(tl("cyclone.server.command.switch.usage"));
		setArgumentsRange(1, 2);
		addKey("server switch");
		addKey("switch");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.server.switch"))
		{
			try
			{
				final Server server = manager.getServer(UUID.fromString(arguments.get(0)), true);
				if (arguments.size() > 1)
				{
					if (sender.hasPermission("cyclone.server.switch.other"))
					{
						final Player forPlayer = (Player) accountManager.getUser(accountManager.findUserId(arguments.get(1)));
						if (forPlayer != null)
						{
							if ((forPlayer.getProxyServer() != null) && (forPlayer.getServer() != null))
							{
								if (!forPlayer.getServer().equals(server.getId()))
								{
									forPlayer.setServer(server);
									sender.sendMessage(tl("cyclone.server.switchother", server.getId(), forPlayer.getName()));
								}
								else
								{
									sender.sendMessage(tl("cyclone.server.alreadyswitchother", server.getId(), forPlayer.getName()));
								}
							}
							else
							{
								sender.sendMessage(tl("cyclone.user.notonline", arguments.get(1)));
							}
						}
						else
						{
							sender.sendMessage(tl("cyclone.user.notfound", arguments.get(1)));
						}
					}
					else
					{
						sender.sendMessage(tl("cyclone.nopermission"));
					}
				}
				else if (sender instanceof Player)
				{
					final Player byPlayer = (Player) sender;
					if (!byPlayer.getServer().equals(server.getId()))
					{
						byPlayer.setServer(server);
						sender.sendMessage(tl("cyclone.server.switch", server.getId()));
					}
					else
					{
						sender.sendMessage(tl("cyclone.server.alreadyswitch", server.getId()));
					}
				}
				else
				{
					sender.sendMessage(tl("cyclone.noconsole"));
				}
			}
			catch (final IllegalArgumentException ex)
			{
				sender.sendMessage(tl("cyclone.server.notfound", arguments.get(0)));
			}
		}
		else
		{
			sender.sendMessage(tl("cyclone.nopermission"));
		}
	}
}
