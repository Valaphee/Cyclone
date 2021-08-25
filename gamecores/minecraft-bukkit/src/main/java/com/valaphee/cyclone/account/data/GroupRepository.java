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

package com.valaphee.cyclone.account.data;

import com.valaphee.cyclone.cache.Cache;
import com.valaphee.cyclone.cache.RedisCache;
import com.valaphee.cyclone.serialization.Serializer;
import java.util.Collection;
import redis.clients.jedis.JedisPool;

/**
 * Default
 *
 * @author valaphee
 */
public final class GroupRepository
{
	private final Cache<Long, GroupData> cache;

	public GroupRepository(final JedisPool redisPool, final Serializer serializer)
	{
		serializer.register(GroupData.class);
		cache = new RedisCache<>("cyclone:group", null, null, redisPool, serializer, Long.class, GroupData.class);
	}

	public Collection<GroupData> getCachedGroups()
	{
		return cache.getAll().values();
	}

	public GroupData getGroup(final long id)
	{
		return cache.get(id);
	}

	public void setGroup(final GroupData data)
	{
		cache.put(data.id, data);
	}

	public void removeGroup(final long id)
	{
		cache.invalidate(id);
	}

	public GroupData getDefaultGroup()
	{
		return cache.get(0L);
	}

	public void setDefaultGroup(final GroupData data)
	{
		cache.put(0L, data);
	}
}
