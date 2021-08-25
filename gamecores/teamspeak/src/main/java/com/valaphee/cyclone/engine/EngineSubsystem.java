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

import com.valaphee.cyclone.context.Context;

/**
 * Default
 *
 * @author valaphee
 */
public interface EngineSubsystem
{
	String getName();

	default void preStartup(Context context)
	{}

	default void startup(Context context)
	{}

	default void preUpdate(float delta)
	{}

	default void postUpdate(float delta)
	{}

	default void preShutdown()
	{}

	default void shutdown()
	{}
}
