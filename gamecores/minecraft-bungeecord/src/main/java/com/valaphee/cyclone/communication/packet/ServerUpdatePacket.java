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
import com.valaphee.cyclone.server.data.ServerData;

/**
 * Default
 *
 * @author valaphee
 */
public final class ServerUpdatePacket
		implements Packet<PacketProtocolSubscriber>
{
	private int playerCount;
	private double ticksPerSecond;
	private ServerData.State state;
	private String stateDescription;

	public ServerUpdatePacket()
	{}

	public ServerUpdatePacket(final int playerCount, final double ticksPerSecond, final ServerData.State state, final String stateDescription)
	{
		this.playerCount = playerCount;
		this.ticksPerSecond = ticksPerSecond;
		this.state = state;
		this.stateDescription = stateDescription;
	}

	public int getPlayerCount()
	{
		return playerCount;
	}

	public double getTicksPerSecond()
	{
		return ticksPerSecond;
	}

	public ServerData.State getState()
	{
		return state;
	}

	public String getStateDescription()
	{
		return stateDescription;
	}

	@Override
	public void read(final PacketBuffer buffer)
	{
		playerCount = buffer.readInt();
		ticksPerSecond = buffer.readDouble();
		state = ServerData.State.values()[buffer.readUnsignedByte()];
		stateDescription = buffer.readString();
	}

	@Override
	public void write(final PacketBuffer buffer)
	{
		buffer.writeInt(playerCount);
		buffer.writeDouble(ticksPerSecond);
		buffer.writeByte(state.ordinal());
		buffer.writeString(stateDescription);
	}

	@Override
	public void handle(final PacketProtocolSubscriber subscriber)
	{
		subscriber.serverUpdate(this);
	}
}
