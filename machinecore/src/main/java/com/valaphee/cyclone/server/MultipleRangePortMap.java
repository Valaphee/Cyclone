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

/**
 * Default
 *
 * @author valaphee
 */
public final class MultipleRangePortMap
		implements PortMap
{
	private final SingleRangePortMap[] portMaps;

	public MultipleRangePortMap(final int... ranges)
	{
		if ((ranges.length % 2) != 0)
		{
			throw new IllegalArgumentException("You can only use a base of two array.");
		}

		portMaps = new SingleRangePortMap[(ranges.length / 2)];
		for (int i = 0; i < ranges.length; i += 2)
		{
			portMaps[(i / 2)] = new SingleRangePortMap(ranges[i], ranges[(i + 1)]);
		}
	}

	@Override
	public int occupyPort()
	{
		for (final SingleRangePortMap portMap : portMaps)
		{
			final int port = portMap.occupyPort();
			if (port != 0)
			{
				return port;
			}
		}

		return 0;
	}

	@Override
	public boolean occupyPort(final int port)
	{
		for (final SingleRangePortMap portMap : portMaps)
		{
			if (portMap.occupyPort(port))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean freePort(final int port)
	{
		for (final SingleRangePortMap portMap : portMaps)
		{
			if (portMap.freePort(port))
			{
				return true;
			}
		}

		return false;
	}
}
