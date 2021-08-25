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
public final class PlayerKickPacket
		implements Packet<PacketProtocolSubscriber>
{
	private UUID node;
	private long player;
	private String reason;

	public PlayerKickPacket()
	{}

	public PlayerKickPacket(final long player, final String reason)
	{
		this(null, player, reason);
	}

	public PlayerKickPacket(final UUID node, final long player, final String reason)
	{
		this.node = node;
		this.player = player;
		this.reason = reason;
	}

	public UUID getNode()
	{
		return node;
	}

	public long getPlayer()
	{
		return player;
	}

	public String getReason()
	{
		return reason;
	}

	@Override
	public void read(final PacketBuffer buffer)
	{
		player = buffer.readLong();
		reason = buffer.readString();
		if (buffer.isReadable())
		{
			node = buffer.readGUID();
		}
	}

	@Override
	public void write(final PacketBuffer buffer)
	{
		buffer.writeLong(player);
		buffer.writeString(reason);
		if (node != null)
		{
			buffer.writeGUID(node);
		}
	}

	@Override
	public void handle(final PacketProtocolSubscriber subscriber)
	{
		subscriber.playerKick(this);
	}
}
