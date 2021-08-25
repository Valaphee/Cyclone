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

import com.valaphee.cyclone.Cyclone;
import com.valaphee.cyclone.account.data.GroupData;
import com.valaphee.cyclone.account.data.GroupRepository;
import com.valaphee.cyclone.account.data.UserData;
import com.valaphee.cyclone.account.data.UserRepository;
import com.valaphee.cyclone.account.event.*;
import com.valaphee.cyclone.communication.packet.PlayerProclamationPacket;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Default
 *
 * @author valaphee
 */
public final class AccountManager
{
	final Cyclone cyclone;
	private final JedisPool redisPool;
	private final GroupRepository groupRepository;
	private final Map<Long, Group> groups = new ConcurrentHashMap<>();
	private Group defaultGroup;
	private final Queue<Long> queuedGroups = new ConcurrentLinkedQueue<>();
	private final UserRepository userRepository;
	private final Map<Long, User> users = new ConcurrentHashMap<>();
	private final Queue<Long> queuedUsers = new ConcurrentLinkedQueue<>();
	private final Object proclamateLock = new Object();
	private final Map<UUID, CountDownLatch> pendingProclamations = new HashMap<>();
	private final Map<UUID, Long> associationToUser = new HashMap<>();
	private final Map<Long, UUID> userToAssociation = new HashMap<>();
	private final Object connectLock = new Object();
	private final Map<Long, CountDownLatch> pendingConnects = new HashMap<>();

	public AccountManager(final Cyclone cyclone)
	{
		this.cyclone = cyclone;
		redisPool = this.cyclone.getRedisPool();
		groupRepository = new GroupRepository(redisPool, this.cyclone.getSerializer());
		userRepository = new UserRepository(redisPool, this.cyclone.getSerializer());

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
		cyclone.getProxy().getPluginManager().callEvent(new GroupLoadEvent(group));
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
			cyclone.getProxy().getPluginManager().callEvent(new GroupUnloadEvent(group));
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
				final Group group = getGroup(groupId);
				group.setData(groupRepository.getGroup(groupId));
				cyclone.getProxy().getPluginManager().callEvent(new GroupUpdateEvent(group));
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
				addUser(new User(this, userData));
			}
		}

		return users.get(id);
	}

	public void addUser(final User user)
	{
		users.put(user.getId(), user);
		cyclone.getProxy().getPluginManager().callEvent(new UserLoadEvent(user));
	}

	public boolean removeUser(final long id)
	{
		if (userExists(id))
		{
			final User user = getUser(id);
			cyclone.getProxy().getPluginManager().callEvent(new UserUnloadEvent(user));
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

	public Long findUserId(final UUID id)
	{
		return associationToUser.get(id);
	}

	public UUID findAssociation(final Long user)
	{
		return userToAssociation.get(user);
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
				final User user = getUser(userId);
				user.setData(userRepository.getUser(userId));
				cyclone.getProxy().getPluginManager().callEvent(new UserUpdateEvent(user));
			}
		}

		getUsers().stream().filter((user) -> (user.isModified())).forEach((user) ->
		{
			userRepository.setUser(user.getData());
			user.flagAsSaved();

			announceUserUpdate(user.getId(), true);
		});
	}

	public long proclamate(final String name, final UUID association)
	{
		Cyclone.getInstance().getConnectionManager().getOutgoingConnection().sendPacket(new PlayerProclamationPacket(name, association.toString()));

		CountDownLatch latch;
		synchronized (proclamateLock)
		{
			if (!pendingProclamations.containsKey(association))
			{
				pendingProclamations.put(association, latch = new CountDownLatch(1));
			}
			else
			{
				latch = pendingProclamations.get(association);
			}
		}

		try
		{
			if (!latch.await(2_500L, TimeUnit.MILLISECONDS))
			{
				Cyclone.getInstance().getConnectionManager().getOutgoingConnection().sendPacket(new PlayerProclamationPacket(name, association.toString()));

				synchronized (proclamateLock)
				{
					if (!pendingProclamations.containsKey(association))
					{
						pendingProclamations.put(association, latch = new CountDownLatch(1));
					}
					else
					{
						latch = pendingProclamations.get(association);
					}
				}
				if (!latch.await(2_500L, TimeUnit.MILLISECONDS))
				{
					throw new IllegalStateException("resolve() tooks too long.");
				}
			}
		}
		catch (final InterruptedException ignore)
		{}

		return associationToUser.get(association);
	}

	public void proclamated(final UUID association, final long user)
	{
		associationToUser.put(association, user);
		userToAssociation.put(user, association);

		synchronized (proclamateLock)
		{
			if (pendingProclamations.containsKey(association))
			{
				pendingProclamations.remove(association).countDown();
			}
		}
	}

	public void release(final long user)
	{
		final UUID profile = userToAssociation.get(user);
		if (profile != null)
		{
			associationToUser.remove(profile);
			userToAssociation.remove(user);
		}
	}

	public void connect(final long user)
	{
		final CountDownLatch latch;
		synchronized (connectLock)
		{
			if (!pendingConnects.containsKey(user))
			{
				pendingConnects.put(user, latch = new CountDownLatch(1));
			}
			else
			{
				latch = pendingConnects.get(user);
			}
		}

		try
		{
			if (!latch.await(2_500L, TimeUnit.MILLISECONDS))
			{
				throw new IllegalStateException("connect() tooks too long.");
			}
		}
		catch (final InterruptedException ignore)
		{}
	}

	public void connected(final long user)
	{
		synchronized (connectLock)
		{
			if (pendingConnects.containsKey(user))
			{
				pendingConnects.remove(user).countDown();
			}
		}
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
