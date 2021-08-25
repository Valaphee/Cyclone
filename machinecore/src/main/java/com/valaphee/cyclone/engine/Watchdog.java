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

package com.valaphee.cyclone.engine;

import static com.valaphee.cyclone.Cyclone.SYSERR;
import static com.valaphee.cyclone.Cyclone.TERMINAL;
import com.valaphee.cyclone.util.StringUtil;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import org.apache.logging.log4j.Level;

/**
 * Default
 *
 * @author valaphee
 */
public final class Watchdog
		extends Thread
{
	private static final long TIMEOUT = 10_000L;
	private volatile long last;
	private boolean paused;

	public Watchdog()
	{
		super("Engine Watchdog");

		last = System.currentTimeMillis();
	}

	@Override
	public void run()
	{
		try
		{
			for (;;)
			{
				final long delta = System.currentTimeMillis() - last;
				if ((last != 0L) && (delta > TIMEOUT) && (!paused))
				{
					Engine.LOGGER.log(Level.FATAL, "Main loop has stopped responding (Delta: {}, Timeout: {}).", delta, TIMEOUT);

					SYSERR.println();
					final ThreadInfo[] threads = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);
					for (final ThreadInfo thread : threads)
					{
						SYSERR.println(StringUtil.center("] " + thread.getThreadName() + " [", TERMINAL.getWidth(), '-'));
						SYSERR.println("Suspended: " + thread.isSuspended());
						SYSERR.println("Native: " + thread.isInNative());
						SYSERR.println("State: " + thread.getThreadState());
						if (thread.getLockedMonitors().length != 0)
						{
							SYSERR.println("Locked Monitors:");
							for (final MonitorInfo monitor : thread.getLockedMonitors())
							{
								SYSERR.println("\t" + monitor.getLockedStackFrame().toString());
							}
						}
						if (thread.getStackTrace().length != 0)
						{
							SYSERR.println("Stack Trace:");
							for (final StackTraceElement stack : thread.getStackTrace())
							{
								SYSERR.println("\t" + stack.toString());
							}
						}
					}

					Runtime.getRuntime().halt(1);
				}

				sleep(TIMEOUT);
			}
		}
		catch (final InterruptedException ignore)
		{}
	}

	public void tick(final long time)
	{
		this.last = time;
	}

	public void setPaused(final boolean paused)
	{
		this.paused = paused;
	}
}
