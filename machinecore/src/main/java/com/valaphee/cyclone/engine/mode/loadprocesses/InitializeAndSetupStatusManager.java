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

package com.valaphee.cyclone.engine.mode.loadprocesses;

import com.valaphee.cyclone.context.Context;
import com.valaphee.cyclone.engine.mode.LoadProcess;
import com.valaphee.cyclone.status.StatusManager;

/**
 * Default
 *
 * @author valaphee
 */
public final class InitializeAndSetupStatusManager
		implements LoadProcess
{
	private final StatusManager statusManager;

	public InitializeAndSetupStatusManager(final Context context)
	{
		context.set(StatusManager.class, statusManager = new StatusManager(context));
	}

	@Override
	public boolean step()
	{
		return true;
	}

	@Override
	public float getProgress()
	{
		return 1.0f;
	}

	@Override
	public int getExpectedCost()
	{
		return 1;
	}
}
