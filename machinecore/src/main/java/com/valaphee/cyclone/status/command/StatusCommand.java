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

import com.valaphee.cyclone.command.Command;
import com.valaphee.cyclone.command.CommandException;
import com.valaphee.cyclone.command.CommandSender;
import static com.valaphee.cyclone.language.I18n.tl;
import com.valaphee.cyclone.status.StatusManager;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Default
 *
 * @author valaphee
 */
public final class StatusCommand
		extends Command
{
	private final StatusManager manager;

	public StatusCommand(final StatusManager manager)
	{
		this.manager = manager;

		setName(tl("cyclone.status.command"));
		setDescription(tl("cyclone.status.command.desc"));
		setUsage(tl("cyclone.status.command.usage"));
		setArgumentsRange(1, -1);
		addKey("status");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cyclone.status"))
		{
			final String type = arguments.get(0);
			StringBuilder value = null;
			if (arguments.size() > 1)
			{
				value = new StringBuilder();
				for (int i = 1; i < arguments.size(); ++i)
				{
					value.append(arguments.get(i)).append(' ');
				}
				value.setLength(value.length() - 1);
			}
			boolean succeed = false;

			if (type.contains("name"))
			{
				manager.setName(value != null ? value.toString() : null);
				succeed = true;
			}
			else if (type.contains("version"))
			{
				manager.setVersion(value != null ? value.toString() : null);
				succeed = true;
			}
			else if (type.contains("samples"))
			{
				manager.setSamples(value != null ? Arrays.asList(value.toString().split(" ")) : null);
				succeed = true;
			}
			else if (type.contains("description"))
			{
				manager.setDescription(value != null ? value.toString().replace("&p", "\n") : null);
				succeed = true;
			}
			else if (type.contains("shortdescription"))
			{
				manager.setShortDescription(value != null ? value.toString() : null);
				succeed = true;
			}
			else if (type.contains("favicon"))
			{
				try
				{
					manager.setFavicon(value != null ? ImageIO.read(new File(value.toString())) : null);
					succeed = true;
				}
				catch (final IOException ex)
				{}
			}

			if (!succeed)
			{
				sender.sendMessage(tl("cyclone.status.failed", type));
			}
			else if (value != null)
			{
				sender.sendMessage(tl("cyclone.status", type, value.toString()));
			}
			else
			{
				sender.sendMessage(tl("cyclone.status.reset", type));
			}
		}
		else
		{
			sender.sendMessage(tl("cyclone.nopermission"));
		}
	}
}
