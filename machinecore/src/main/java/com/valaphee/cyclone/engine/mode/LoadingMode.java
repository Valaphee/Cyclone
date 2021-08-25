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

package com.valaphee.cyclone.engine.mode;

import com.valaphee.cyclone.Cyclone;
import com.valaphee.cyclone.context.Context;
import com.valaphee.cyclone.engine.Engine;
import com.valaphee.cyclone.engine.EngineMode;
import com.valaphee.cyclone.engine.Time;
import com.valaphee.cyclone.engine.mode.loadprocesses.*;
import java.util.ArrayDeque;
import java.util.Queue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Default
 *
 * @author valaphee
 */
public final class LoadingMode
		implements EngineMode
{
	static final Logger LOGGER = LogManager.getLogger(LoadingMode.class);
	private Context context;
	private Engine engine;
	private final Queue<LoadProcess> processes = new ArrayDeque<>();
	private LoadProcess currentProcess;
	private int progress;
	private int expectedCost;

	@Override
	public void startup(final Engine engine)
	{
		context = engine.getContext();
		this.engine = engine;

		init();

		progress = 0;
		expectedCost = 0;
		processes.forEach((process) ->
		{
			expectedCost += process.getExpectedCost();
		});

		Cyclone.PROGRESS.setActive(true);

		popStep();
	}

	private void init()
	{
		processes.add(new InitializeConnectionManager(context));
		processes.add(new InitializeAndSetupAccountManager(context));
		processes.add(new InitializeAndSetupServerManager(context));
		processes.add(new InitializeAndSetupStatusManager(context));
		processes.add(new InitializeConnection(context));
	}

	private void popStep()
	{
		if (currentProcess != null)
		{
			progress += currentProcess.getExpectedCost();
		}
		currentProcess = null;
		if (!processes.isEmpty())
		{
			currentProcess = processes.remove();
		}
	}

	@Override
	public void update(final float delta)
	{
		final Time time = context.get(Time.class);
		long startTime = time.getRealTime();
		while ((currentProcess != null) && ((time.getRealTime() - startTime) < 20L))
		{
			if (currentProcess.step())
			{
				popStep();
			}
		}

		if (currentProcess == null)
		{
			engine.changeMode(new DefaultMode(context));
		}
		else
		{
			Cyclone.PROGRESS.update((progress + currentProcess.getExpectedCost() * currentProcess.getProgress()) / expectedCost);
		}
	}

	@Override
	public void shutdown()
	{
		Cyclone.PROGRESS.setActive(false);
	}
}
