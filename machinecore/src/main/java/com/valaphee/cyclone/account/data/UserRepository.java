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

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.TxScope;
import com.valaphee.cyclone.cache.Cache;
import com.valaphee.cyclone.cache.CacheLoader;
import com.valaphee.cyclone.cache.EvictionListener;
import com.valaphee.cyclone.cache.RedisCache;
import com.valaphee.cyclone.serialization.Serializer;
import java.util.*;
import redis.clients.jedis.JedisPool;

/**
 * Default
 *
 * @author valaphee
 */
public final class UserRepository
		implements CacheLoader<Long, UserData>, EvictionListener<UserData>
{
	private final Cache<Long, UserData> cache;
	private final EbeanServer database;

	public UserRepository(final JedisPool redisPool, final Serializer serializer, final EbeanServer database)
	{
		serializer.register(UserPunishmentData.class);
		serializer.register(UserData.class);
		cache = new RedisCache<>("cyclone:user", this, this, redisPool, serializer, Long.class, UserData.class);
		this.database = database;
	}

	public Collection<Long> getAllUsers()
	{
		return database.find(UserTable.class).findIds();
	}

	public Collection<UserData> getCachedUsers()
	{
		return cache.getAll().values();
	}

	public UserData getUser(final long id)
	{
		return cache.get(id);
	}

	public void setUser(final UserData data)
	{
		cache.put(data.id, data);
	}

	public void removeUser(final long id)
	{
		cache.invalidate(id);
	}

	@Override
	public UserData load(final Long key)
	{
		final UserTable user = database.find(UserTable.class, key);
		if (user != null)
		{
			final UserData value = new UserData();
			value.id = user.getId();
			value.name = user.getName();
			value.joined = user.getLastLogin();
			value.playedTime = user.getPlayedTime();

			final Set<UserVariableTable> userVariables = user.getVariables();
			value.variables = new HashMap<>();
			userVariables.forEach((userVariable) ->
			{
				value.variables.put(userVariable.getName(), userVariable.getContent());
			});

			final UserIpTable userIp = database.find(UserIpTable.class).where().eq("user_id", user.getId()).orderBy().desc("validated").setMaxRows(1).findUnique();
			if (userIp != null)
			{
				value.ip = userIp.getIp();
			}

			final Set<UserPunishmentTable> userPunishments = user.getPunishments();
			value.punishments = new ArrayList<>();
			userPunishments.forEach((userPunishment) ->
			{
				value.punishments.add(new UserPunishmentData(userPunishment.getId(), userPunishment.getType(), userPunishment.isActive(), userPunishment.getReason(), userPunishment.getCreatedBy() != null ? userPunishment.getCreatedBy().getId() : null, userPunishment.getCreatedAt(), userPunishment.getExpire()));
			});

			value.group = user.getGroup() != null ? user.getGroup().getId() : null;

			return value;
		}

		return null;
	}

	@Override
	public void evict(final Object key, final UserData value)
	{
		final TxScope scope = TxScope.requiresNew();
		Ebean.execute(scope, () ->
		{
			UserTable user = database.find(UserTable.class, key);
			if (user == null)
			{
				user = new UserTable(value.name, value.joined);
			}

			final Map<String, String> variables = new HashMap<>(value.variables);
			final Set<UserVariableTable> userVariables = user.getVariables();
			userVariables.forEach((userVariable) ->
			{
				final String content = variables.get(userVariable.getName());
				if (content != null)
				{
					userVariable.setContent(content);
					database.update(userVariable);
					variables.remove(userVariable.getName());
				}
				else
				{
					database.delete(userVariable);
				}
			});
			for (final Map.Entry<String, String> variable : variables.entrySet())
			{
				database.insert(new UserVariableTable(user, variable.getKey(), variable.getValue()));
			}

			if (value.ip != null)
			{
				final UserIpTable userIp = database.find(UserIpTable.class).where().eq("user_id", user.getId()).orderBy().desc("validated").setMaxRows(1).findUnique();
				if ((userIp == null) || (!userIp.getIp().equals(value.ip)))
				{
					database.insert(new UserIpTable(user, value.ip, value.joined));
				}
				else
				{
					userIp.setValidated(value.joined);
					database.update(userIp);
				}
			}

			final List<UserPunishmentData> punishments = new ArrayList<>(value.punishments);
			final Set<UserPunishmentTable> userPunishments = user.getPunishments();
			userPunishments.forEach((userPunishment) ->
			{
				UserPunishmentData punishment = null;
				for (final UserPunishmentData found : punishments)
				{
					if ((found.id != null) && (found.id == userPunishment.getId()))
					{
						punishment = found;
					}
				}

				if (punishment != null)
				{
					userPunishment.setActive(punishment.active);
					database.update(userPunishment);
					punishments.remove(punishment);
				}
				else
				{
					database.delete(userPunishment);
				}
			});
			for (final UserPunishmentData punishment : punishments)
			{
				database.insert(new UserPunishmentTable(user, punishment.type, punishment.active, punishment.reason, punishment.createdBy != null ? database.find(UserTable.class, punishment.createdBy) : null, punishment.createdAt, punishment.expire));
			}

			user.setName(value.name);
			user.setLastLogin(value.joined);
			user.setPlayedTime(value.playedTime + (value.joined != null ? System.currentTimeMillis() - value.joined.getTime() : 0));
			if (value.group != null)
			{
				user.setGroup(database.find(GroupTable.class, value.group));
			}

			database.save(user);
		});
	}
}
