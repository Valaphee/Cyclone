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

/**
 * Default
 *
 * @author valaphee
 */
public final class PlayerUpdatePacket
		implements Packet<PacketProtocolSubscriber>
{
	private long player;
	private State state;
	private String name;
	private String ip;

	public PlayerUpdatePacket()
	{}

	public PlayerUpdatePacket(final long player, final State state)
	{
		this.player = player;
		this.state = state;
	}

	public PlayerUpdatePacket(final long player, final State state, final String name, final String ip)
	{
		this.player = player;
		this.state = state;
		this.name = name;
		this.ip = ip;
	}

	public long getPlayer()
	{
		return player;
	}

	public State getState()
	{
		return state;
	}

	public String getName()
	{
		return name;
	}

	public String getIp()
	{
		return ip;
	}

	@Override
	public void read(final PacketBuffer buffer)
	{
		player = buffer.readLong();
		state = State.values()[buffer.readUnsignedByte()];
		if (state.equals(State.JOINED))
		{
			name = buffer.readString();
			ip = buffer.readString();
		}
	}

	@Override
	public void write(final PacketBuffer buffer)
	{
		buffer.writeLong(player);
		buffer.writeByte(state.ordinal());
		if (state.equals(State.JOINED))
		{
			buffer.writeString(name);
			buffer.writeString(ip);
		}
	}

	@Override
	public void handle(final PacketProtocolSubscriber subscriber)
	{
		subscriber.playerUpdate(this);
	}

	public static enum State
	{
		JOINED, LEFT, SWITCHED
	}
}
