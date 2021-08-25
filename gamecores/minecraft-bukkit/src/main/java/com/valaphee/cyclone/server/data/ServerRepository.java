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

package com.valaphee.cyclone.server.data;

import com.valaphee.cyclone.cache.Cache;
import com.valaphee.cyclone.cache.RedisCache;
import com.valaphee.cyclone.serialization.Serializer;
import java.util.Map;
import java.util.UUID;
import redis.clients.jedis.JedisPool;

/**
 * Default
 *
 * @author valaphee
 */
public final class ServerRepository
{
	private final Cache<UUID, ServerData> cache;

	public ServerRepository(final JedisPool redisPool, final Serializer serializer, final String serverCategory)
	{
		cache = new RedisCache<>("cyclone:server:" + serverCategory, null, null, redisPool, serializer, UUID.class, ServerData.class);
	}

	public Map<UUID, ServerData> getCachedServers()
	{
		return cache.getAll();
	}

	public ServerData getServer(final UUID id)
	{
		return cache.get(id);
	}

	public void setServer(final ServerData server)
	{
		cache.put(server.id, server);
	}

	public void removeServer(final UUID id)
	{
		cache.invalidate(id);
	}
}
