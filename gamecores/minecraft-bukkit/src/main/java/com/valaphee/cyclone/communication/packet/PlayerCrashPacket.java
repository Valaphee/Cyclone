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

package com.valaphee.cyclone.communication.packet;

import com.valaphee.cyclone.communication.Packet;
import com.valaphee.cyclone.communication.PacketBuffer;
import java.util.UUID;

/**
 * Default
 *
 * @author valaphee
 */
public final class PlayerCrashPacket
		implements Packet<PacketProtocolSubscriber>
{
	private UUID node;
	private long player;
	private boolean immediately;

	public PlayerCrashPacket()
	{}

	public PlayerCrashPacket(final long player, final boolean immediately)
	{
		this(null, player, immediately);
	}

	public PlayerCrashPacket(final UUID node, final long player, final boolean immediately)
	{
		this.node = node;
		this.player = player;
		this.immediately = immediately;
	}

	public UUID getNode()
	{
		return node;
	}

	public long getPlayer()
	{
		return player;
	}

	public boolean isImmediately()
	{
		return immediately;
	}

	@Override
	public void read(final PacketBuffer buffer)
	{
		player = buffer.readLong();
		immediately = buffer.readBoolean();
		if (buffer.isReadable())
		{
			node = buffer.readGUID();
		}
	}

	@Override
	public void write(final PacketBuffer buffer)
	{
		buffer.writeLong(player);
		buffer.writeBoolean(immediately);
		if (node != null)
		{
			buffer.writeGUID(node);
		}
	}

	@Override
	public void handle(final PacketProtocolSubscriber subscriber)
	{
		subscriber.playerCrash(this);
	}
}
