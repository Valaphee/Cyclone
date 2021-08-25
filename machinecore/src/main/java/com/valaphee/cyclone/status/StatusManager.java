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

package com.valaphee.cyclone.status;

import com.valaphee.cyclone.context.Context;
import com.valaphee.cyclone.serialization.Serializer;
import com.valaphee.cyclone.server.Server;
import com.valaphee.cyclone.server.ServerManager;
import com.valaphee.cyclone.status.data.StatusData;
import com.valaphee.cyclone.status.data.StatusRepository;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import redis.clients.jedis.JedisPool;

/**
 * Default
 *
 * @author valaphee
 */
public final class StatusManager
{
	final Context context;
	private final StatusRepository repository;
	private StatusData status;

	public StatusManager(final Context context)
	{
		this.context = context;
		repository = new StatusRepository(this.context.get(JedisPool.class), this.context.get(Serializer.class));

		updateStatus();
	}

	private void updateStatus()
	{
		status = repository.getStatus();
		if (status == null)
		{
			status = new StatusData();
			saveStatus();
		}
	}

	private void saveStatus()
	{
		repository.setStatus(status);
	}

	public String getName()
	{
		return status.name;
	}

	public void setName(final String name)
	{
		updateStatus();
		status.name = name;
		saveStatus();
	}

	public String getVersion()
	{
		return status.version;
	}

	public void setVersion(final String version)
	{
		updateStatus();
		status.version = version;
		saveStatus();
	}

	public List<String> getSamples()
	{
		return status.samples;
	}

	public void setSamples(final List<String> samples)
	{
		updateStatus();
		status.samples = samples;
		saveStatus();
	}

	public String getDescription()
	{
		return status.description;
	}

	public void setDescription(final String description)
	{
		updateStatus();
		status.description = description;
		saveStatus();
	}

	public String getShortDescription()
	{
		return status.shortDescription;
	}

	public void setShortDescription(final String shortDescription)
	{
		updateStatus();
		status.shortDescription = shortDescription;
		saveStatus();
	}

	public byte[] getFavicon()
	{
		return status.favicon;
	}

	public void setFavicon(final RenderedImage favicon)
	{
		updateStatus();
		if (favicon != null)
		{
			try
			{
				final ByteArrayOutputStream stream = new ByteArrayOutputStream();
				ImageIO.write(favicon, "PNG", stream);
				status.favicon = stream.toByteArray();
			}
			catch (final IOException ignore)
			{}
		}
		else
		{
			status.favicon = null;
		}
		saveStatus();
	}

	public int getPlayerLimit()
	{
		return status.playerLimit;
	}

	public int getPlayerCount()
	{
		return status.playerCount;
	}

	public boolean isMaintenance()
	{
		return status.maintenance;
	}

	public void setMaintenance(final boolean maintenance)
	{
		updateStatus();
		status.maintenance = maintenance;
		saveStatus();
	}

	public void update()
	{
		updateStatus();
		int playerLimit = 0;
		int playerCount = 0;
		final ServerManager serverManager = context.get(ServerManager.class);
		for (final Server server : serverManager.getSubcategory("bungeecord").getServers(true))
		{
			playerLimit += server.getPlayerLimit();
			playerCount += server.getPlayerCount();
		}
		status.playerLimit = playerLimit;
		status.playerCount = playerCount;
		saveStatus();
	}
}
