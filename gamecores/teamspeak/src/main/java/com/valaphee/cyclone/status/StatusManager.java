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
import com.valaphee.cyclone.status.data.StatusData;
import com.valaphee.cyclone.status.data.StatusRepository;
import java.util.List;
import redis.clients.jedis.JedisPool;

/**
 * Default
 *
 * @author valaphee
 */
public final class StatusManager
{
	private final StatusRepository repository;
	private StatusData status;

	public StatusManager(final Context context)
	{
		repository = new StatusRepository(context.get(JedisPool.class), context.get(Serializer.class));

		updateStatus();
	}

	private void updateStatus()
	{
		status = repository.getStatus();
	}

	public String getName()
	{
		return status.name;
	}

	public String getVersion()
	{
		return status.version;
	}

	public List<String> getSamples()
	{
		return status.samples;
	}

	public String getDescription()
	{
		return status.description;
	}

	public String getShortDescription()
	{
		return status.shortDescription;
	}

	public byte[] getFavicon()
	{
		return status.favicon;
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

	public void update()
	{
		updateStatus();
	}
}
