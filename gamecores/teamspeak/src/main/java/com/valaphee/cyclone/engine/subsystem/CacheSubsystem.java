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

import com.valaphee.cyclone.config.CacheConfig;
import com.valaphee.cyclone.config.Config;
import com.valaphee.cyclone.context.Context;
import com.valaphee.cyclone.engine.EngineSubsystem;
import com.valaphee.cyclone.serialization.Serializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Default
 *
 * @author valaphee
 */
public final class CacheSubsystem
		implements EngineSubsystem
{
	private JedisPool redisPool;

	@Override
	public String getName()
	{
		return "Cache";
	}

	@Override
	public void preStartup(final Context context)
	{
		final CacheConfig config = context.get(Config.class).getCache();
		final JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(config.getConnectionPoolSize());
		context.set(JedisPool.class, redisPool = new JedisPool(jedisPoolConfig, config.getHost(), config.getPort(), 30_000, config.getAuth(), 0, context.get(Config.class).getCommunication().getId().toString()));
		context.set(Serializer.class, new Serializer());
	}

	@Override
	public void shutdown()
	{
		if (redisPool != null)
		{
			redisPool.close();
		}
	}
}
