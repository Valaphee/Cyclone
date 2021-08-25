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

import com.valaphee.cyclone.account.data.UserData;
import com.valaphee.cyclone.account.data.UserPunishmentData;
import java.sql.Timestamp;
import java.util.*;

/**
 * Default
 *
 * @author valaphee
 */
public class User
		extends PermissionUnit
{
	protected UserData data;

	public User(final AccountManager manager, final UserData data)
	{
		super(manager);

		this.data = data;
	}

	public UserData getData()
	{
		return data;
	}

	void setData(final UserData data)
	{
		this.data = data;
	}

	public long getId()
	{
		return data.id;
	}

	public String getName()
	{
		return data.name;
	}

	public void setName(final String name)
	{
		data.name = name;
		flagAsModified();
	}

	@Override
	public Map<String, String> getVariables()
	{
		return data.variables;
	}

	@Override
	public String getVariable(final String name)
	{
		return data.variables.get(name);
	}

	@Override
	public void addVariable(final String name, final String content)
	{
		if (data.variables.containsKey(name))
		{
			data.variables.remove(name);
		}
		data.variables.put(name, content);
		flagAsModified();
	}

	@Override
	public boolean removeVariable(final String name)
	{
		if (data.variables.containsKey(name))
		{
			data.variables.remove(name);
			flagAsModified();

			return true;
		}

		return false;
	}

	@Override
	public Collection<String> getPermissions()
	{
		return Collections.emptyList();
	}

	@Override
	public void addPermission(final String permission)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean removePermission(final String permission)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Group getGroup()
	{
		if (data.group == null)
		{
			return manager.getDefaultGroup();
		}

		final Group group = manager.getGroup(data.group);
		if (group == null)
		{
			setGroup(manager.getDefaultGroup());

			return manager.getDefaultGroup();
		}

		return group;
	}

	public void setGroup(final Group group)
	{
		if (!manager.groupExists(group.getId()))
		{
			manager.addGroup(group);
		}

		data.group = group.getId();
		flagAsModified();
	}

	public String getIp()
	{
		return data.ip;
	}

	public void setIp(final String ip)
	{
		data.ip = ip;
		flagAsModified();
	}

	public long getPlayedTime()
	{
		if ((data.proxyServer != null) && (data.server != null))
		{
			return data.playedTime + System.currentTimeMillis() - data.joined.getTime();
		}

		return data.playedTime;
	}

	public void setPlayedTime(final long playedTime)
	{
		data.playedTime = playedTime;
		flagAsModified();
	}

	public Timestamp getJoined()
	{
		return data.joined;
	}

	public void setJoined(final Timestamp joined)
	{
		data.joined = joined;
		flagAsModified();
	}

	public Collection<UserPunishmentData> getPunishments()
	{
		return Collections.unmodifiableCollection(data.punishments);
	}

	public Collection<UserPunishmentData> getPunishments(final UserPunishmentData.Type type)
	{
		final List<UserPunishmentData> punishments = new ArrayList<>();
		getPunishments().stream().filter((punishment) -> punishment.type.equals(type)).forEach(punishments::add);

		return Collections.unmodifiableCollection(punishments);
	}

	public UserPunishmentData getActivePunishment(final UserPunishmentData.Type type)
	{
		return getPunishments(type).stream().filter((punishment) -> punishment.active).findFirst().orElse(null);
	}

	public boolean deactivatePunishment(final UserPunishmentData.Type type)
	{
		final UserPunishmentData punishment = getActivePunishment(type);
		if (punishment != null)
		{
			punishment.active = false;
			flagAsModified();

			return true;
		}

		return false;
	}

	public void addKickPunishment(final String reason, final User createdBy, final Timestamp createdAt)
	{
		data.punishments.add(new UserPunishmentData(UserPunishmentData.Type.KICK, false, reason, createdBy != null ? createdBy.getId() : null, createdAt, null));
		flagAsModified();
	}

	public void addMutePunishment(final String reason, final User createdBy, final Timestamp createdAt, final Timestamp expire)
	{
		final UserPunishmentData punishment = getActivePunishment(UserPunishmentData.Type.MUTE);
		if (punishment != null)
		{
			punishment.active = false;
		}

		data.punishments.add(new UserPunishmentData(UserPunishmentData.Type.MUTE, true, reason, createdBy != null ? createdBy.getId() : null, createdAt, expire));
		flagAsModified();
	}

	public void addBanPunishment(final String reason, final User createdBy, final Timestamp createdAt, final Timestamp expire)
	{
		final UserPunishmentData punishment = getActivePunishment(UserPunishmentData.Type.BAN);
		if (punishment != null)
		{
			punishment.active = false;
		}

		data.punishments.add(new UserPunishmentData(UserPunishmentData.Type.BAN, true, reason, createdBy != null ? createdBy.getId() : null, createdAt, expire));
		flagAsModified();
	}

	public void addCrashPunishment(final String reason, final User createdBy, final Timestamp createdAt, boolean immediately)
	{
		data.punishments.add(new UserPunishmentData(immediately ? UserPunishmentData.Type.CRASH_IMMEDIATELY : UserPunishmentData.Type.CRASH_STUCK, false, reason, createdBy != null ? createdBy.getId() : null, createdAt, null));
		flagAsModified();
	}

	public UUID getProxyServer()
	{
		return data.proxyServer;
	}

	public UUID getServer()
	{
		return data.server;
	}
}
