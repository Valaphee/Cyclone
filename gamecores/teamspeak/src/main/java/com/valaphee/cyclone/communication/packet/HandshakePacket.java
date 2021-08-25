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
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.UUID;

/**
 * Default
 *
 * @author valaphee
 */
public final class HandshakePacket
		implements Packet<PacketProtocolSubscriber>
{
	private Mode mode;
	private UUID id;
	private SocketAddress address;
	private boolean negotiated;

	public HandshakePacket()
	{}

	public HandshakePacket(final Mode mode, final UUID id, final SocketAddress address, final boolean negotiated)
	{
		this.mode = mode;
		this.id = id;
		this.address = address;
		this.negotiated = negotiated;
	}

	public Mode getMode()
	{
		return mode;
	}

	public UUID getId()
	{
		return id;
	}

	public SocketAddress getAddress()
	{
		return address;
	}

	public boolean isNegotiated()
	{
		return negotiated;
	}

	@Override
	public void read(final PacketBuffer buffer)
	{
		mode = Mode.values()[buffer.readByte()];
		id = buffer.readGUID();
		address = new InetSocketAddress(buffer.readString(), buffer.readUnsignedShort());
		negotiated = buffer.readBoolean();
	}

	@Override
	public void write(final PacketBuffer buffer)
	{
		buffer.writeByte(mode.ordinal());
		buffer.writeGUID(id);
		buffer.writeString(((InetSocketAddress) address).getHostString());
		buffer.writeShort(((InetSocketAddress) address).getPort());
		buffer.writeBoolean(negotiated);
	}

	@Override
	public void handle(final PacketProtocolSubscriber subscriber)
	{
		subscriber.handshake(this);
	}

	public static enum Mode
	{
		MASTER, SLAVE, CONTROLLER
	}
}
