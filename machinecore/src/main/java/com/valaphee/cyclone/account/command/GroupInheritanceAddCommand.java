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
public final class GroupInheritanceAddCommand
		extends Command
{
	private final AccountManager manager;

	public GroupInheritanceAddCommand(final AccountManager manager)
	{
		this.manager = manager;

		setName(tl("cyclone.group.command.inheritance.add"));
		setDescription(tl("cyclone.group.command.inheritance.add.desc"));
		setUsage(tl("cyclone.group.command.inheritance.add.usage"));
		setArgumentsRange(2, -1);
		addKey("group inheritance add");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.group.inheritance.add"))
		{
			final Group forGroup = manager.getGroup(manager.findGroupId(arguments.get(0)));
			if (forGroup != null)
			{
				for (int i = 1; i < arguments.size(); ++i)
				{
					final Group inheritance = manager.getGroup(manager.findGroupId(arguments.get(i)));
					if (inheritance != null)
					{
						forGroup.addInheritance(inheritance);
						sender.sendMessage(tl("cyclone.group.inheritance.add", forGroup.getName(), inheritance.getName()));
					}
					else
					{
						sender.sendMessage(tl("cyclone.group.notfound", arguments.get(i)));
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
