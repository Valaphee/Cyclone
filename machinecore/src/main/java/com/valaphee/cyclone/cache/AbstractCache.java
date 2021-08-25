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

package com.valaphee.cyclone.cache;

import java.util.Map;

/**
 * Default
 *
 * @author valaphee
 */
public abstract class AbstractCache<K, V>
		implements Cache<K, V>
{
	protected final String name;
	protected final CacheLoader<K, V> cacheLoader;
	protected final EvictionListener<V> evictionListener;

	public AbstractCache(final String name, final CacheLoader<K, V> cacheLoader, final EvictionListener<V> evictionListener)
	{
		this.name = name;
		this.cacheLoader = cacheLoader;
		this.evictionListener = evictionListener;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void putAll(final Map<? extends K, ? extends V> map)
	{
		map.entrySet().forEach((entry) ->
		{
			put(entry.getKey(), entry.getValue());
		});
	}

	@Override
	public void invalidateAll(final Iterable<?> keys)
	{
		keys.forEach((key) ->
		{
			invalidate(key);
		});
	}
}
