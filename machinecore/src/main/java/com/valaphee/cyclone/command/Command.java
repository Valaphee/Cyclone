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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Default
 *
 * @author valaphee
 */
public abstract class Command
{
	private final List<CommandKey> keys = new ArrayList<>();
	private String name;
	private String description;
	private String usage;
	private int minimumArguments;
	private int maximumArguments;

	public Collection<CommandKey> getKeys()
	{
		return Collections.unmodifiableCollection(keys);
	}

	public CommandKey getKeyFor(final String line)
	{
		return keys.stream().filter((key) -> key.getKeyPattern().matcher(line).matches()).findFirst().orElse(null);
	}

	protected final void addKey(final String key)
	{
		keys.add(new CommandKey(key));
		Collections.sort(keys);
	}

	protected final void addKey(final String key, final int minimumArguments, final int maximumArguments)
	{
		keys.add(new CommandKey(key, minimumArguments, maximumArguments));
		Collections.sort(keys);
	}

	public String getName()
	{
		return name;
	}

	protected final void setName(final String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	protected final void setDescription(final String description)
	{
		this.description = description;
	}

	public String getUsage()
	{
		return usage;
	}

	protected final void setUsage(final String usage)
	{
		this.usage = usage;
	}

	public int getMinimumArguments()
	{
		return minimumArguments;
	}

	public int getMaximumArguments()
	{
		return maximumArguments;
	}

	public boolean checkArgumentsRange(final int count)
	{
		return (count >= minimumArguments) && ((maximumArguments == -1) || (count <= maximumArguments));
	}

	protected final void setArgumentsRange(final int minimumArguments, final int maximumArguments)
	{
		this.minimumArguments = minimumArguments;
		this.maximumArguments = maximumArguments;
	}

	public abstract void execute(CommandSender sender, String label, List<String> arguments)
			throws CommandException;

	public void usage(final CommandSender sender)
			throws CommandException
	{
		sender.sendMessage(tl("cyclone.usageheader", getName().toUpperCase()));
		sender.sendMessage(tl("cyclone.usagedescription", getDescription()));
		sender.sendMessage(tl("cyclone.usageusage", getUsage()));
		sender.sendMessage(tl("cyclone.usagefooter", getName().toUpperCase()));
	}
}
