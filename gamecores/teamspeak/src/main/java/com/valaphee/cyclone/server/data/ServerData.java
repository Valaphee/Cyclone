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

package com.valaphee.cyclone.server.data;

import com.valaphee.cyclone.serialization.annotation.Index;
import java.util.UUID;

/**
 * Default
 *
 * @author valaphee
 */
public final class ServerData
{
	@Index(0)
	public UUID id;
	@Index(1)
	public String serverCategory;
	@Index(2)
	public UUID node;
	@Index(3)
	public boolean persistent;
	@Index(4)
	public String host;
	@Index(5)
	public int port;
	@Index(6)
	public int playerLimit;
	@Index(7)
	public int playerCount;
	@Index(8)
	public double ticksPerSecond;
	@Index(9)
	public State state;
	@Index(10)
	public String stateDescription;

	@Override
	public boolean equals(final Object object)
	{
		if (object == this)
		{
			return true;
		}
		if (object instanceof ServerData)
		{
			final ServerData other = (ServerData) object;

			return id.equals(other.id);
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		return id.hashCode();
	}

	public static enum State
	{
		NOT_SPECIFIED, LOBBY, RUNNING, RESTARTING
	}
}
