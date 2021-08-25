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

import com.valaphee.cyclone.CycloneConstants;
import java.io.File;
import java.util.Locale;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;

/**
 * Default
 *
 * @author valaphee
 */
public final class MainConfig
{
	private String locale;
	private Long timeout;
	private Integer ticksPerSecond;
	private File sharePath;
	private File runPath;

	public Locale getLocale()
	{
		if (locale == null)
		{
			setLocale(Locale.getDefault(Locale.Category.DISPLAY));
		}

		return Locale.forLanguageTag(locale);
	}

	public void setLocale(final Locale locale)
	{
		this.locale = locale.toLanguageTag();
	}

	public Long getTimeout()
	{
		if (timeout == null)
		{
			timeout = 60000L;
		}

		return timeout;
	}

	public void setTimeout(final Long timeout)
	{
		this.timeout = timeout;
	}

	public Integer getTicksPerSecond()
	{
		if (ticksPerSecond == null)
		{
			ticksPerSecond = 5;
		}

		return ticksPerSecond;
	}

	public void setTicksPerSecond(final Integer ticksPerSecond)
	{
		this.ticksPerSecond = ticksPerSecond;
	}

	public File getSharePath()
	{
		if (sharePath == null)
		{
			sharePath = new File(CycloneConstants.USER_HOME_DIRECTORY, "cyclone").getAbsoluteFile();
		}

		return sharePath;
	}

	public void setSharePath(final File sharePath)
	{
		this.sharePath = sharePath;
	}

	public File getRunPath()
	{
		if (runPath == null)
		{
			runPath = new File(CycloneConstants.USER_WORKING_DIRECTORY, "cyclone").getAbsoluteFile();
		}

		return runPath;
	}

	public void setRunPath(final File runPath)
	{
		this.runPath = runPath;
	}

	public void load(final Ini configuration)
	{
		final Section section = configuration.get("main");
		if (section != null)
		{
			if (section.containsKey("locale"))
			{
				setLocale(Locale.forLanguageTag(section.get("locale")));
			}
			if (section.containsKey("timeout"))
			{
				setTimeout(section.get("timeout", Long.class));
			}
			if (section.containsKey("ticks-per-second"))
			{
				setTicksPerSecond(section.get("ticks-per-second", Integer.class));
			}
			if (section.containsKey("share-path"))
			{
				setSharePath(section.get("share-path", File.class));
			}
			if (section.containsKey("run-path"))
			{
				setRunPath(section.get("run-path", File.class));
			}
		}
	}

	public void save(final Ini configuration)
	{
		Section section = configuration.get("main");
		if (section == null)
		{
			section = configuration.add("main");
		}

		if (getLocale() != null)
		{
			section.put("locale", getLocale().toLanguageTag());
		}
		if (getTimeout() != null)
		{
			section.put("timeout", getTimeout());
		}
		if (getTicksPerSecond() != null)
		{
			section.put("ticks-per-second", getTicksPerSecond());
		}
		if (getSharePath() != null)
		{
			section.put("share-path", getSharePath());
		}
		if (getRunPath() != null)
		{
			section.put("run-path", getRunPath());
		}
	}
}
