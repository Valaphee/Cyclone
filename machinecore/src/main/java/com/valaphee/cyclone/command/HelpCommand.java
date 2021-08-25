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

package com.valaphee.cyclone.command;

import static com.valaphee.cyclone.language.I18n.tl;
import java.util.ArrayList;
import java.util.List;

/**
 * Default
 *
 * @author valaphee
 */
public final class HelpCommand
		extends PaginatedCommand<Command>
{
	private final CommandHandler commandHandler;

	public HelpCommand(final CommandHandler commandHandler)
	{
		this.commandHandler = commandHandler;

		setName(tl("cyclone.command.help"));
		setDescription(tl("cyclone.command.help.desc"));
		setUsage(tl("cyclone.command.help.usage"));
		addKey("help");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
	{
		final Pagination pagination = getPagination(sender, arguments, commandHandler.getCommands());

		sender.sendMessage(tl("cyclone.helpheader", pagination.getPage(), pagination.getPages()));
		if (pagination.getItems().isEmpty())
		{
			sender.sendMessage(tl("cyclone.helpnothing"));
		}
		else
		{
			display(sender, pagination);
		}
		sender.sendMessage(tl("cyclone.helpfooter", pagination.getPage(), pagination.getPages()));
	}

	@Override
	public List<Command> run(final CommandSender sender, final Pagination pagination)
	{
		final String pattern = "(?i).*" + pagination.getFilter() + ".*";
		final List<Command> filtered = new ArrayList<>();
		pagination.getCollection().stream().forEach((command) ->
		{
			final StringBuilder keys = new StringBuilder();
			command.getKeys().forEach((key) ->
			{
				keys.append(key.getKey()).append(' ');
			});

			if (keys.toString().matches(pattern))
			{
				filtered.add(command);
			}
			else if (command.getName().matches(pattern))
			{
				filtered.add(command);
			}
			else if (command.getDescription().matches(pattern))
			{
				filtered.add(command);
			}
			else if (command.getUsage().matches(pattern))
			{
				filtered.add(command);
			}
		});

		return filtered;
	}

	@Override
	public String format(final Command item)
	{
		return tl("cyclone.helpentry", item.getUsage());
	}
}
