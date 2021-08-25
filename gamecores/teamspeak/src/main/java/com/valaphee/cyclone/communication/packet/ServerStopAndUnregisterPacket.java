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
public final class ServerStopAndUnregisterPacket
		implements Packet<PacketProtocolSubscriber>
{
	private UUID addressee;
	private UUID server;

	public ServerStopAndUnregisterPacket()
	{}

	public ServerStopAndUnregisterPacket(final UUID addressee, final UUID server)
	{
		this.addressee = addressee;
		this.server = server;
	}

	public UUID getAddressee()
	{
		return addressee;
	}

	public UUID getServer()
	{
		return server;
	}

	@Override
	public void read(final PacketBuffer buffer)
	{
		addressee = buffer.readGUID();
		server = buffer.readGUID();
	}

	@Override
	public void write(final PacketBuffer buffer)
	{
		buffer.writeGUID(addressee);
		buffer.writeGUID(server);
	}

	@Override
	public void handle(final PacketProtocolSubscriber subscriber)
	{
		subscriber.serverStopAndUnregister(this);
	}
}
