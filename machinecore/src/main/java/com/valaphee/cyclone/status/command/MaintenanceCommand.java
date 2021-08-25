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

package com.valaphee.cyclone.status.command;

import com.valaphee.cyclone.account.AccountManager;
import com.valaphee.cyclone.account.PermissionHandler;
import com.valaphee.cyclone.account.Player;
import com.valaphee.cyclone.command.Command;
import com.valaphee.cyclone.command.CommandException;
import com.valaphee.cyclone.command.CommandSender;
import static com.valaphee.cyclone.language.I18n.tl;
import com.valaphee.cyclone.status.StatusManager;
import java.util.List;

/**
 * Default
 *
 * @author valaphee
 */
public final class MaintenanceCommand
		extends Command
{
	private final StatusManager statusManager;
	private final AccountManager accountManager;
	private final PermissionHandler permissionHandler;

	public MaintenanceCommand(final StatusManager statusManager, final AccountManager accountManager, final PermissionHandler permissionHandler)
	{
		this.statusManager = statusManager;
		this.accountManager = accountManager;
		this.permissionHandler = permissionHandler;

		setName(tl("cyclone.status.command.maintenance"));
		setDescription(tl("cyclone.status.command.maintenance.desc"));
		setUsage(tl("cyclone.status.command.maintenance.usage"));
		setArgumentsRange(0, 0);
		addKey("maintenance");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.maintenance.toggle"))
		{
			statusManager.setMaintenance(!statusManager.isMaintenance());
			if (statusManager.isMaintenance())
			{
				final StringBuilder message = new StringBuilder();
				message.append(tl("cyclone.user.disconnect.header")).append('\n');
				message.append(tl("cyclone.user.disconnect.title.kick")).append("\n\n");
				message.append(tl("cyclone.user.disconnect.cause.maintenance")).append('\n');
				message.append(tl("cyclone.user.disconnect.footer"));
				final String messageToString = message.toString();
				accountManager.getUsers().stream().filter((user) -> !permissionHandler.has(user, "cyclone.maintenance")).forEach((user) -> ((Player) user).kick(messageToString));

				sender.sendMessage(tl("cyclone.maintenance.enable"));
			}
			else
			{
				sender.sendMessage(tl("cyclone.maintenance.disable"));
			}
		}
		else
		{
			sender.sendMessage(tl("cyclone.nopermission"));
		}
	}
}
