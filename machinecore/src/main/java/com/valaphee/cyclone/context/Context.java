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

package com.valaphee.cyclone.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default
 *
 * @author valaphee
 */
public final class Context
{
	private final Context parent;
	private final Map<Class<? extends Object>, Object> context = new ConcurrentHashMap<>();

	public Context()
	{
		this(null);
	}

	public Context(final Context parent)
	{
		this.parent = parent;
	}

	public <T> T get(final Class<? extends T> type)
	{
		return context.containsKey(type) ? type.cast(context.get(type)) : parent != null ? parent.get(type) : null;
	}

	public <T> void set(final Class<? extends T> type, final T object)
	{
		context.put(type, object);
	}
}
