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
public final class GroupRepository
		implements CacheLoader<Long, GroupData>, EvictionListener<GroupData>
{
	private final Cache<Long, GroupData> cache;
	private final EbeanServer database;

	public GroupRepository(final JedisPool redisPool, final Serializer serializer, final EbeanServer database)
	{
		serializer.register(GroupData.class);
		cache = new RedisCache<>("cyclone:group", this, this, redisPool, serializer, Long.class, GroupData.class);
		this.database = database;
	}

	public Collection<Long> getAllGroups()
	{
		return database.find(GroupTable.class).findIds();
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

	@Override
	public GroupData load(final Long key)
	{
		final GroupTable group = database.find(GroupTable.class, key);
		if (group != null)
		{
			final GroupData value = new GroupData();

			value.id = group.getId();
			value.name = group.getName();

			final Set<GroupVariableTable> groupVariables = group.getVariables();
			value.variables = new HashMap<>();
			groupVariables.forEach((groupVariable) ->
			{
				value.variables.put(groupVariable.getName(), groupVariable.getContent());
			});

			final Set<GroupPermissionTable> groupPermissions = group.getPermissions();
			value.permissions = new ArrayList<>();
			groupPermissions.stream().map((groupPermission) -> groupPermission.getPermission()).forEach((permission) ->
			{
				value.permissions.add(permission);
			});

			final Set<GroupInheritanceTable> groupInheritances = group.getInheritances();
			value.inheritances = new ArrayList<>();
			groupInheritances.stream().map((groupInheritance) -> groupInheritance.getParent().getId()).forEach((inheritance) ->
			{
				value.inheritances.add(inheritance);
			});

			return value;
		}

		return null;
	}

	@Override
	public void evict(final Object key, final GroupData value)
	{
		final TxScope scope = TxScope.requiresNew();
		Ebean.execute(scope, () ->
		{
			GroupTable group = database.find(GroupTable.class, key);
			if (group == null)
			{
				group = new GroupTable(value.name);
			}

			final Map<String, String> variables = new HashMap<>(value.variables);
			final Set<GroupVariableTable> groupVariables = group.getVariables();
			groupVariables.forEach((groupVariable) ->
			{
				final String content = variables.get(groupVariable.getName());
				if (content != null)
				{
					groupVariable.setContent(content);
					database.update(groupVariable);
					variables.remove(groupVariable.getName());
				}
				else
				{
					database.delete(groupVariable);
				}
			});
			for (final Map.Entry<String, String> variable : variables.entrySet())
			{
				database.insert(new GroupVariableTable(group, variable.getKey(), variable.getValue()));
			}

			final List<String> permissions = new ArrayList<>(value.permissions);
			final Set<GroupPermissionTable> groupPermissions = group.getPermissions();
			groupPermissions.forEach((groupPermission) ->
			{
				if (!permissions.contains(groupPermission.getPermission()))
				{
					database.delete(groupPermission);
				}
				else
				{
					permissions.remove(groupPermission.getPermission());
				}
			});
			for (final String permission : permissions)
			{
				database.insert(new GroupPermissionTable(group, permission));
			}

			final List<Long> inheritances = new ArrayList<>(value.inheritances);
			final Set<GroupInheritanceTable> groupInheritances = group.getInheritances();
			groupInheritances.forEach((groupInheritance) ->
			{
				if (!inheritances.contains(groupInheritance.getParent().getId()))
				{
					database.delete(groupInheritance);
				}
				else
				{
					inheritances.remove(groupInheritance.getParent().getId());
				}
			});
			for (final Long inheritance : inheritances)
			{
				database.insert(new GroupInheritanceTable(group, database.find(GroupTable.class, inheritance)));
			}

			group.setName(value.name);

			database.save(group);
		});
	}
}
