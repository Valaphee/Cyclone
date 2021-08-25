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
public final class SpecifiedPortMap
		implements PortMap
{
	private final int[] ports;
	private final boolean[] portsUsed;

	public SpecifiedPortMap(final int... ports)
	{
		for (int i = ports.length - 1; i >= 0; --i)
		{
			if ((ports[i] < 0) || (ports[i] > 65536))
			{
				throw new IllegalArgumentException("Port out of range.");
			}
		}

		this.ports = ports;
		portsUsed = new boolean[this.ports.length];
	}

	@Override
	public int occupyPort()
	{
		for (int i = 0; i < ports.length; ++i)
		{
			if (!portsUsed[i])
			{
				portsUsed[i] = true;

				return ports[i];
			}
		}

		return 0;
	}

	@Override
	public boolean occupyPort(final int port)
	{
		for (int i = 0; i < ports.length; ++i)
		{
			if (ports[i] == port)
			{
				if (!portsUsed[i])
				{
					return (portsUsed[i] = true);
				}
			}
		}

		return false;
	}

	@Override
	public boolean freePort(final int port)
	{
		for (int i = 0; i < ports.length; ++i)
		{
			if (ports[i] == port)
			{
				if (portsUsed[i])
				{
					return !(portsUsed[i] = false);
				}

				break;
			}
		}

		return false;
	}
}
