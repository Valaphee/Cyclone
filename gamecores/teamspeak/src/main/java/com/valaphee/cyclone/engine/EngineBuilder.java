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

import java.util.ArrayList;
import java.util.List;

/**
 * Default
 *
 * @author valaphee
 */
public final class EngineBuilder
{
	private final List<EngineSubsystem> subsystems = new ArrayList<>();
	private EngineMode initialMode;

	public EngineBuilder addSubsystem(final EngineSubsystem subsystem)
	{
		subsystems.add(subsystem);

		return this;
	}

	public EngineBuilder setInitialMode(final EngineMode mode)
	{
		initialMode = mode;

		return this;
	}

	public Engine build()
	{
		return new Engine(subsystems, initialMode);
	}
}
