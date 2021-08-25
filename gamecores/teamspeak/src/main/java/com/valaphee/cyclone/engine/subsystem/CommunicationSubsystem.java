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

import com.valaphee.cyclone.communication.Communication;
import com.valaphee.cyclone.context.Context;
import com.valaphee.cyclone.engine.EngineSubsystem;

/**
 * Default
 *
 * @author valaphee
 */
public final class CommunicationSubsystem
		implements EngineSubsystem
{
	private Communication communication;

	@Override
	public String getName()
	{
		return "Communication";
	}

	@Override
	public void preStartup(final Context context)
	{
		context.set(Communication.class, communication = new Communication());
	}

	@Override
	public void preUpdate(final float delta)
	{
		communication.update();
	}

	@Override
	public void shutdown()
	{
		if (communication != null)
		{
			communication.shutdown();
		}
	}
}
