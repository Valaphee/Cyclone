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

import com.valaphee.cyclone.serialization.annotation.Index;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("PublicField")
public final class UserPunishmentData
{
	@Index(0)
	public Long id;
	@Index(1)
	public Type type;
	@Index(2)
	public boolean active;
	@Index(3)
	public String reason;
	@Index(4)
	public Long createdBy;
	@Index(5)
	public Timestamp createdAt;
	@Index(6)
	public Timestamp expire;

	public UserPunishmentData()
	{}

	public UserPunishmentData(final Type type, final boolean active, final String reason, final Long createdBy, final Timestamp createdAt, final Timestamp expire)
	{
		this(null, type, active, reason, createdBy, createdAt, expire);
	}

	public UserPunishmentData(final Long id, final Type type, final boolean active, final String reason, final Long createdBy, final Timestamp createdAt, final Timestamp expire)
	{
		this.id = id;
		this.type = type;
		this.active = active;
		this.reason = reason;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
		this.expire = expire;
	}

	@Override
	public boolean equals(final Object object)
	{
		if (object == this)
		{
			return true;
		}
		if (object instanceof UserPunishmentData)
		{
			final UserPunishmentData other = (UserPunishmentData) object;

			return Objects.equals(id, other.id);
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(id);
	}

	public static enum Type
	{
		KICK, MUTE, BAN, CRASH_IMMEDIATELY, CRASH_STUCK
	}
}
