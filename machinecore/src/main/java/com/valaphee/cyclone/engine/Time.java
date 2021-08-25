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

import java.util.Iterator;
import org.apache.logging.log4j.Level;

/**
 * Default
 *
 * @author valaphee
 */
public final class Time
{
	private static final float RESYNC = 0.1f;
	private static final long CYCLE_CAP = 1_000L;
	private static final long CYCLE_LENGTH = 1_000L;
	private volatile long last;
	private volatile long realDelta;
	private volatile long gameTime;
	private volatile long gameDelta;
	private long desynch;

	public Time()
	{
		reset();
	}

	public Iterator<Float> tick()
	{
		long now = getRealTime();
		long delta = now - last;
		if (delta == 0)
		{
			Engine.LOGGER.log(Level.WARN, "Main loop is in idle.");

			try
			{
				Thread.sleep(1L);
			}
			catch (final InterruptedException ignore)
			{}

			now = getRealTime();
			delta = now - last;
		}
		last = now;
		realDelta = delta;

		if (delta >= CYCLE_CAP)
		{
			Engine.LOGGER.log(Level.WARN, "Main loop takes too much time (Delta: {}, Cap: {}).", delta, CYCLE_CAP);
			delta = CYCLE_CAP;
		}

		final int cycles = (int) ((delta - 1) / CYCLE_LENGTH) + 1;
		if (desynch != 0)
		{
			long difference = (long) Math.ceil(desynch * RESYNC);
			if (difference == 0)
			{
				difference = (long) Math.signum(desynch);
			}
			gameTime += difference;
			desynch -= difference;
		}
		if (cycles > 0)
		{
			gameDelta = delta / cycles;
		}

		return new TimeStepper(cycles, delta / cycles);
	}

	public long getRealTime()
	{
		return System.currentTimeMillis();
	}

	public long getRealDelta()
	{
		return realDelta;
	}

	public long getGameTime()
	{
		return gameTime;
	}

	public long getGameDelta()
	{
		return gameDelta;
	}

	public void update(final long time)
	{
		desynch = time - System.currentTimeMillis();
	}

	public void reset()
	{
		last = System.currentTimeMillis();
	}

	private final class TimeStepper
			implements Iterator<Float>
	{
		private final int cycles;
		private final long deltaPerCycle;
		private int currentCycle;

		private TimeStepper(final int cycles, final long deltaPerCycle)
		{
			this.cycles = cycles;
			this.deltaPerCycle = deltaPerCycle;
		}

		@Override
		public boolean hasNext()
		{
			return currentCycle < cycles;
		}

		@Override
		public Float next()
		{
			currentCycle++;
			gameTime += deltaPerCycle;

			return deltaPerCycle / 1_000f;
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
}
