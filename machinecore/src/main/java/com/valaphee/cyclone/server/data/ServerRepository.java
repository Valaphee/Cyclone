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

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.TxScope;
import com.valaphee.cyclone.cache.Cache;
import com.valaphee.cyclone.cache.CacheLoader;
import com.valaphee.cyclone.cache.EvictionListener;
import com.valaphee.cyclone.cache.RedisCache;
import com.valaphee.cyclone.serialization.Serializer;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import redis.clients.jedis.JedisPool;

/**
 * Default
 *
 * @author valaphee
 */
public final class ServerRepository
		implements CacheLoader<UUID, ServerData>, EvictionListener<ServerData>
{
	private final Cache<UUID, ServerData> cache;
	private final String serverCategory;
	private final EbeanServer database;

	public ServerRepository(final JedisPool redisPool, final Serializer serializer, final EbeanServer database, final String serverCategory)
	{
		cache = new RedisCache<>("cyclone:server:" + (this.serverCategory = serverCategory), this, this, redisPool, serializer, UUID.class, ServerData.class);
		this.database = database;
	}

	public Collection<UUID> getAllServers()
	{
		return database.find(ServerTable.class).where().eq("serverCategory", database.find(ServerCategoryTable.class).where().eq("name", serverCategory).findUnique()).findIds();
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

	@Override
	public ServerData load(final UUID key)
	{
		final ServerTable server = database.find(ServerTable.class, key);
		if (server != null)
		{
			final ServerData data = new ServerData();

			data.id = server.getId();
			data.serverCategory = server.getServerCategory().getName();
			data.node = server.getNode();
			data.persistent = true;

			return data;
		}

		return null;
	}

	@Override
	public void evict(final Object key, final ServerData value)
	{
		if (value.persistent)
		{
			final TxScope scope = TxScope.requiresNew();
			Ebean.execute(scope, () ->
			{
				ServerTable server = database.find(ServerTable.class, key);
				if (server == null)
				{
					server = new ServerTable((UUID) key);
				}

				final String[] nopName = value.serverCategory.split(":");
				server.setServerCategory(database.find(ServerCategoryTable.class).where().eq("name", nopName[nopName.length - 1]).findUnique());
				server.setNode(value.node);

				database.save(server);
			});
		}
	}
}
