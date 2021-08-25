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

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.valaphee.cyclone.context.Context;
import com.valaphee.cyclone.engine.Engine;
import com.valaphee.cyclone.engine.EngineMode;
import com.valaphee.cyclone.level.LevelManager;
import com.valaphee.cyclone.support.SupportManager;

/**
 * Default
 *
 * @author valaphee
 */
public final class DefaultMode
		implements EngineMode
{
	private TS3Api teamspeak;
	private SupportManager supportManager;
	private LevelManager levelManager;

	@Override
	public void startup(final Engine engine)
	{
		final Context context = engine.getContext();
		teamspeak = context.get(TS3Api.class);
		supportManager = new SupportManager(context);
		levelManager = new LevelManager(context);
	}

	@Override
	public void update(final float delta)
	{
		supportManager.update();
		levelManager.update();
	}

	@Override
	public void shutdown()
	{}
}
