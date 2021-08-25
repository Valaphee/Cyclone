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

import com.valaphee.cyclone.Cyclone;
import com.valaphee.cyclone.CycloneConstants;
import com.valaphee.cyclone.context.Context;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Default
 *
 * @author valaphee
 */
public class Engine
		implements Runnable
{
	private static final long MEBIBYTES_IN_BYTES = 1024L * 1024L;
	static final Logger LOGGER = LogManager.getLogger(Engine.class);
	private final Context context = new Context();
	private final Time time = new Time();
	private final Watchdog watchdog = new Watchdog();
	private final Deque<EngineSubsystem> subsystems = new ArrayDeque<>();
	private final EngineMode initialMode;
	private EngineMode currentMode;
	private EngineMode pendingMode;
	private volatile boolean running, stopped;

	public Engine(final Collection<EngineSubsystem> subsystems, final EngineMode initialMode)
	{
		this.subsystems.addAll(subsystems);
		this.initialMode = initialMode;
		context.set(Time.class, time);
		context.set(Watchdog.class, watchdog);
	}

	public Context getContext()
	{
		return new Context(context);
	}

	public <T> T getFromContext(final Class<? extends T> type)
	{
		return context.get(type);
	}

	public Time getTime()
	{
		return time;
	}

	public Watchdog getWatchdog()
	{
		return watchdog;
	}

	public void changeMode(final EngineMode mode)
	{
		if (currentMode == null)
		{
			currentMode = mode;
			currentMode.startup(this);
		}
		else
		{
			pendingMode = mode;
		}
	}

	public boolean isRunning()
	{
		return running;
	}

	public boolean isStopped()
	{
		return stopped;
	}

	private boolean startup()
	{
		final Thread engine = Thread.currentThread();
		engine.setName("Engine");
		engine.setPriority(Thread.MAX_PRIORITY);
		Runtime.getRuntime().addShutdownHook(new Thread(() ->
		{
			if (!stopped)
			{
				stopped = true;
				running = false;

				shutdown();
				watchdog.interrupt();

				Cyclone.SYSOUT.println("Goodbye and see you soon!");
				Cyclone.SYSOUT.println("And... Don't kill me with Ctrl+C thats hurt.");
			}
		}, "Engine Shutdown Hook"));

		LOGGER.log(Level.INFO, "Java: {} in \"{}\"", CycloneConstants.JAVA_VERSION, CycloneConstants.JAVA_HOME_DIRECTORY);
		LOGGER.log(Level.INFO, "Java VM: {}, {}", CycloneConstants.JAVA_VM_NAME, CycloneConstants.JAVA_VM_VERSION);
		LOGGER.log(Level.INFO, "Java Runtime: {}MiB on {} cores", Runtime.getRuntime().maxMemory() / MEBIBYTES_IN_BYTES, Runtime.getRuntime().availableProcessors());

		try
		{
			subsystems.forEach((subsystem) ->
			{
				subsystem.preStartup(context);
			});

			subsystems.forEach((subsystem) ->
			{
				subsystem.startup(context);
			});
		}
		catch (final Throwable thrown)
		{
			LOGGER.log(Level.FATAL, "An exception occured while starting subsystems.", thrown);

			return false;
		}

		Runtime.getRuntime().gc();

		return true;
	}

	private void shutdown()
	{
		if (currentMode != null)
		{
			currentMode.shutdown();
			currentMode = null;
		}

		subsystems.descendingIterator().forEachRemaining((subsystem) ->
		{
			try
			{
				subsystem.preShutdown();
			}
			catch (final Throwable thrown)
			{
				LOGGER.log(Level.ERROR, "One subsystem cannot be prepared to shutting down.", thrown);
			}
		});

		subsystems.descendingIterator().forEachRemaining((subsystem) ->
		{
			try
			{
				subsystem.shutdown();
			}
			catch (final Throwable thrown)
			{
				LOGGER.log(Level.ERROR, "One subsystem cannot be shutted down.", thrown);
			}
		});

		Runtime.getRuntime().gc();
	}

	@Override
	public void run()
	{
		if (!running)
		{
			running = true;
			try
			{
				watchdog.start();
				if (startup() && (!stopped))
				{
					changeMode(initialMode);
					while (running)
					{
						if (pendingMode != null)
						{
							if (currentMode != null)
							{
								currentMode.shutdown();
							}
							currentMode = pendingMode;
							currentMode.startup(this);
							pendingMode = null;
						}
						if (currentMode == null)
						{
							running = false;
						}

						final Iterator<Float> cycles = time.tick();
						subsystems.forEach((subsystem) ->
						{
							subsystem.preUpdate(time.getRealDelta());
						});

						while (cycles.hasNext())
						{
							currentMode.update(cycles.next());
						}

						subsystems.forEach((subsystem) ->
						{
							subsystem.postUpdate(time.getRealDelta());
						});

						watchdog.tick(time.getRealTime());
					}
				}
			}
			catch (final Throwable thrown)
			{
				LOGGER.log(Level.FATAL, "An exception occured while running main loop.", thrown);
			}
		}

		if (!stopped)
		{
			stopped = true;

			shutdown();
			watchdog.interrupt();

			LOGGER.log(Level.INFO, "Goodbye and see you soon!");
		}
	}

	public void stop()
	{
		running = false;
	}
}
