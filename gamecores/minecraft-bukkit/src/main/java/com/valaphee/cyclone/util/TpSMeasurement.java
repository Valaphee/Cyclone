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

package com.valaphee.cyclone.util;

/**
 * Default
 *
 * @author valaphee
 */
public final class TpSMeasurement
		implements Runnable
{
	private static final long[] TICKS = new long[500];
	private static int tick = 0;

	public static double getTpS(final int ticks)
	{
		if (tick < ticks)
		{
			return 20.0D;
		}

		return ticks / ((System.currentTimeMillis() - TICKS[(tick - 1 - ticks) % TICKS.length]) / 1000.0D);
	}

	public static double getTpS()
	{
		return getTpS(100);
	}

	@Override
	public void run()
	{
		TICKS[(tick % TICKS.length)] = System.currentTimeMillis();

		tick++;
	}
}
