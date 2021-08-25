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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import org.ini4j.Ini;

/**
 * Default
 *
 * @author valaphee
 */
public final class Config
{
	private final MainConfig main = new MainConfig();
	private final CommunicationConfig communication = new CommunicationConfig();
	private final DatabaseConfig database = new DatabaseConfig();
	private final CacheConfig cache = new CacheConfig();
	private final TeamSpeakConfig teamspeak = new TeamSpeakConfig();

	public void load()
	{
		final File file = new File("cyclone.conf");
		if (file.exists())
		{
			try
			{
				final Ini configuration = new Ini(new FileReader(file));

				main.load(configuration);
				communication.load(configuration);
				database.load(configuration);
				cache.load(configuration);
				teamspeak.load(configuration);
			}
			catch (final Throwable ex)
			{
				file.renameTo(new File("cyclone.broken.conf"));

				final File fileOld = new File("cyclone.old.conf");
				if (fileOld.exists())
				{
					fileOld.renameTo(file);
				}
			}
		}
	}

	public void save()
	{
		final File file = new File("cyclone.conf");
		final File fileOld = new File("cyclone.old.conf");

		try
		{
			final Ini configuration = new Ini();
			configuration.setComment("Cyclone Cloud Configuration");

			main.save(configuration);
			communication.save(configuration);
			database.save(configuration);
			cache.save(configuration);
			teamspeak.save(configuration);

			final File fileNew = new File("cyclone.new.conf");
			if (!fileNew.exists())
			{
				fileNew.createNewFile();
			}
			configuration.store(new FileWriter(fileNew));

			if (file.exists())
			{
				file.renameTo(fileOld);
			}
			if (fileNew.exists())
			{
				fileNew.renameTo(file);
			}
		}
		catch (final Throwable ex)
		{
			if (file.exists())
			{
				file.renameTo(new File("cyclone.broken.conf"));
			}
			if (fileOld.exists())
			{
				fileOld.renameTo(file);
			}
		}
	}

	public MainConfig getMain()
	{
		return main;
	}

	public CommunicationConfig getCommunication()
	{
		return communication;
	}

	public DatabaseConfig getDatabase()
	{
		return database;
	}

	public CacheConfig getCache()
	{
		return cache;
	}

	public TeamSpeakConfig getTeamSpeak()
	{
		return teamspeak;
	}
}
