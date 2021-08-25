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

import com.valaphee.cyclone.config.Config;
import com.valaphee.cyclone.config.Parameter;
import com.valaphee.cyclone.context.Context;
import com.valaphee.cyclone.engine.EngineSubsystem;

/**
 * Default
 *
 * @author valaphee
 */
public final class ConfigSubsystem
		implements EngineSubsystem
{
	private final Parameter parameter;
	private Config config;

	public ConfigSubsystem(final Parameter parameter)
	{
		this.parameter = parameter;
	}

	@Override
	public String getName()
	{
		return "Config";
	}

	@Override
	public void preStartup(final Context context)
	{
		context.set(Parameter.class, parameter);
		config = new Config();
		config.load();
		context.set(Config.class, config);
	}

	@Override
	public void shutdown()
	{
		if (config != null)
		{
			config.save();
		}
	}
}
