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

package com.valaphee.cyclone.status.data;

import com.valaphee.cyclone.cache.Cache;
import com.valaphee.cyclone.cache.RedisCache;
import com.valaphee.cyclone.serialization.Serializer;
import redis.clients.jedis.JedisPool;

/**
 * Default
 *
 * @author valaphee
 */
public final class StatusRepository
{
	private final Cache<Void, StatusData> cache;

	public StatusRepository(final JedisPool jedisPool, final Serializer serializer)
	{
		serializer.register(StatusData.class);
		cache = new RedisCache<>("cyclone:status", null, null, jedisPool, serializer, Void.class, StatusData.class);
	}

	public StatusData getStatus()
	{
		return cache.get(null);
	}

	public void setStatus(final StatusData status)
	{
		cache.put(null, status);
	}
}
