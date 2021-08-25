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

import com.valaphee.cyclone.util.CharsetUtil;
import java.nio.ByteBuffer;
import java.util.Arrays;
import redis.clients.jedis.BinaryJedisPubSub;

/**
 * Default
 *
 * @author valaphee
 */
public final class AccountService
		extends BinaryJedisPubSub
{
	public static final byte[] GROUP_UPDATE_CHANNEL = "GroupUpdate".getBytes(CharsetUtil.UTF_8),
			USER_UPDATE_CHANNEL = "UserUpdate".getBytes(CharsetUtil.UTF_8);
	private final AccountManager manager;

	public AccountService(final AccountManager manager)
	{
		this.manager = manager;
	}

	@Override
	public void onMessage(final byte[] channel, final byte[] message)
	{
		final ByteBuffer wrapped = ByteBuffer.wrap(message);
		if (Arrays.equals(channel, GROUP_UPDATE_CHANNEL))
		{
			manager.announceGroupUpdate(wrapped.getLong(), false);
		}
		else if (Arrays.equals(channel, USER_UPDATE_CHANNEL))
		{
			manager.announceUserUpdate(wrapped.getLong(), false);
		}
	}
}
