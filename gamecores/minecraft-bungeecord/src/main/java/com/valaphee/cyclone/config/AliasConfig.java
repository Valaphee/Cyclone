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

package com.valaphee.cyclone.config;

import java.util.HashMap;
import java.util.Map;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;

/**
 * Default
 *
 * @author valaphee
 */
public final class AliasConfig
{
	private final Map<String, String> aliases = new HashMap<>();

	public AliasConfig()
	{
		aliases.put("confirm", "confirm");
	}

	public Map<String, String> getAliases()
	{
		return aliases;
	}

	public void setAliases(final Map<String, String> aliases)
	{
		this.aliases.clear();
		this.aliases.putAll(aliases);
	}

	public void load(final Ini configuration)
	{
		final Section section = configuration.get("aliases");
		if ((section != null) && (!section.isEmpty()))
		{
			setAliases(section);
		}
	}

	public void save(final Ini configuration)
	{
		Section section = configuration.get("aliases");
		if (section == null)
		{
			section = configuration.add("aliases");
		}

		if (!getAliases().isEmpty())
		{
			section.putAll(getAliases());
		}
	}
}
