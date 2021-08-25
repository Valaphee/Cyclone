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
public final class HandshakeFinalPacket
		implements Packet<PacketProtocolSubscriber>
{
	private Status status;

	public HandshakeFinalPacket()
	{}

	public HandshakeFinalPacket(final Status status)
	{
		this.status = status;
	}

	public Status getStatus()
	{
		return status;
	}

	@Override
	public void read(final PacketBuffer buffer)
	{
		status = Status.values()[buffer.readUnsignedByte()];
	}

	@Override
	public void write(final PacketBuffer buffer)
	{
		buffer.writeByte(status.ordinal());
	}

	@Override
	public void handle(final PacketProtocolSubscriber subscriber)
	{
		subscriber.handshakeFinish(this);
	}

	public static enum Status
	{
		ACCEPTED, DECLINED, AUTHENTICATION_REQUIRED
	}
}
