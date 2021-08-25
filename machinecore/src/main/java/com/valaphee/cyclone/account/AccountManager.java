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

package com.valaphee.cyclone.account;

import com.avaje.ebean.EbeanServer;
import com.valaphee.cyclone.account.data.*;
import com.valaphee.cyclone.context.Context;
import com.valaphee.cyclone.serialization.Serializer;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Default
 *
 * @author valaphee
 */
public final class AccountManager
{
	final Context context;
	private final JedisPool redisPool;
	private final EbeanServer database;
	private final GroupRepository groupRepository;
	private final Map<Long, Group> groups = new ConcurrentHashMap<>();
	private Group defaultGroup;
	private final Queue<Long> queuedGroups = new ConcurrentLinkedQueue<>();
	private final UserRepository userRepository;
	private final Map<Long, User> users = new ConcurrentHashMap<>();
	private final Queue<Long> queuedUsers = new ConcurrentLinkedQueue<>();

	public AccountManager(final Context context)
	{
		this.context = context;
		redisPool = this.context.get(JedisPool.class);
		database = this.context.get(EbeanServer.class);
		groupRepository = new GroupRepository(redisPool, this.context.get(Serializer.class), database);
		userRepository = new UserRepository(redisPool, this.context.get(Serializer.class), database);

		startService();
	}

	public GroupRepository getGroupRepository()
	{
		return groupRepository;
	}

	public Collection<Group> getGroups()
	{
		return groups.values();
	}

	public boolean groupExists(final Long id)
	{
		return groups.containsKey(id);
	}

	public Group getGroup(final Long id)
	{
		if (id == null)
		{
			return null;
		}

		if (!groupExists(id))
		{
			final GroupData groupData = groupRepository.getGroup(id);
			if (groupData != null)
			{
				addGroup(new Group(this, groupData));
			}
		}

		return groups.get(id);
	}

	public void addGroup(final Group group)
	{
		groups.put(group.getId(), group);
	}

	public boolean removeGroup(final long id)
	{
		if ((defaultGroup != null) && (id == defaultGroup.getId()))
		{
			return false;
		}

		if (groupExists(id))
		{
			final Group group = getGroup(id);
			if (group.isModified())
			{
				groupRepository.setGroup(group.getData());
			}

			groups.remove(id);
			groupRepository.removeGroup(id);

			return true;
		}

		return false;
	}

	public Group getDefaultGroup()
	{
		if (defaultGroup == null)
		{
			setDefaultGroup(new Group(this, groupRepository.getDefaultGroup()));
		}

		return defaultGroup;
	}

	public void setDefaultGroup(final Group defaultGroup)
	{
		if (!groupExists(defaultGroup.getId()))
		{
			groups.put(defaultGroup.getId(), defaultGroup);
		}

		this.defaultGroup = defaultGroup;
		groupRepository.setDefaultGroup(defaultGroup.getData());
	}

	public Long findGroupId(final String name)
	{
		final GroupTable group = database.find(GroupTable.class).where().eq("name", name).findUnique();
		if (group != null)
		{
			return group.getId();
		}

		return null;
	}

	public void announceGroupUpdate(final long id, final boolean publish)
	{
		if (publish)
		{
			try (final Jedis redis = redisPool.getResource())
			{
				final ByteBuffer message = ByteBuffer.allocate(Long.BYTES);
				message.putLong(id);
				redis.publish(AccountService.GROUP_UPDATE_CHANNEL, message.array());
			}
		}
		else
		{
			queuedGroups.add(id);
		}
	}

	private void updateGroups()
	{
		while (!queuedGroups.isEmpty())
		{
			final long groupId = queuedGroups.poll();
			if (groupExists(groupId))
			{
				getGroup(groupId).setData(groupRepository.getGroup(groupId));
			}
		}

		getGroups().stream().filter((group) -> (group.isModified())).forEach((group) ->
		{
			groupRepository.setGroup(group.getData());
			group.flagAsSaved();

			announceGroupUpdate(group.getId(), true);
		});
	}

	public UserRepository getUserRepository()
	{
		return userRepository;
	}

	public Collection<User> getUsers()
	{
		return users.values();
	}

	public boolean userExists(final Long id)
	{
		return users.containsKey(id);
	}

	public User getUser(final Long id)
	{
		if (id == null)
		{
			return null;
		}

		if (!userExists(id))
		{
			final UserData userData = userRepository.getUser(id);
			if (userData != null)
			{
				addUser(new Player(this, userData));
			}
		}

		return users.get(id);
	}

	public void addUser(final User user)
	{
		users.put(user.getId(), user);
	}

	public boolean removeUser(final long id)
	{
		if (userExists(id))
		{
			final User user = getUser(id);
			if (user.isModified())
			{
				userRepository.setUser(user.data);
			}

			users.remove(id);
			userRepository.removeUser(id);

			return true;
		}

		return false;
	}

	public Long findUserId(final String name)
	{
		final UserTable user = database.find(UserTable.class).where().eq("name", name).findUnique();
		if (user != null)
		{
			return user.getId();
		}

		return null;
	}

	public Long findUserIdByAssociation(final String association)
	{
		final UserAssociationTable userAssociation = database.find(UserAssociationTable.class).where().eq("linked", association).findUnique();
		if (userAssociation != null)
		{
			return userAssociation.getUser().getId();
		}

		return null;
	}

	public Long findAndSynchronizeUserIdByAssociation(final String name, final String association)
	{
		final UserTable user = database.find(UserTable.class).where().eq("name", name).findUnique();
		final UserAssociationTable userAssociation = database.find(UserAssociationTable.class).where().eq("linked", association).findUnique();
		if ((userAssociation != null) && ((user == null) || (userAssociation.getUser().getId() != user.getId())))
		{
			userAssociation.getUser().setName(name);
		}
		if (userAssociation != null)
		{
			return userAssociation.getUser().getId();
		}

		return null;
	}

	public void addAssociation(final long id, final String association)
	{
		database.insert(new UserAssociationTable(database.find(UserTable.class, id), "MC", association));
	}

	@Deprecated
	public long secureGetNewUserId(final String name, final Timestamp created)
	{
		final UserTable user = new UserTable(name, created);
		database.insert(user);

		return user.getId();
	}

	public void announceUserUpdate(final long id, final boolean publish)
	{
		if (publish)
		{
			try (final Jedis redis = redisPool.getResource())
			{
				final ByteBuffer message = ByteBuffer.allocate(Long.BYTES);
				message.putLong(id);
				redis.publish(AccountService.USER_UPDATE_CHANNEL, message.array());
			}
		}
		else
		{
			queuedUsers.add(id);
		}
	}

	private void updateUsers()
	{
		while (!queuedUsers.isEmpty())
		{
			final long userId = queuedUsers.poll();
			if (userExists(userId))
			{
				getUser(userId).setData(userRepository.getUser(userId));
			}
		}

		getUsers().stream().filter((user) -> (user.isModified())).forEach((user) ->
		{
			userRepository.setUser(user.getData());
			user.flagAsSaved();

			announceUserUpdate(user.getId(), true);
		});
	}

	public void update()
	{
		updateGroups();

		updateUsers();
	}

	private void startService()
	{
		final Thread accountService = new Thread(() ->
		{
			try (final Jedis redis = redisPool.getResource())
			{
				redis.subscribe(new AccountService(AccountManager.this), AccountService.GROUP_UPDATE_CHANNEL, AccountService.USER_UPDATE_CHANNEL);
			}
		}, "Account Service");
		accountService.setDaemon(true);
		accountService.start();
	}
}
