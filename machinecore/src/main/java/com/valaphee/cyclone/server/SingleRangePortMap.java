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
public final class SingleRangePortMap
		implements PortMap
{
	private final int begin;
	private final int end;
	private final boolean[] ports;

	public SingleRangePortMap(int begin, int end)
	{
		if (((begin < 0) || (begin > 65536)) || ((end < 0) || (end > 65536)))
		{
			throw new IllegalArgumentException("Port out of range.");
		}

		if (begin > end)
		{
			begin ^= end;
			end ^= begin;
			begin ^= end;
		}
		this.begin = begin;
		this.end = end;
		ports = new boolean[(end - begin)];
	}

	@Override
	public int occupyPort()
	{
		for (int i = 0; i < ports.length; ++i)
		{
			if (!ports[i])
			{
				ports[i] = true;

				return (i + begin);
			}
		}

		return 0;
	}

	@Override
	public boolean occupyPort(final int port)
	{
		if ((port >= begin) && (port <= end))
		{
			if (!ports[(port - begin)])
			{
				return (ports[(port - begin)] = true);
			}
		}

		return false;
	}

	@Override
	public boolean freePort(final int port)
	{
		if ((port >= begin) && (port <= end))
		{
			if (ports[(port - begin)])
			{
				return !(ports[(port - begin)] = false);
			}
		}

		return false;
	}
}
