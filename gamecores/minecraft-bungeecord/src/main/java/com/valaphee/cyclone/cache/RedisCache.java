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

import com.valaphee.cyclone.serialization.SerializationException;
import com.valaphee.cyclone.serialization.Serializer;
import com.valaphee.cyclone.util.CharsetUtil;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.JedisPool;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("unchecked")
public class RedisCache<K, V>
		extends AbstractCache<K, V>
		implements Cache<K, V>
{
	protected final JedisPool redisPool;
	protected final Serializer serializer;
	protected final byte[] nameEncoded;
	protected final Type keyType;
	protected final Type valueType;

	public RedisCache(final String name, final CacheLoader<K, V> cacheLoader, final EvictionListener<V> evictionListener, final JedisPool redisPool, final Serializer serializer, final Type keyType, final Type valueType)
	{
		super(name, cacheLoader, evictionListener);

		this.redisPool = redisPool;
		this.serializer = serializer;
		nameEncoded = name.getBytes(CharsetUtil.UTF_8);
		this.keyType = keyType;
		this.valueType = valueType;
	}

	protected byte[] encodeKey(final Object key)
	{
		final byte[] serializedKey = serializer.serialize(keyType, key);
		final byte[] encodedKey = new byte[(nameEncoded.length + serializedKey.length)];
		System.arraycopy(nameEncoded, 0, encodedKey, 0, nameEncoded.length);
		System.arraycopy(serializedKey, 0, encodedKey, nameEncoded.length, serializedKey.length);

		return encodedKey;
	}

	protected Object decodeKey(final byte[] encodedKey)
	{
		final byte[] serializedKey = new byte[(encodedKey.length - nameEncoded.length)];
		System.arraycopy(encodedKey, nameEncoded.length, serializedKey, 0, serializedKey.length);

		return serializer.deserialize(keyType, serializedKey);
	}

	@Override
	public V get(final K key)
	{
		V value = null;
		try (final BinaryJedis redis = redisPool.getResource())
		{
			final byte[] encodedKey = encodeKey(key);
			if (redis.exists(encodedKey))
			{
				value = (V) serializer.deserialize(valueType, redis.get(encodedKey));
			}
			else
			{
				if (cacheLoader != null)
				{
					value = cacheLoader.load(key);
					if (value != null)
					{
						redis.set(encodedKey, serializer.serialize(valueType, value));
						redis.rpush(nameEncoded, new byte[][]
						{
							encodedKey
						});
					}
				}
			}
		}

		return value;
	}

	@Override
	public Map<K, V> getAll()
	{
		final Map<K, V> entries = new HashMap<>();
		try (final BinaryJedis redis = redisPool.getResource())
		{
			final List<byte[]> encodedKeys = redis.lrange(nameEncoded, 0, -1);
			encodedKeys.stream().filter((encodedKey) -> (redis.exists(encodedKey))).forEach((encodedKey) ->
			{
				entries.put((K) decodeKey(encodedKey), (V) serializer.deserialize(valueType, redis.get(encodedKey)));
			});
		}

		return entries;
	}

	@Override
	public void put(final K key, final V value)
	{
		try (final BinaryJedis redis = redisPool.getResource())
		{
			final byte[] encodedKey = encodeKey(key);
			if (!redis.exists(encodedKey))
			{
				redis.rpush(nameEncoded, new byte[][]
				{
					encodedKey
				});
			}
			redis.set(encodedKey, serializer.serialize(valueType, value));
		}
	}

	@Override
	public void invalidate(final Object key)
	{
		try (final BinaryJedis redis = redisPool.getResource())
		{
			final byte[] encodedKey = encodeKey(key);
			if (redis.exists(encodedKey))
			{
				try
				{
					final V value = (V) serializer.deserialize(valueType, redis.get(encodedKey));
					if (redis.expire(encodedKey, 0) == 1)
					{
						redis.lrem(nameEncoded, 0, encodedKey);
					}
					if (evictionListener != null)
					{
						evictionListener.evict(key, value);
					}
				}
				catch (final SerializationException ex)
				{
					if (redis.expire(encodedKey, 0) == 1)
					{
						redis.lrem(nameEncoded, 0, encodedKey);
					}
				}
			}
		}
	}

	@Override
	public void invalidateAll()
	{
		try (final BinaryJedis redis = redisPool.getResource())
		{
			final List<byte[]> encodedKeys = redis.lrange(nameEncoded, 0, -1);
			encodedKeys.stream().filter((encodedKey) -> (redis.exists(encodedKey))).forEachOrdered((encodedKey) ->
			{
				try
				{
					final Object key = decodeKey(encodedKey);
					final V value = (V) serializer.deserialize(valueType, redis.get(encodedKey));
					if (redis.expire(encodedKey, 0) == 1)
					{
						redis.lrem(nameEncoded, 0, encodedKey);
					}
					if (evictionListener != null)
					{
						evictionListener.evict(key, value);
					}
				}
				catch (final SerializationException ex)
				{
					if (redis.expire(encodedKey, 0) == 1)
					{
						redis.lrem(nameEncoded, 0, encodedKey);
					}
				}
			});
		}
	}

	@Override
	public long size()
	{
		long size = 0;
		try (final BinaryJedis redis = redisPool.getResource())
		{
			if (redis.exists(nameEncoded))
			{
				size = redis.llen(nameEncoded);
			}
		}

		return size;
	}
}
