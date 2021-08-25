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

import com.valaphee.cyclone.Cyclone;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.scheduler.TaskScheduler;

/**
 * Default
 *
 * @author valaphee
 */
public final class DenialOfServiceBlocker
{
	private final Map<InetAddress, Tracker> trackers = new ConcurrentHashMap<>();
	private final TaskScheduler scheduler;
	private long idle;
	private int requestsPerMinute;

	public DenialOfServiceBlocker(final TaskScheduler scheduler, final long idle, final int requestsPerMinute)
	{
		this.scheduler = scheduler;
		this.idle = idle;
		this.requestsPerMinute = requestsPerMinute;
	}

	public long getIdle()
	{
		return idle;
	}

	public void setIdle(final long idle)
	{
		this.idle = idle;
	}

	public int getRequestsPerMinute()
	{
		return requestsPerMinute;
	}

	public void setRequestsPerMinute(final int requestsPerMinute)
	{
		this.requestsPerMinute = requestsPerMinute;
	}

	public boolean available(final InetAddress address)
	{
		Tracker tracker = trackers.get(address);
		if (tracker == null)
		{
			trackers.put(address, tracker = new Tracker(address));
			scheduler.schedule(Cyclone.getInstance(), tracker, idle, TimeUnit.MILLISECONDS);
		}

		return tracker.available(System.currentTimeMillis());
	}

	private final class Tracker
			implements Runnable
	{
		private final InetAddress address;
		private final long[] timestamps;
		private int index;

		private Tracker(final InetAddress address)
		{
			this.address = address;
			timestamps = new long[requestsPerMinute];
		}

		public boolean available(final long now)
		{
			final long last = timestamps[index];
			timestamps[index] = now;
			index = (index + 1) % timestamps.length;

			return (last == 0) || ((now - last) > 60_000L);
		}

		@Override
		public void run()
		{
			final long last = timestamps[((index == 0 ? timestamps.length : index) - 1)];
			if ((last != 0) && ((System.currentTimeMillis() - last) < 1_000L))
			{
				scheduler.schedule(Cyclone.getInstance(), this, idle, TimeUnit.MILLISECONDS);
			}
			else
			{
				trackers.remove(address);
			}
		}
	}
}
