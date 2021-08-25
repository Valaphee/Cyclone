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

package com.valaphee.cyclone.engine.subsystem;

import com.valaphee.cyclone.context.Context;
import com.valaphee.cyclone.engine.EngineSubsystem;
import com.valaphee.cyclone.engine.Time;

/**
 * Default
 *
 * @author valaphee
 */
public final class SleeperSubsystem
		implements EngineSubsystem
{
	private final long sleep;
	private long lastSleep;
	private Time time;

	public SleeperSubsystem(final float ticksPerSecond)
	{
		lastSleep = sleep = (long) (1_000L / ticksPerSecond);
	}

	@Override
	public String getName()
	{
		return "Sleeper";
	}

	@Override
	public void preStartup(final Context context)
	{
		time = context.get(Time.class);
	}

	@Override
	public void preUpdate(final float delta)
	{
		lastSleep = sleep - (time.getRealDelta() - lastSleep);
		if (lastSleep > 0L)
		{
			try
			{
				Thread.sleep(lastSleep);
			}
			catch (final InterruptedException ignore)
			{}
		}
	}
}
