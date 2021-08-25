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

package com.valaphee.cyclone.server;

import com.valaphee.cyclone.server.data.ServerData;
import java.util.UUID;

/**
 * Default
 *
 * @author valaphee
 */
public abstract class Server
{
	private final ServerCategory category;
	protected ServerData data;

	public Server(final ServerCategory category, final ServerData data)
	{
		this.category = category;
		this.data = data;
	}

	public final UUID getId()
	{
		return data.id;
	}

	public final ServerCategory getCategory()
	{
		return category;
	}

	public final UUID getNode()
	{
		return data.node;
	}

	public final boolean isPersistent()
	{
		return data.persistent;
	}

	public void setPersistent(final boolean persistent)
	{
		data.persistent = persistent;
	}

	public final String getHost()
	{
		return data.host;
	}

	public final int getPort()
	{
		return data.port;
	}

	public final int getPlayerLimit()
	{
		return data.playerLimit;
	}

	public final int getPlayerCount()
	{
		return data.playerCount;
	}

	public final boolean isFull()
	{
		return data.playerCount >= data.playerLimit;
	}

	public void setPlayerCount(final int playerCount)
	{
		data.playerCount = playerCount;
	}

	public final double getTicksPerSecond()
	{
		return data.ticksPerSecond;
	}

	public void setTicksPerSecond(final double ticksPerSecond)
	{
		data.ticksPerSecond = ticksPerSecond;
	}

	public final ServerData.State getState()
	{
		return data.state;
	}

	public void setState(final ServerData.State state)
	{
		data.state = state;
	}

	public abstract void start();

	public abstract void stop();

	public abstract void update();
}
